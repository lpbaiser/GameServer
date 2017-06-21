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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nome_player")
    private String nomePlayer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "senha")
    private String senha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "life")
    private double life;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_level_atual")
    private int idLevelAtual;
    @JoinTable(name = "player_has_trophy", joinColumns = {
        @JoinColumn(name = "player_id_player", referencedColumnName = "id_player")}, inverseJoinColumns = {
        @JoinColumn(name = "trophy_id_trophy", referencedColumnName = "id_trophy")})
    @ManyToMany
    private List<Trophy> trophyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playerIdPlayer")
    private List<Game> gameList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playerIdPlayer")
    private List<Level> levelList;


    public Player(Integer idPlayer, String nomePlayer, String senha, double life, int idLevelAtual, List<Level> levels) {
        this.idPlayer = idPlayer;
        this.nomePlayer = nomePlayer;
        this.senha = senha;
        this.life = life;
        this.idLevelAtual = idLevelAtual;
        this.levelList = levels;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public int getIdLevelAtual() {
        return idLevelAtual;
    }

    public void setIdLevelAtual(int idLevelAtual) {
        this.idLevelAtual = idLevelAtual;
    }

    public List<Trophy> getTrophyList() {
        return trophyList;
    }

    public void setTrophyList(List<Trophy> trophyList) {
        this.trophyList = trophyList;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public List<Level> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<Level> levelList) {
        this.levelList = levelList;
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
