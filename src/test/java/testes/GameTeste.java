/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import com.google.gson.Gson;
import dao.GameDAO;
import dao.PlayerDAO;
import dao.TrophyDAO;
import game.Game;
import game.Player;
import game.Trophy;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author leonardo
 */
public class GameTeste {

    @Test
    public void createNewGameAndNewPlayer() {
        Player playerPO = new Player();
        playerPO.setNomePlayer("Marco");

        Game gamePO = new Game();
        gamePO.setPlayerIdPlayer(playerPO);

        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.insert(playerPO);

        GameDAO gameDAO = new GameDAO();
        gameDAO.insert(gamePO);

    }

//    @Test
    public void listPlayers() {
        PlayerDAO playerDAO = new PlayerDAO();
        List<Player> players = playerDAO.list();
        for (Player player : players) {
            System.out.println(player.getNomePlayer());
        }
    }

//    @Test
    public void test() {
        PlayerDAO playerDAO = new PlayerDAO();
        List<Player> players = playerDAO.list();
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player player : players) {
            Player p = new Player(player.getIdPlayer(), player.getNomePlayer());
            list.add(p);
        }
//        System.out.println("ID: "+ list.iterator().next().getIdPlayer());
//        System.out.println("nome: "+list.iterator().next().getNomePlayer());
        String json = new Gson().toJson(list.get(0));
        System.out.println(json);
    }
    
//    @Test
    public void addTrophy(){
        TrophyDAO trophyDAO = new TrophyDAO();
        List<Player> players = new ArrayList<>();
        Trophy trophy = new Trophy("10 coins", 30, "Win 10 coins", "descricao", players);
        
        trophyDAO.insert(trophy);
        
    }

}
