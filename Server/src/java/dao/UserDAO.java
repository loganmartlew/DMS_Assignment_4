/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entities.Location;
import entities.Request;
import entities.User;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonStructure;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.io.StringWriter;
import java.io.Writer;
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
        Query userQuery = em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class);
        userQuery.setParameter("name", name);
        List<User> users = userQuery.getResultList();
        
        if (users.size() < 1) {
            return null;
        }
        
        User user = em.find(User.class, users.get(0).getId());
        
        Query locationQuery = em.createQuery("SELECT l FROM Location l WHERE l.user.id = :id", Location.class);
        locationQuery.setParameter("id", user.getId());
        List<Location> locations = locationQuery.getResultList();
        
        user.setLocations(locations);
        
        return user;
    }
    
    public List<Request> getUsersIncomingRequests(int id) {
        User user = em.find(User.class, id);
        return user.getIncomingRequests();
    }
    
    public List<Request> getUsersOutgoingRequests(int id) {
        User user = em.find(User.class, id);
        return user.getOutgoingRequests();
    }
    
     private String buildToString(JsonStructure json) {
        String jsonString;
        
        try(Writer writer = new StringWriter()) {
            Json.createWriter(writer).write(json);
            jsonString = writer.toString();
            return jsonString;
        } catch(Exception e) {
            return "";
        }
    }
}
