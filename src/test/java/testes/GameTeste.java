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
import game.Level;
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
        List<Level> levels = new ArrayList<>();
        Level level = new Level(0, 0, 0, 0, 0);
        levels.add(level);
        Player playerPO = new Player(0, "Marco", "123", 3, 1, levels);

        Game gamePO = new Game(1);
        Game gamePO1;
        gamePO.setPlayerIdPlayer(playerPO);

        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.insert(playerPO);

        GameDAO gameDAO = new GameDAO();
        gameDAO.insert(gamePO);
        gamePO1 = gameDAO.obter(0);
        Assert.assertEquals(gamePO.getPlayerIdPlayer().getNomePlayer(), gamePO1.getPlayerIdPlayer().getNomePlayer());
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
            Player p = new Player(player.getIdPlayer(), player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual(), player.getLevelList());
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
