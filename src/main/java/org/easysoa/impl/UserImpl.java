/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easysoa.impl;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easysoa.api.Friends;
import org.easysoa.api.Users;
import org.easysoa.api.Utils;
import org.easysoa.jpa.Provider;
import org.easysoa.model.Civility;
import org.easysoa.model.User;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author dirix
 */
public class UserImpl implements Users {

    private static final String WORKSPACE_PATH = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"easysoawebsitesimplified";
    @Reference
    public Provider<EntityManager> db;
    @Reference
    public Friends friends;
    @Reference
    public Utils utils;

    @Override
    public User connect(String login, String password) {
        try {
            EntityManager em = db.get();
            Query query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.login = :login AND u.password = :password");
            query.setParameter("login", login);
            query.setParameter("password", password);
            User user = (User) query.getSingleResult();
            user.setFriendRequests(friends.getFriendRequests(user));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User createAccount(String login, String password, String confirmPassword, String mail, String name, String surname, String civility, String town, String country, String birthday) {
        try {
            EntityManager em = db.get();
            em.getTransaction().begin();
            Civility civilityValue = Civility.fromValue(civility);
            User user = new User(login, name, surname, password, mail, civilityValue, town, country, birthday);
            em.persist(user);
            em.getTransaction().commit();
            //utils.sendMailAccountCreation(user);
            this.createWorkspace(user);
            em.getTransaction().begin();
            User userx = em.find(User.class, user.getId());
            em.getTransaction().commit();
            return userx;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User modifyInformations(User user) {
        EntityManager em = db.get();
        em.getTransaction().begin();
        User userx = em.find(User.class, user.getId());
        userx.setUserName(user.getUserName());
        userx.setPassword(user.getPassword());
        userx.setMail(user.getMail());
        userx.setTown(user.getTown());
        userx.setCivility(user.getCivility());
        em.getTransaction().commit();
        return userx;
    }


    private void createWorkspace(User user) {
    	try{
        String path = WORKSPACE_PATH;
        File space = new File(path);
        if(!space.exists()){
        	space.mkdirs();
        }
        File f = new File(path + File.separator + user.getLogin());
        f.mkdirs();
        EntityManager em = db.get();
        em.getTransaction().begin();
        User userx = em.find(User.class, user.getId());
        userx.setWorkspaceUrl(f.getPath());
        em.getTransaction().commit();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @Override
    public User searchUser(Long id) {
        EntityManager em = db.get();
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        return user;
    }
    
    @Override
    public User searchUser(String idString) {
        Long id = Long.parseLong(idString);
        return this.searchUser(id);
    }
}
