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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author leonardo
 */
@Entity
@Table(name = "images")
@NamedQueries({
    @NamedQuery(name = "Images.findAll", query = "SELECT i FROM Images i")})
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_images")
    private Integer idImages;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "image")
    private String image;
    @JoinColumn(name = "player_nome_player", referencedColumnName = "nome_player")
    @ManyToOne(optional = false)
    private Player playerNomePlayer;

    public Images() {
    }

    public Images(Integer idImages) {
        this.idImages = idImages;
    }

    public Images(Integer idImages, String image) {
        this.idImages = idImages;
        this.image = image;
    }

    public Images(String image, Player player) {
        this.image = image;
        this.playerNomePlayer = player;
    }

    public Integer getIdImages() {
        return idImages;
    }

    public void setIdImages(Integer idImages) {
        this.idImages = idImages;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        hash += (idImages != null ? idImages.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Images)) {
            return false;
        }
        Images other = (Images) object;
        if ((this.idImages == null && other.idImages != null) || (this.idImages != null && !this.idImages.equals(other.idImages))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "game.Images[ idImages=" + idImages + " ]";
    }

}
