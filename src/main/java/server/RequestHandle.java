/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controller.PlayerController;
import game.Player;
import game.Trophy;
import http.Header;
import http.Request;
import http.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author leonardo
 */
public class RequestHandle {

    private final Request request;
    private String path;

    public RequestHandle(Request request) throws IOException {
        this.request = request;
        this.path = this.request.getPath();
    }

    public Response processRequestHandle() throws IOException {
        Response response = null;
        String method = this.request.getMethod();
        if (method.equals("GET")) {
            if (this.path.endsWith(".html") || this.path.endsWith(".htm")) {
                response = this.getFile("text/html");
            } else if (this.path.endsWith(".js")) {
                response = this.getFile("text/javascript");
            } else if (this.path.endsWith(".css")) {
                response = this.getFile("text/css");
            } else if (this.path.endsWith(".jpeg") || this.path.endsWith(".jpg")) {
                response = this.getFile("image/jpeg");
            } else if (this.path.endsWith(".png")) {
                response = this.getFile("image/png");
            } else if (this.path.endsWith(".pdf")) {
                response = this.getFile("application/pdf");
            } else if (this.path.endsWith(".txt")) {
                response = this.getFile("text/plain");
            } else if (this.path.startsWith("/")) {
                this.path = "index.html";
                response = this.getFile("text/html");
            } else if (this.path.startsWith("/game")) {
                response = this.gameProcess();
            } else if (this.path.startsWith("/files/")) {
                this.path = this.path.replaceFirst("/files", "");
                response = this.listFiles("application/json");
            } else {
                response = this.getFile("text/plain");
            }
        } else if (method.equals("POST")) {
            if (this.path.startsWith("/game")) {
                System.out.println("implementar");
            }
        }
        return response;
    }

    private Response getFile(String contentType) throws IOException {
        System.out.println("contentType: " + contentType);
        int statusCode;
        String message;
        byte[] content;

        File file = new File(Server.PATH_FILES, this.path);

        if (file.exists()) {
            statusCode = 200;
            message = "OK";
        } else {
            statusCode = 404;
            message = "File Not Found";
            contentType = "text/html";
            file = new File(Server.PATH_FILES, "404.html");
        }

        content = Files.readAllBytes(file.toPath());

        Header header = new Header();
        header.addAttribute("Location", ("http://" + Server.ADDRESS + ":" + String.valueOf(Server.PORT) + "/"));
        header.addAttribute("Date", getTimeZone());
        header.addAttribute("Content-Type", contentType);
        header.addAttribute("Content-Lenght", String.valueOf(content.length));

        Response response = new Response(statusCode, request.getProtocol(), message, header, content);
        return response;
    }

    private String getTimeZone() {
        final Date currentTime = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(currentTime);
    }

    private Response gameProcess() {
        Response response = null;
        if (this.path.contains("list-players")) {
            response = this.listPlayers();
        } else if (this.path.contains("player")) {
            response = this.getPlayer();
        }
        return response;
    }

    private Response listPlayers() {
        int statusCode;
        String message;
        byte[] content;
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
            statusCode = 200;
            message = "OK";
        } else {
            statusCode = 404;
            message = "File Not Found";
            contentType = "text/html";
            File file = new File(Server.PATH_FILES, "404.html");
        }

        content = playersJson.getBytes();

        Header header = new Header();
        header.addAttribute("Location", ("http://" + Server.ADDRESS + ":" + String.valueOf(Server.PORT) + "/"));
        header.addAttribute("Date", getTimeZone());
        header.addAttribute("Content-Type", contentType);
        header.addAttribute("Content-Lenght", String.valueOf(content.length));

        Response response = new Response(statusCode, request.getProtocol(), message, header, content);
        return response;

    }

    private Response listThopies() {
        int statusCode;
        String message;
        byte[] content;
        String contentType = "application/json";

        PlayerController playerController = new PlayerController();
        List<Trophy> trophys = new ArrayList<Trophy>();

        return null;
    }

    private Response getPlayer() {
        int statusCode;
        String message;
        byte[] content;
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
                statusCode = 200;
                message = "OK";
            } else {
                statusCode = 404;
                message = "File Not Found";
                contentType = "text/html";
                File file = new File(Server.PATH_FILES, "404.html");
            }

            content = playerJson.getBytes();

            Header header = new Header();
            header.addAttribute("Location", ("http://" + Server.ADDRESS + ":" + String.valueOf(Server.PORT) + "/"));
            header.addAttribute("Date", getTimeZone());
            header.addAttribute("Content-Type", contentType);
            header.addAttribute("Content-Lenght", String.valueOf(content.length));

            Response response = new Response(statusCode, request.getProtocol(), message, header, content);
            return response;
        }
        return null;
    }

    private Response listFiles(String contentType) {
        int statusCode;
        String message;
        byte[] content;
        File folder = new File(Server.PATH_FILES, this.path);
        JsonObject folderJson = new JsonObject();
        if (!folder.exists()) {
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    JsonObject fileJson = new JsonObject();
                    fileJson.addProperty("last_modified", getFormatedDate(new Date(file.lastModified())));
                    fileJson.addProperty("size", file.length());
                    folderJson.add(file.getName(), fileJson);
                }
            }
        }
        if (folderJson != null) {
            statusCode = 200;
            message = "OK";
            content = folderJson.toString().getBytes();
        } else {
            content = "".getBytes();
            statusCode = 404;
            message = "File Not Found";
            contentType = "text/html";
            File file = new File(Server.PATH_FILES, "404.html");
        }
        Header header = new Header();
        header.addAttribute("Location", ("http://" + Server.ADDRESS + ":" + String.valueOf(Server.PORT) + "/"));
        header.addAttribute("Date", getTimeZone());
        header.addAttribute("Content-Type", contentType);
        header.addAttribute("Content-Lenght", String.valueOf(content.length));
        Response response = new Response(statusCode, request.getProtocol(), message, header, content);
        return response;
    }

    private String getFormatedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(date);
    }

}
