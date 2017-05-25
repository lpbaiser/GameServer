/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controller.PlayerController;
import game.Player;
import gameprotocol.GameProtocolResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Worker implements Runnable {

    private Request request;
    private Response response;
    private Socket clientSocket;
//    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            inputStream = this.clientSocket.getInputStream();
            outputStream = this.clientSocket.getOutputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
//            this.bufferedReader = new BufferedReader(inputStreamReader);
//            this.bufferedWriter = new BufferedWriter(outputStreamWriter);

        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

    @Override
    public void run() {
        try {
            this.request = new Request(inputStream);
            String path = request.getResource();
            request.setPath(path);
            response = choose(path);
            response.enviar(outputStream);
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Response choose(String path) throws IOException {
        if (request.getMethod().equals("GET")) {
            if (path.endsWith(".html")) {
                response = getResponseFile(request, path, "text/html");
            } else if (path.endsWith(".css")) {
                response = getResponseFile(request, path, "text/css");
            } else if (path.endsWith(".js")) {
                response = getResponseFile(request, path, "text/javascript");
            } else if (path.endsWith(".txt")) {
                response = getResponseFile(request, path, "text/plain");
            } else if (path.startsWith("/files/")) {
                path = path.replaceFirst("/files/", "");
                response = getJSON(request, path);
            } else if (path.startsWith("/game")) {
                GameProcess gameHandler = new GameProcess();
                GameProtocolResponse gcpResponse = gameHandler.getGameResource(path);
                response = getJSON(request, gcpResponse);
            } else if (resourceExists(path)) {
                if (path.contains(".")) {
                    response = getResponseFile(request, path, detectType(path));
                } else {
                    path = "index.html";
                    response = getResponseFile(request, path, "text/html");
                }
            } else {
                path = "-";
                response = getResponseFile(request, path, "text/html");
            }
        } else if (request.getMethod().equals("POST")) {
            path = request.getResource();
            if (path.startsWith("/game")) {
                GameProcess gameHandler = new GameProcess();
                GameProtocolResponse postGameResource = gameHandler.postGameResource(request);
                Gson gson = new Gson();
                response = getJSON(request, gson.toJson(postGameResource));
            } else {
                path = "-";
                response = getResponseFile(request, path, "text/html");
            }
        } else {
            throw new AssertionError(request.getMethod());
        }

        return response;
    }

    private Response getResponseFile(Request request, String stringPath, String type) throws IOException {
        int code;
        Path path;
        String message;
        String protocol;
        byte[] value;
        stringPath = HttpServer.RESOURCES_PATH + stringPath;
        File file = new File(stringPath);
        if (file.exists()) {
            code = 200;
            message = "OK";
            path = Paths.get(stringPath);
        } else {
            code = 404;
            message = "Not Found";
            type = "text/html";
            path = Paths.get(HttpServer.RESOURCES_PATH + "404.html");
        }
        protocol = request.getProtocol();
//        if (type.equals("text/html")) {
//            //Processa 
//            value = processDynamicHtml(file);
//        } else {
//            value = Files.readAllBytes(path);
//        }
            value = Files.readAllBytes(path);
        String dateGMT = getDateGTM();

        Response response = new Response(protocol, code, message);

        response.setHeader("Location", "http://localhost:8000/");
        response.setHeader("Date", dateGMT);
        response.setHeader("Server", "MarcoServer/1.0");
        response.setHeader("Content-Type", type);
        response.setHeader("Content-Length", String.valueOf(value.length));
        response.setResponseValue(message.getBytes());
        response.setResponseCode(code);
        response.setResponseValue(value);
        return response;
    }

    private Response getJSON(Request request, Object object) {

        int code;
        Path path;
        String message;
        String protocol;
        byte[] value;
        if (object != null) {
            Gson gson = new Gson();
            String toJson = gson.toJson(object);
            value = toJson.getBytes();
        } else {
            value = "".getBytes();
        }
        code = 200;
        message = "OK";
        protocol = request.getProtocol();

        String dateGMT = getDateGTM();

        Response response = new Response(protocol, code, message);

        response.setHeader("Location", "http://localhost:8000/");
        response.setHeader("Date", dateGMT);
        response.setHeader("Server", "MarcoServer/1.0");
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Content-Length", String.valueOf(value.length));

        response.setResponseValue(message.getBytes());
        response.setResponseCode(code);
        response.setResponseValue(value);
        return response;
    }

    private Response getJSON(Request request, String stringPath) {

        int code;
        Path path;
        String message;
        String protocol;
        byte[] value;
        stringPath = HttpServer.RESOURCES_PATH + stringPath;
        try {
            JsonObject jsonFolder = new JsonObject();
            File folder = new File(stringPath);
            if (!folder.exists()) {
                throw new FileNotFoundException();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            File[] files = folder.listFiles();
            for (File file : files) {
                String lastModified = simpleDateFormat.format(new Date(file.lastModified()));
                long totalSpace = file.length();
                boolean isNavigable = isNavigable(file);
                JsonObject jsonFile = new JsonObject();
                jsonFile.addProperty("size", totalSpace);
                jsonFile.addProperty("lastModified", lastModified);
                jsonFile.addProperty("isNavigable", isNavigable);
                jsonFolder.add(file.getName(), jsonFile);
            }
            value = jsonFolder.toString().getBytes();
        } catch (FileNotFoundException ex) {
            value = "".getBytes();
        }
        code = 200;
        message = "OK";
        protocol = request.getProtocol();

        String dateGMT = getDateGTM();

        Response response = new Response(protocol, code, message);

        response.setHeader("Location", "http://localhost:8000/");
        response.setHeader("Date", dateGMT);
        response.setHeader("Server", "MarcoServer/1.0");
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Content-Length", String.valueOf(value.length));
        response.setResponseValue(message.getBytes());
        response.setResponseCode(code);
        response.setResponseValue(value);
        return response;

    }

    private String getDateGTM() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        String dateGMT = simpleDateFormat.format(date) + " GMT";
        return dateGMT;
    }

    private Response gameProcess(String path) {
        Response response = null;
        if (path.contains("list-players")) {
            response = this.listPlayers(path);
        } else if (path.contains("player")) {
            response = this.getPlayer(path);
        }
        return response;
    }

    private Response listPlayers(String stringPath) {

        int code;
        String message;
        String protocol;
        byte[] value;
        stringPath = HttpServer.RESOURCES_PATH + stringPath;
        String contentType = "application/json";

        PlayerController playerController = new PlayerController();
        List<Player> players = playerController.listAllPlayers();
        String playersJson = null;

        if (players != null) {
            playersJson = new Gson().toJson(players);
        } else {
            throw new Error("Listagem de players retornou nula!");
        }

        if (playersJson != null) {
            code = 200;
            message = "OK";
        } else {
            code = 404;
            message = "File Not Found";
            contentType = "text/html";
            File file = new File(HttpServer.RESOURCES_PATH, "404.html");
        }
        value = playersJson.getBytes();
        protocol = request.getProtocol();

        String dateGMT = getDateGTM();

        Response response = new Response(protocol, code, message);

        response.setHeader("Location", "http://localhost:8000/");
        response.setHeader("Date", dateGMT);
        response.setHeader("Server", "MarcoServer/1.0");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", String.valueOf(value.length));
        response.setResponseValue(message.getBytes());
        response.setResponseCode(code);
        response.setResponseValue(value);
        return response;
    }

    private Response listThopies() {
        throw new Error(" listThopies não implementado");

//        String contentType = "application/json";
//
//        PlayerController playerController = new PlayerController();
//        List<Trophy> trophys = new ArrayList<Trophy>();
//
//        return null;
    }

    private Response getPlayer(String stringPath) {

        int code;
        Path path;
        String message;
        String protocol;
        byte[] value;
        stringPath = HttpServer.RESOURCES_PATH + stringPath;
        String contentType = "application/json";
        if (this.request.containsQuery("player_id")) {
            String playerId = this.request.getQuery("player_id");
            PlayerController playerController = new PlayerController();
            Player player = playerController.getPlayeerById(playerId);
            String playerJson = null;
            if (player != null) {
                playerJson = new Gson().toJson(player);
            } else {
                throw new Error("Busca de player retornou nula!");
            }

            if (playerJson != null) {
                code = 200;
                message = "OK";
            } else {
                code = 404;
                message = "File Not Found";
                contentType = "text/html";
                File file = new File(HttpServer.RESOURCES_PATH, "404.html");
            }
            value = playerJson.getBytes();

            protocol = request.getProtocol();

            String dateGMT = getDateGTM();

            Response response = new Response(protocol, code, message);

            response.setHeader("Location", "http://localhost:8000/");
            response.setHeader("Date", dateGMT);
            response.setHeader("Server", "MarcoServer/1.0");
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Length", String.valueOf(value.length));
            response.setResponseValue(message.getBytes());
            response.setResponseCode(code);
            response.setResponseValue(value);
            return response;
        }
        return null;
    }

    private boolean resourceExists(String path) {
        File file = new File(HttpServer.RESOURCES_PATH, path);
        return file.exists();
    }

    private boolean isNavigable(File file) {
        if (file.isDirectory()) {
            return true;
        } else if (file.getName().endsWith(".js")) {
            return true;
        } else if (file.getName().endsWith(".html")) {
            return true;
        } else if (file.getName().endsWith(".css")) {
            return true;
        } else if (file.getName().endsWith(".txt")) {
            return true;
        }
        return false;
    }

    private String detectType(String url) {
        if (url.endsWith(".html")) {
            return "text/html";
        } else if (url.endsWith(".css")) {
            return "text/css";
        } else if (url.endsWith(".json")) {
            return "application/json";
        } else if (url.endsWith(".js")) {
            return "text/javascript";
        } else if (url.endsWith(".png")) {
            return "image/png";
        } else if (url.endsWith(".txt")) {
            return "text/plain";
        }
        return "application/octet-stream";
    }

    private byte[] processDynamicHtml(File file) throws IOException {
        byte[] value;
        String html;
        String footer;
        html = String.valueOf(file);
//        value = Files.readAllBytes(Paths.get(file.getPath()));
        footer = "";
        value = (html + footer).getBytes();
        return value;
    }
}
