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
import javax.persistence.Id;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playerNomePlayer")
    private List<Game> gameList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playerNomePlayer")
    private List<Level> levelList;

    public Player() {
    }

    public Player(String nomePlayer, String senha) {
        this.nomePlayer = nomePlayer;
        this.senha = senha;
    }

    public Player(String nomePlayer) {
        this.nomePlayer = nomePlayer;
    }

    public Player(String nomePlayer, String senha, double life, int idLevelAtual) {
        this.nomePlayer = nomePlayer;
        this.senha = senha;
        this.life = life;
        this.idLevelAtual = idLevelAtual;
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
        hash += (nomePlayer != null ? nomePlayer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        if ((this.nomePlayer == null && other.nomePlayer != null) || (this.nomePlayer != null && !this.nomePlayer.equals(other.nomePlayer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.Player[ nomePlayer=" + nomePlayer + " ]";
    }

}
