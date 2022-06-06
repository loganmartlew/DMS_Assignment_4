/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.LocationDTO;
import entities.Location;
import entities.User;
import jakarta.ejb.EJB;
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
public class LocationDAO {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    UserDAO userDao;
    
    public Location createLocation(LocationDTO dto) {
        Location newLocation = new Location();
        newLocation.setName(dto.getName());
        newLocation.setAddress(dto.getAddress());
        newLocation.setLatitude(dto.getLatitude());
        newLocation.setLongitude(dto.getLongitude());
        
        User user = userDao.getUserByName(dto.getUserName());
        newLocation.setUser(user);
        
        em.persist(newLocation);
        return newLocation;
    }
    
    public List<Location> getAllLocations() {
        Query query = em.createQuery("SELECT l FROM Location l", Location.class);
        List<Location> locations = query.getResultList();
        return locations;
    }
    
    public Location getLocationById(int id) {
        Location location = em.find(Location.class, id);
        return location;
    }
}
