/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.PlayerDAO;
import game.Level;
import game.Player;
import game.Trophy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class PlayerController {

    PlayerDAO playerDAO;

    public PlayerController() {
        this.playerDAO = new PlayerDAO();
    }

    public Player getPlayerById(String id) {
        int playerId = Integer.parseInt(id);
        Player player = playerDAO.obter(playerId);
        Player p = new Player(player.getIdPlayer(), player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual(), player.getLevelList());
        return p;
    }
    public Player getPlayerById(int id) {
        Player player = playerDAO.obter(id);
        Player p = new Player(player.getIdPlayer(), player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual(), player.getLevelList());
        return p;
    }

    public ArrayList<Player> listAllPlayers() {
        List<Player> players = playerDAO.list();
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player player : players) {
            Player p = new Player(player.getIdPlayer(), player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual(), player.getLevelList());
            list.add(p);
        }
        return list;
    }

    public double updateScore(double newScore, int idPlayer) {
        Player player = playerDAO.obter(idPlayer);
        List<Level> levelList = player.getLevelList();
//        Não tem o level add ainda
        Level level = levelList.get(player.getIdLevelAtual());
        double score = level.getCoins();
        if (newScore > score) {
            level.setCoins(newScore);
        } else {
            newScore = score;
        }
        levelList.set(player.getIdLevelAtual(), level);
        player.setLevelList(levelList);
        playerDAO.update(player);
        return newScore;
    }
}
