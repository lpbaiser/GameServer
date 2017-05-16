/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author marco
 */
public class Response {

    private String protocol;
    private int responseCode; //200 ou 400 ...
    private String message; //<html> js bla
    private byte[] responseValue; //
    private Map<String, List> headers; //

    public Response() {

    }

    public Response(String protocolo, int codigoResposta, String mensagem) {
        this.protocol = protocolo;
        this.responseCode = codigoResposta;
        this.message = mensagem;
    }

    /**
     * Envia os dados da resposta ao cliente.
     *
     * @param out
     * @throws IOException
     */
    public void enviar(OutputStream out) throws IOException {
        //escreve o headers em bytes
        out.write(toBytes());
        out.write(this.responseValue);
        //encerra a resposta
        out.flush();
    }

    /**
     * Insere um item de cabeçalho no mapa
     *
     * @param key
     * @param values lista com um ou mais valores para esta chave
     */
    public void setHeader(String key, String... values) {
        if (headers == null) {
            headers = new TreeMap<>();
        }
        headers.put(key, Arrays.asList(values));
    }

    /**
     * pega o tamanho da resposta em bytes
     *
     * @return retorna o valor em bytes do tamanho do conteudo da resposta
     * convertido em string
     */
    public String getTamanhoResposta() {
        return getResponseValue().length + "";
    }

    /**
     * converte o cabecalho em string.
     *
     * @return retorna o cabecalho em bytes
     */
    private byte[] toBytes() {
        return this.toString().getBytes();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(protocol).append(" ").append(responseCode).append(" ").append(message).append("\r\n");
        for (Map.Entry<String, List> entry : headers.entrySet()) {
            str.append(entry.getKey());
            String stringCorrigida = Arrays.toString(entry.getValue().toArray()).replace("[", "").replace("]", "");
            str.append(": ").append(stringCorrigida).append("\r\n");
        }
        str.append("\r\n");
        return str.toString();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(byte[] responseValue) {
        this.responseValue = responseValue;
    }

    public Map<String, List> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List> headers) {
        this.headers = headers;
    }
}