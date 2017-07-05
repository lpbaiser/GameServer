/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.LevelDAO;
import game.Level;
import java.util.ArrayList;
import java.util.Comparator;
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

    public List<Level> getTeenBetter() {
        List<Level> levelList = levelDAO.list();
        List<Level> newLevelList = new ArrayList<>();
        levelList.sort(new Comparator<Level>() {
            @Override
            public int compare(Level o1, Level o2) {
                return Double.compare(o1.getXp(), o2.getXp());
            }
        });
        int i = 10;
        for (Level level : levelList) {
            i++;
            if (levelList.size() <= i) {
                newLevelList.add(level);
            }
        }
        return newLevelList;
    }
}
