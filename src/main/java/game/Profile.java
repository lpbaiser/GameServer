/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author romulo
 */
public class Profile implements Serializable {

    private final String name;
    private final ArrayList<Trophy> trophies;

    public Profile(String name) {
        this.name = name;
        this.trophies = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Trophy> getTrophies() {
        return this.trophies;
    }

    public void addTrophy(Trophy trophy) {
        if (!getTrophies().contains(trophy)) {
            getTrophies().add(trophy);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) obj;
        return other.getName().equals(getName());
    }

    @Override
    public String toString() {
        StringBuilder dump = new StringBuilder();
        dump.append("{");
        dump.append("\"name\"").append(":");
        dump.append("\"").append(getName()).append("\"");
        dump.append("}");
        return dump.toString();
    }

}
