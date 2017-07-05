/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import com.google.gson.Gson;
import gameprotocol.GameProtocolRequest;
import gameprotocol.GameProtocolResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author leonardo
 */
public class ServerComunication implements Runnable {

    public static final int MULTICAST_PORT = 8889;
    public static final String MULTICAST_IP = "225.1.2.3";
    private MulticastSocket multicastSocket;
    private GameProtocolRequest gameProtocolRequest;
    private ArrayList<GameProtocolResponse> gameProtocolResponses;
    private long timeout;
    private boolean estouPerguntando = false;

    public ServerComunication() {
        gameProtocolResponses = new ArrayList<>();
        try {
            multicastSocket = new MulticastSocket(MULTICAST_PORT);
            multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_IP));
        } catch (IOException ex) {
            Logger.getLogger(ServerComunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            //tratar deadlock distribuido
            //tratar loopback
            try {
                byte[] sharedBuffer = new byte[3];
                DatagramPacket request = new DatagramPacket(sharedBuffer, sharedBuffer.length);
                multicastSocket.receive(request);
                if (Arrays.equals(sharedBuffer, "req".getBytes())) {
                    DatagramPacket response = new DatagramPacket("rep".getBytes(), 3);
                    multicastSocket.send(response);
                } else if (estouPerguntando && Arrays.equals(sharedBuffer, "rep".getBytes())) {
                    InetAddress address = request.getAddress();
                    URL url = new URL("http://" + address.getHostAddress() + ":8000" + "/games");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    Gson gson = new Gson();
                    String toJson = gson.toJson(gameProtocolRequest);
                    outputStream.write(toJson.getBytes());
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    Stream<String> lines = bufferedReader.lines();
                    Iterator<String> iterator = lines.iterator();
                    String next = "";
                    while (iterator.hasNext()) {
                        next += iterator.next();
                    }
                    GameProtocolResponse fromJson = gson.fromJson(next, GameProtocolResponse.class);
                    this.gameProtocolResponses.add(fromJson);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerComunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void sendRequest() {
        try {
            byte[] sharedBuffer = "req".getBytes();
            DatagramPacket request = new DatagramPacket(sharedBuffer, sharedBuffer.length);
            multicastSocket.receive(request);
        } catch (IOException ex) {
            Logger.getLogger(ServerComunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MulticastSocket getMulticastSocket() {
        return multicastSocket;
    }

    public void setMulticastSocket(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
    }

    public GameProtocolRequest getGameProtocolRequest() {
        return gameProtocolRequest;
    }

    public void setGameProtocolRequest(GameProtocolRequest gameProtocolRequest) {
        this.gameProtocolRequest = gameProtocolRequest;
    }

    public ArrayList<GameProtocolResponse> getGameProtocolResponses() {
        return gameProtocolResponses;
    }

    public void setGameProtocolResponses(ArrayList<GameProtocolResponse> gameProtocolResponses) {
        this.gameProtocolResponses = gameProtocolResponses;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isEstouPerguntando() {
        return estouPerguntando;
    }

    public void setEstouPerguntando(boolean estouPerguntando) {
        this.estouPerguntando = estouPerguntando;
    }

}
