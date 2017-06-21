/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author leonardo
 */
@Embeddable
public class PlayerHasTrophyPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "player_id_player")
    private int playerIdPlayer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trophy_id_trophy")
    private int trophyIdTrophy;

    public PlayerHasTrophyPK() {
    }

    public PlayerHasTrophyPK(int playerIdPlayer, int trophyIdTrophy) {
        this.playerIdPlayer = playerIdPlayer;
        this.trophyIdTrophy = trophyIdTrophy;
    }

    public int getPlayerIdPlayer() {
        return playerIdPlayer;
    }

    public void setPlayerIdPlayer(int playerIdPlayer) {
        this.playerIdPlayer = playerIdPlayer;
    }

    public int getTrophyIdTrophy() {
        return trophyIdTrophy;
    }

    public void setTrophyIdTrophy(int trophyIdTrophy) {
        this.trophyIdTrophy = trophyIdTrophy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) playerIdPlayer;
        hash += (int) trophyIdTrophy;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlayerHasTrophyPK)) {
            return false;
        }
        PlayerHasTrophyPK other = (PlayerHasTrophyPK) object;
        if (this.playerIdPlayer != other.playerIdPlayer) {
            return false;
        }
        if (this.trophyIdTrophy != other.trophyIdTrophy) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.PlayerHasTrophyPK[ playerIdPlayer=" + playerIdPlayer + ", trophyIdTrophy=" + trophyIdTrophy + " ]";
    }
    
}
