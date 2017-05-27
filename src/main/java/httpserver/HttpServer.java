/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

/**
 *
 * @author marco
 */
import dao.GameDAO;
import dao.PlayerDAO;
import dao.TrophyDAO;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {

    public static final String RESOURCES_PATH = "./www/";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Server is Running");
        ServerSocket serverSocket = null;
        try {
            /* cria um socket "servidor" associado a porta 8000
          já aguardando conexões
             */
            ServerSocket servidor = new ServerSocket(8000);
            //Limita a x conecções 
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                //aceita conexao que vier
                Socket socket = servidor.accept();
                GameDAO gameDAO = new GameDAO();
                PlayerDAO playerDAO = new PlayerDAO();
                TrophyDAO trophyDAO = new TrophyDAO();
                Worker serverWorker = new Worker(socket, gameDAO, playerDAO, trophyDAO);
                pool.execute(serverWorker);
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
