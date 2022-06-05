/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entities.Request;
import entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

/**
 *
 * @author Logan
 */
@Stateless
public class UserDAO {
    
    @PersistenceContext
    private EntityManager em;
    
    public User createUser(String name) {
        User newUser = new User();
        newUser.setName(name);
        em.persist(newUser);
        return newUser;
    }
    
    public List<User> getAllUsers() {
        Query query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        return users;
    }
    
    public User getUserById(int id) {
        User user = em.find(User.class, id);
        return user;
    }
    
    public User getUserByName(String name) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class);
        query.setParameter("name", name);
        List<User> users = query.getResultList();
        
        if (users.size() < 1) {
            return null;
        }
        
        return users.get(0);
    }
    
    public List<Request> getUsersIncomingRequests(int id) {
        User user = em.find(User.class, id);
        return user.getIncomingRequests();
    }
    
    public List<Request> getUsersOutgoingRequests(int id) {
        User user = em.find(User.class, id);
        return user.getOutgoingRequests();
    }
}
