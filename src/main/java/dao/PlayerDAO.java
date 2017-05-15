/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import game.Player;

/**
 *
 * @author leonardo
 */
public class PlayerDAO extends GenericDAO<Player> {

    public PlayerDAO() {
        super(Player.class);
    }

}
