/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.LevelDAO;
import game.Level;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class LevelController {
    
    LevelDAO levelDAO;
    
    public LevelController() {
        this.levelDAO = new LevelDAO();
    }
    
    public Level getLevelAtual(List<Level> levelList, int idLevel) {
        Level level1 = new Level(0, 0, 0, 0, 0, 0);
        for (Level level : levelList) {
            if (level.getIdLevel() == idLevel) {
                if (level1.getXp() < level.getXp()) {
                    level1 = level;
                }
            }
        }
        level1.setPlayerNomePlayer(null);
        return level1;
    }
    
}
