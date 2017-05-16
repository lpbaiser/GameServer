/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author leonardo
 */
public class Server {

    public static int PORT = 8000;
    public static String ADDRESS = "localhost";
    public static final String PATH_FILES = "/dados/Documents/BCC/7-Periodo/DesenvolvimentoWebBack/FileServer/resources/";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started in: " + serverSocket.getInetAddress().getHostName() + ":" + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Worker(socket, PATH_FILES)).start();
            }

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
