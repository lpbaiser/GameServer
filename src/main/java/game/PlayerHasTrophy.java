/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author leonardo
 */
@Entity
@Table(name = "player_has_trophy")
@NamedQueries({
    @NamedQuery(name = "PlayerHasTrophy.findAll", query = "SELECT p FROM PlayerHasTrophy p")})
public class PlayerHasTrophy implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlayerHasTrophyPK playerHasTrophyPK;
    @JoinColumn(name = "trophy_id_trophy", referencedColumnName = "id_trophy", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Trophy trophy;

    public PlayerHasTrophy() {
    }

    public PlayerHasTrophy(PlayerHasTrophyPK playerHasTrophyPK) {
        this.playerHasTrophyPK = playerHasTrophyPK;
    }

    public PlayerHasTrophy(int playerIdPlayer, int trophyIdTrophy) {
        this.playerHasTrophyPK = new PlayerHasTrophyPK(playerIdPlayer, trophyIdTrophy);
    }

    public PlayerHasTrophyPK getPlayerHasTrophyPK() {
        return playerHasTrophyPK;
    }

    public void setPlayerHasTrophyPK(PlayerHasTrophyPK playerHasTrophyPK) {
        this.playerHasTrophyPK = playerHasTrophyPK;
    }

    public Trophy getTrophy() {
        return trophy;
    }

    public void setTrophy(Trophy trophy) {
        this.trophy = trophy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playerHasTrophyPK != null ? playerHasTrophyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlayerHasTrophy)) {
            return false;
        }
        PlayerHasTrophy other = (PlayerHasTrophy) object;
        if ((this.playerHasTrophyPK == null && other.playerHasTrophyPK != null) || (this.playerHasTrophyPK != null && !this.playerHasTrophyPK.equals(other.playerHasTrophyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.PlayerHasTrophy[ playerHasTrophyPK=" + playerHasTrophyPK + " ]";
    }
    
}
