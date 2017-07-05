/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author leonardo
 */
public class Ranking {

    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    private String playerName;
    private String xp;

    public Ranking(String position, String playerName, String xp) {
        this.position = position;
        this.playerName = playerName;
        this.xp = xp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getXp() {
        return xp;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

}
