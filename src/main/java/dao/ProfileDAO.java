/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import game.Profile;

/**
 *
 * @author marco
 */
public class ProfileDAO extends GenericDAO<Profile>  {

    public ProfileDAO() {
        super(Profile.class);
    }
    
}
