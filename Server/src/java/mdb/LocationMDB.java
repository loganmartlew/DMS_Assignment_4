/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mdb;

import dao.LocationDAO;
import dto.LocationDTO;
import entities.Location;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
import java.io.Serializable;
import java.util.List;
import messages.LocationMessage;
import messages.LocationMessageType;

/**
 *
 * @author Logan
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId",
            propertyValue = "jms/AddressBookLocationQueue"),
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "jms/AddressBookLocationQueue"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability",
            propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName",
            propertyValue = "jms/AddressBookLocationQueue"),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")
})
public class LocationMDB implements MessageListener {
    
    @Resource(mappedName = "jms/AddressBookConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    private Connection conn;
    private Session session;
    
    @EJB
    LocationDAO locationDao;

    @Override
    public void onMessage(Message message) {
        try {
            
            if (message instanceof ObjectMessage) {
                
                LocationMessage locationMessage = (LocationMessage) ((ObjectMessage) message).getObject();
                LocationMessageType type = locationMessage.getType();
                
                if (type == LocationMessageType.CREATE) {
                    LocationMessage<LocationDTO> typedMessage = locationMessage;
                    Location location = locationDao.createLocation(typedMessage.getData());
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject(location);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
                if (type == LocationMessageType.GET_ALL) {
                    List<Location> locations = locationDao.getAllLocations();
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject((Serializable) locations);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
                if (type == LocationMessageType.GET_BY_ID) {
                    LocationMessage<Integer> typedMessage = locationMessage;
                    int id = typedMessage.getData();
                    
                    Location location = locationDao.getLocationById(id);
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject(location);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
            }
            
        } catch (JMSException e) {
            System.out.println("LocationMDB Messaging Error: " + e.getMessage());
        }
    }
    
    @PostConstruct
    public void setupConnection() {
        try {
            conn = connectionFactory.createConnection();
            conn.start();
            session = conn.createSession();
        } catch (JMSException e) {}
    }
    
    @PreDestroy
    public void destroyConnection() {
        try {
            if (session != null) session.close();
            if (conn != null) conn.close();
        } catch (JMSException e) {}
    }
}
