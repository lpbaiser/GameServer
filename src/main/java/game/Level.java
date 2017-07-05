/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author leonardo
 */
@Entity
@Table(name = "level")
@NamedQueries({
    @NamedQuery(name = "Level.findAll", query = "SELECT l FROM Level l")})
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_level")
    private Integer idLevel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coins")
    private double coins;
    @Basic(optional = false)
    @NotNull
    @Column(name = "save_point_x")
    private double savePointX;
    @Basic(optional = false)
    @NotNull
    @Column(name = "save_pont_y")
    private double savePontY;
    @Basic(optional = false)
    @NotNull
    @Column(name = "save_point_id")
    private double savePointId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "life")
    private double life;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xp")
    private double xp;
    @JoinColumn(name = "player_nome_player", referencedColumnName = "nome_player")
    @ManyToOne(optional = false)
    private Player playerNomePlayer;

    public Level() {
    }

    public Level(Integer idLevel) {
        this.idLevel = idLevel;
    }

    public Level(double coins, double savePointX, double savePontY, double savePointId, double life, double xp) {
        this.coins = coins;
        this.savePointX = savePointX;
        this.savePontY = savePontY;
        this.savePointId = savePointId;
        this.life = life;
        this.xp = xp;
    }

    public Integer getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(Integer idLevel) {
        this.idLevel = idLevel;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public double getSavePointX() {
        return savePointX;
    }

    public void setSavePointX(double savePointX) {
        this.savePointX = savePointX;
    }

    public double getSavePontY() {
        return savePontY;
    }

    public void setSavePontY(double savePontY) {
        this.savePontY = savePontY;
    }

    public double getSavePointId() {
        return savePointId;
    }

    public void setSavePointId(double savePointId) {
        this.savePointId = savePointId;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public Player getPlayerNomePlayer() {
        return playerNomePlayer;
    }

    public void setPlayerNomePlayer(Player playerNomePlayer) {
        this.playerNomePlayer = playerNomePlayer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLevel != null ? idLevel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level)) {
            return false;
        }
        Level other = (Level) object;
        if ((this.idLevel == null && other.idLevel != null) || (this.idLevel != null && !this.idLevel.equals(other.idLevel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.Level[ idLevel=" + idLevel + " ]";
    }
    
}
