/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author leonardo
 */
public class Response {

    private final int statusCode;
    private final String protocol;
    private final String message;
    private final Header header;
    private final byte[] content;

    public Response(int statusCode, String protocol, String message, Header header, byte[] content) {
        this.statusCode = statusCode;
        this.protocol = protocol;
        this.message = message;
        this.header = header;
        this.content = content;
    }

    public void sendResponse(OutputStream outputStream) throws IOException {
        outputStream.write(this.buildResponse());
        outputStream.write(this.content);
        outputStream.flush();
    }

    private byte[] buildResponse() {
        StringBuilder string = new StringBuilder();
        string.append(this.protocol);
        string.append(" ");
        string.append(this.statusCode);
        string.append(" ");
        string.append(this.message);
        string.append("\r\n");
        for (Map.Entry<String, List<String>> attribute : this.header.getAttributes().entrySet()) {
            string.append(attribute.getKey());
            string.append(": ");
            string.append(Arrays.toString(attribute.getValue().toArray()).replace("[", "").replace("]", ""));
            string.append("\r\n");
        }
        string.append("\r\n");
        return string.toString().getBytes();
    }

}
