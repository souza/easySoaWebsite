/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easysoa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easysoa.api.Friends;
import org.easysoa.api.Utils;
import org.easysoa.model.FriendRequest;
import org.easysoa.model.Point;
import org.easysoa.model.User;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.jpa.Provider;

/**
 *
 * @author dirix
 */
public class FriendsImpl implements Friends {

    @Reference
    public Provider<EntityManager> db;
    @Reference
    public Utils utils;

    @Override
    public List<User> getFriendsAroundMe(User user, String kmsRadius) {
        List<User> users = new ArrayList<User>();
        EntityManager em = db.get();
        try {
            Query queryMyCountry = em.createQuery("SELECT NEW org.easysoa.model.Point(t.latitude,t.longitude) FROM Town t WHERE t.country = :country and t.townName = :town");
            queryMyCountry.setParameter("country", user.getCountry());
            queryMyCountry.setParameter("town", user.getTown());
            Point userCoord = (Point)queryMyCountry.getSingleResult();

            Query query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.country = :country");
            query.setParameter("country", user.getCountry());
            List<User> usersInSameCountry = query.getResultList();
            for (User userElement : usersInSameCountry) {
                Query queryUserCountry = em.createQuery("SELECT NEW org.easysoa.model.Point(t.latitude,t.longitude) FROM Town t WHERE t.country = :country AND t.townName = :town");
                queryUserCountry.setParameter("country", userElement.getCountry());
                queryUserCountry.setParameter("town", userElement.getTown());
                Point userTown = (Point)queryUserCountry.getSingleResult();
                if (isInSelectedRadius(userCoord, userTown, Integer.parseInt(kmsRadius))) {
                    users.add(userElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> searchFriends(String username, String surname, String town) {
        EntityManager em = db.get();
        Query query = null;
        if (username != null && !username.equals("") && surname != null && !surname.equals("") && town != null && !town.equals("")) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.userName = :username AND u.surname = :surname AND u.town = :town");
            query.setParameter("surname", surname);
            query.setParameter("username", username);
            query.setParameter("town", town);
        } else if ((username == null || username.equals("")) && surname != null && !surname.equals("") && town != null && !town.equals("")) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.surname = :surname AND u.town = :town");
            query.setParameter("surname", surname);
            query.setParameter("town", town);
        } else if (username != null && !username.equals("") && (surname == null || surname.equals("")) && town != null && !town.equals("")) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.userName = :username AND u.town = :town");
            query.setParameter("username", username);
            query.setParameter("town", town);
        } else if (username != null && !username.equals("") && surname != null && !surname.equals("") && (town == null || town.equals(""))) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.userName = :username AND u.surname = :surname");
            query.setParameter("surname", surname);
            query.setParameter("username", username);
        } else if (username != null && !username.equals("") && (surname == null || surname.equals("")) && (town == null || town.equals(""))) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.userName = :username");
            query.setParameter("username", username);
        } else if ((username == null || username.equals("")) && (surname == null || surname.equals("")) && town != null && !town.equals("")) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.town = :town");
            query.setParameter("town", town);
        } else if ((username == null || username.equals("")) && surname != null && !surname.equals("") && (town == null || town.equals(""))) {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u WHERE u.surname = :surname");
            query.setParameter("surname", surname);
        } else {
            query = em.createQuery("SELECT DISTINCT u FROM AppUser u");
        }
        java.util.List<User> search = query.getResultList();
        return search;
    }

    private boolean isInSelectedRadius(Point p1, Point p2, int kmsRadius) {
        double R = 6371; // km : Earth's radius
        double dLat = Math.toRadians(Math.abs(p2.y - p1.y));
        double dLon = Math.toRadians(Math.abs(p2.x - p1.x));
        double lat1 = Math.toRadians(p1.y);
        double lat2 = Math.toRadians(p2.y);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d < kmsRadius;
    }

    @Override
    public void sendFriendRequest(User originUser, User targetUser) {
        try {
            FriendRequest friendRequest = new FriendRequest(originUser, targetUser);
            EntityManager em = db.get();
            em.getTransaction().begin();
            em.persist(friendRequest);
            em.getTransaction().commit();
            utils.sendMailForFriendRequest(originUser, targetUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    @Override
    public List<FriendRequest> getFriendRequests(User user) {
        EntityManager em = db.get();
        Query query = em.createQuery("SELECT DISTINCT fr FROM FriendRequest fr WHERE fr.targetUser = :user");
        query.setParameter("user", user);
        List<FriendRequest> requests = query.getResultList();
        return requests;
    }

    @Override
    public User acceptFriend(String idFriendRequest) {
        Long idFR = Long.parseLong(idFriendRequest);
        EntityManager em = db.get();
        em.getTransaction().begin();
        FriendRequest friendRequest = em.find(FriendRequest.class, idFR);
        friendRequest.getOriginUser().getFriends().add(friendRequest.getTargetUser());
        friendRequest.getTargetUser().getFriends().add(friendRequest.getOriginUser());
        User userOrigin = em.find(User.class, friendRequest.getOriginUser().getId());
        userOrigin.setFriends(friendRequest.getOriginUser().getFriends());
        User userTarget = em.find(User.class, friendRequest.getTargetUser().getId());
        userTarget.setFriends(friendRequest.getTargetUser().getFriends());
        friendRequest.getTargetUser().getFriendRequests().remove(friendRequest);
        em.remove(friendRequest);
        em.getTransaction().commit();
        return userTarget;
    }

    @Override
    public User refuseFriend(String idFriendRequest) {
        Long idFR = Long.parseLong(idFriendRequest);
        EntityManager em = db.get();
        em.getTransaction().begin();
        FriendRequest friendRequest = em.find(FriendRequest.class, idFR);
        friendRequest.getTargetUser().getFriendRequests().remove(friendRequest);
        em.remove(friendRequest);
        em.getTransaction().commit();
        return friendRequest.getTargetUser();
    }

    @Override
    public User removeFriend(User user,String idFriend) {
        EntityManager em = db.get();
        em.getTransaction().begin();
        User friendx = em.find(User.class, Long.parseLong(idFriend));
        user.getFriends().remove(friendx);
        User userx = em.find(User.class, user.getId());
        userx.getFriends().remove(friendx);
        friendx.getFriends().remove(userx);
        em.getTransaction().commit();
        return userx;
    }
}
