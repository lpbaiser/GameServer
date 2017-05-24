/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author leonardo
 */
@Entity
@Table(name = "player")
@NamedQueries({
    @NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p")})
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_player")
    private Integer idPlayer;
    @Basic(optional = false)
    @Column(name = "nome_player")
    private String nomePlayer;
    @JoinTable(name = "player_has_trophy", joinColumns = {
        @JoinColumn(name = "player_id_player", referencedColumnName = "id_player")}, inverseJoinColumns = {
        @JoinColumn(name = "trophy_id_trophy", referencedColumnName = "id_trophy")})
    @ManyToMany
    private List<Trophy> trophyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playerIdPlayer")
    private List<Game> gameList;

    public Player() {
    }

    public Player(Integer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Player(Integer idPlayer, String nomePlayer) {
        this.idPlayer = idPlayer;
        this.nomePlayer = nomePlayer;
    }

    public Integer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Integer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getNomePlayer() {
        return nomePlayer;
    }

    public void setNomePlayer(String nomePlayer) {
        this.nomePlayer = nomePlayer;
    }

    public List<Trophy> getTrophyList() {
        return trophyList;
    }

    public void setTrophyList(List<Trophy> trophyList) {
        this.trophyList = trophyList;
    }

    public void setATrophy(Trophy trophy) {
        this.trophyList.add(trophy);
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlayer != null ? idPlayer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        if ((this.idPlayer == null && other.idPlayer != null) || (this.idPlayer != null && !this.idPlayer.equals(other.idPlayer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.Player[ idPlayer=" + idPlayer + " ]";
    }

}
