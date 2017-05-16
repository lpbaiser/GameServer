/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author marco
 */
public class Request {

    private String protocol;
    private String resource;
    private String method;
    private boolean keepAlive = true;
    private long tempoLimite = 3000;
    private Map<String, List> cabecalhos;
    private HashMap<String, String> queryParams;
    private String path;

    public Request(InputStream entrada) throws IOException {
        path = "";
        this.queryParams = new HashMap<>();

        BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
        System.out.println("Requisição: ");
        /* Lê a primeira linha
         contem as informaçoes da requisição
         */
        String linhaRequisicao = buffer.readLine();
        if (linhaRequisicao == null) {
            throw new RuntimeException("Empty line reading");
        }
        //quebra a string pelo espaço em branco
        String[] dadosReq = linhaRequisicao.split(" ");
        //pega o method
        this.setMethod(dadosReq[0]);
        //paga o caminho do arquivo
        this.setResource(dadosReq[1]);
        //pega o protocol
        this.setProtocol(dadosReq[2]);
        String dadosHeader = buffer.readLine();
        //Enquanto a linha nao for nula e nao for vazia
        while (dadosHeader != null && !dadosHeader.isEmpty()) {
            System.out.println(dadosHeader);
            String[] linhaCabecalho = dadosHeader.split(":");
            this.setCabecalho(linhaCabecalho[0], linhaCabecalho[1].trim().split(","));
            dadosHeader = buffer.readLine();
        }
        //se existir a chave Connection no cabeçalho
        if (this.getCabecalhos().containsKey("Connection")) {
            //seta o manterviva a conexao se o connection for keep-alive
            this.setKeepAlive(this.getCabecalhos().get("Connection").get(0).equals("keep-alive"));
        }
        this.parseQueryParams();

    }

    public void setCabecalho(String chave, String... valores) {
        if (cabecalhos == null) {
            cabecalhos = new TreeMap<>();
        }
        cabecalhos.put(chave, Arrays.asList(valores));
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

    public boolean containsQuery(String property) {
        return this.queryParams.containsKey(property);
    }

    public String getQuery(String property) {
        return this.queryParams.get(property);
    }

    //getters e setters vão aqui
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public long getTempoLimite() {
        return tempoLimite;
    }

    public void setTempoLimite(long tempoLimite) {
        this.tempoLimite = tempoLimite;
    }

    public Map<String, List> getCabecalhos() {
        return cabecalhos;
    }

    public void setCabecalhos(Map<String, List> cabecalhos) {
        this.cabecalhos = cabecalhos;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
