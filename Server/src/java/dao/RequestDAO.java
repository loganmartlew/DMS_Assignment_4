/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.RequestDTO;
import entities.Location;
import entities.Request;
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
public class RequestDAO {

    @PersistenceContext
    private EntityManager em;

    @EJB
    LocationDAO locationDao;

    @EJB
    UserDAO userDao;

    public Request createRequest(RequestDTO dto) {
        Request newRequest = new Request();

        Location location = locationDao.getLocationById(dto.getLocationId());
        newRequest.setLocation(location);

        User fromUser = userDao.getUserByName(dto.getFromUserName());
        newRequest.setFromUser(fromUser);

        User toUser = userDao.getUserByName(dto.getToUserName());
        newRequest.setToUser(toUser);

        em.persist(newRequest);
        return newRequest;
    }
    
    public List<Request> getAllRequests() {
        Query query = em.createQuery("SELECT r FROM Request r", Request.class);
        List<Request> requests = query.getResultList();
        return requests;
    }
}
