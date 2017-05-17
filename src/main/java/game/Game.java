/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "game")
@NamedQueries({
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g")})
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_game")
    private Integer idGame;
    @JoinColumn(name = "player_id_player", referencedColumnName = "id_player")
    @ManyToOne(optional = false)
    private Player playerIdPlayer;
    private Collection<Profile> profiles;

    public Game() {
        profiles = new ArrayList<>();
    }

    public Collection<Profile> getProfiles() {
        return profiles;
    }

    public Profile getProfile(Profile profileToFind) {
        for (Profile profile : profiles) {
            if (profile.getName().equals(profileToFind.getName())) {
                return profile;
            }
        }
        return null;
    }

    public void setProfiles(Collection<Profile> profiles) {
        this.profiles = profiles;
    }

    public void insertProfile(Profile profile) {
        this.profiles.add(profile);
    }

    public Game(Integer idGame) {
        this.idGame = idGame;
        profiles = new ArrayList<>();
    }

    public Integer getIdGame() {
        return idGame;
    }

    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    public Player getPlayerIdPlayer() {
        return playerIdPlayer;
    }

    public void setPlayerIdPlayer(Player playerIdPlayer) {
        this.playerIdPlayer = playerIdPlayer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGame != null ? idGame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Game)) {
            return false;
        }
        Game other = (Game) object;
        if ((this.idGame == null && other.idGame != null) || (this.idGame != null && !this.idGame.equals(other.idGame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.Game[ idGame=" + idGame + " ]";
    }

}
