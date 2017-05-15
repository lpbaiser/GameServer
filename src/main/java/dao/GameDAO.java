/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import game.Game;

/**
 *
 * @author leonardo
 */
public class GameDAO extends GenericDAO<Game> {

    public GameDAO() {
        super(Game.class);
    }

}
