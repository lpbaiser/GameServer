/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author marco
 */
@Entity
@Table(name = "trophy")
@NamedQueries({
    @NamedQuery(name = "Trophy.findAll", query = "SELECT t FROM Trophy t")})
public class Trophy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_trophy")
    private Integer idTrophy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name_trophy")
    private String nameTrophy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xp_trophy")
    private double xpTrophy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "title_trophy")
    private String titleTrophy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "description_trophy")
    private String descriptionTrophy;
    @ManyToMany(mappedBy = "trophyList")
    private List<Player> playerList;

    public Trophy() {
    }

    public Trophy(Integer idTrophy) {
        this.idTrophy = idTrophy;
    }

    public Trophy(Integer idTrophy, String nameTrophy, double xpTrophy, String titleTrophy, String descriptionTrophy) {
        this.idTrophy = idTrophy;
        this.nameTrophy = nameTrophy;
        this.xpTrophy = xpTrophy;
        this.titleTrophy = titleTrophy;
        this.descriptionTrophy = descriptionTrophy;
    }

    public Integer getIdTrophy() {
        return idTrophy;
    }

    public void setIdTrophy(Integer idTrophy) {
        this.idTrophy = idTrophy;
    }

    public String getNameTrophy() {
        return nameTrophy;
    }

    public void setNameTrophy(String nameTrophy) {
        this.nameTrophy = nameTrophy;
    }

    public double getXpTrophy() {
        return xpTrophy;
    }

    public void setXpTrophy(double xpTrophy) {
        this.xpTrophy = xpTrophy;
    }

    public String getTitleTrophy() {
        return titleTrophy;
    }

    public void setTitleTrophy(String titleTrophy) {
        this.titleTrophy = titleTrophy;
    }

    public String getDescriptionTrophy() {
        return descriptionTrophy;
    }

    public void setDescriptionTrophy(String descriptionTrophy) {
        this.descriptionTrophy = descriptionTrophy;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTrophy != null ? idTrophy.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trophy)) {
            return false;
        }
        Trophy other = (Trophy) object;
        if ((this.idTrophy == null && other.idTrophy != null) || (this.idTrophy != null && !this.idTrophy.equals(other.idTrophy))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.Trophy[ idTrophy=" + idTrophy + " ]";
    }
    
}
