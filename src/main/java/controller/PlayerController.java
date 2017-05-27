/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.PlayerDAO;
import game.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class PlayerController {

    PlayerDAO playerDAO;

    public PlayerController() {
        playerDAO = new PlayerDAO();
    }
    
    public Player getPlayerById(String id){
        int playerId = Integer.parseInt(id);
        Player player = playerDAO.obter(playerId);
        Player p = new Player(player.getIdPlayer(), player.getNomePlayer(), player.getSenha());
        return p;
    }
    
    public ArrayList<Player> listAllPlayers() {
        List<Player> players = playerDAO.list();
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player player : players) {
            Player p = new Player(player.getIdPlayer(), player.getNomePlayer(), player.getSenha());
            list.add(p);
        }
        return list;
    }

}
