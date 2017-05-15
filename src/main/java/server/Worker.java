/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import http.Request;
import http.Response;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class Worker implements Runnable {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private String pathDirectory;

    public Worker(Socket socket, String pathDirectory) {
        this.socket = socket;
        this.pathDirectory = pathDirectory;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        Request request = new Request(this.inputStream);
        try {
            RequestHandle requestHandle = new RequestHandle(request);
            
            requestHandle.processRequestHandle().sendResponse(outputStream);
            
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
