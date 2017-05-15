/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class Request {

    private Header header;
    private String method;
    private String protocol;
    private String path;
    private HashMap<String, String> queryParams;
    private BufferedReader bufferedReader;

    public Request(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.queryParams = new HashMap<>();

        try {
            processRequest();
        } catch (IOException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void processRequest() throws IOException {
        this.header = new Header();
        String[] firstLine = this.bufferedReader.readLine().trim().split(" ");
        this.method = firstLine[0];
        this.path = URLDecoder.decode(firstLine[1], "UTF-8");
        this.protocol = firstLine[2];

        this.parseQueryParams();

        String nextLinesHeader = this.bufferedReader.readLine().trim();
        while (!nextLinesHeader.isEmpty()) {
            String[] lineHeader = nextLinesHeader.split(":");
            this.header.addAttribute(lineHeader[0], lineHeader[1].trim().split(","));
            nextLinesHeader = bufferedReader.readLine().trim();
        }
    }

    private void parseQueryParams() throws UnsupportedEncodingException {
        String[] parameters = this.path.split("\\?");
        if (parameters.length == 2) {
            for (String parameter : parameters[1].split("\\&")) {
                String[] query = parameter.split("=");
                this.queryParams.put(URLDecoder.decode(query[0], "UTF-8"), URLDecoder.decode(query[1], "UTF-8"));
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean containsQuery(String property) {
        return this.queryParams.containsKey(property);
    }

    public String getQuery(String property) {
        return this.queryParams.get(property);
    }

}
