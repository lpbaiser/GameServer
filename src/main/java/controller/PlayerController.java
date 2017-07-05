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

    public void updateTrophyList(Player player, Trophy trophy) {
        boolean haveTrophy = false;
        for (Trophy trophy1 : player.getTrophyList()) {
            if (trophy1.getNameTrophy().equals(trophy.getNameTrophy())) {
                if (trophy1.getXpTrophy() < trophy.getXpTrophy()) {
                    trophy1 = trophy;
                    haveTrophy = true;
                }
            }
        }
        if (!haveTrophy) {
            player.getTrophyList().add(trophy);
        }
    }

    public List<Trophy> getTrophyList(Player player) {
        List<Trophy> trophyList = player.getTrophyList();
        for (Trophy trophy : trophyList) {
            trophy.setPlayerList(null);
        }
        return trophyList;
    }

    public Player getPlayerById(String id) {

        Player player = playerDAO.obter(id);
        //PlayerPK playerPk = new PlayerPK(player.getPlayerPK().getNomePlayer());
        Player p = new Player(player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual());
        return p;
    }

    public ArrayList<Player> listAllPlayers() {
        List<Player> players = playerDAO.list();
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player player : players) {
            //PlayerPK playerPk = new PlayerPK(player.getPlayerPK().getNomePlayer());
            Player p = new Player(player.getNomePlayer(), player.getSenha(), player.getLife(), player.getIdLevelAtual());
            list.add(p);
        }
        return list;
    }

    public double updateScore(double newScore, String idPlayer) {
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

    public void updateLevel(Level level, Player player) {
        List<Level> levelList = player.getLevelList();
        if (levelList == null) {
            levelList = new ArrayList<>();
        }
        levelList.add(level);
        player.setLevelList(levelList);
        player.setIdLevelAtual(level.getIdLevel());
        playerDAO.update(player);
    }

}
