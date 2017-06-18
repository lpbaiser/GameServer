/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import com.google.gson.Gson;
import dao.GameDAO;
import dao.PlayerDAO;
import game.Game;
import game.Player;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author leonardo
 */
public class GameTeste {

    public GameTeste() {
    }

    @Test
    public void createNewGameAndNewPlayer() {
        Player playerPO = new Player();
        playerPO.setNomePlayer("lpbaiser");
        playerPO.setSenha("123");
        playerPO.setIdLevelAtual(1);
        playerPO.setLife(3);

        Game gamePO = new Game(1);
        Game gamePO1;
        gamePO.setPlayerNomePlayer(playerPO);

        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.insert(playerPO);

        GameDAO gameDAO = new GameDAO();
        gameDAO.insert(gamePO);
        gamePO1 = gameDAO.obter(1);
        Assert.assertEquals(gamePO.getPlayerNomePlayer(), gamePO1.getPlayerNomePlayer());
    }

    //@Test
    public void listPlayers() {
        PlayerDAO playerDAO = new PlayerDAO();
        List<Player> players = playerDAO.list();
        for (Player player : players) {
            System.out.println(player.getNomePlayer());
        }
    }

    //@Test
    public void test() {
        PlayerDAO playerDAO = new PlayerDAO();
        List<Player> players = playerDAO.list();
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player player : players) {
            Player p = new Player(player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual());
            list.add(p);
        }
//        System.out.println("ID: "+ list.iterator().next().getIdPlayer());
//        System.out.println("nome: "+list.iterator().next().getNomePlayer());
        String json = new Gson().toJson(list.get(0));
        System.out.println(json);
    }

//    @Test
//    public void addTrophy() {
//        TrophyDAO trophyDAO = new TrophyDAO();
//        List<Player> players = new ArrayList<>();
//        Trophy trophy = new Trophy("10 coins", 30, "Win 10 coins", "descricao", players);
//
//        trophyDAO.insert(trophy);
//
//    }
}
