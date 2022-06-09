/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mdb;

import dao.UserDAO;
import entities.Request;
import entities.User;
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
import messages.UserMessage;
import messages.UserMessageType;

/**
 *
 * @author Logan
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId",
            propertyValue = "jms/AddressBookUserQueue"),
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "jms/AddressBookUserQueue"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability",
            propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName",
            propertyValue = "jms/AddressBookUserQueue"),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")
})
public class UserMDB implements MessageListener {
    
    @Resource(mappedName = "jms/AddressBookConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    private Connection conn;
    private Session session;
    
    @EJB
    UserDAO userDao;
    
    @Override
    public void onMessage(Message message) {
        try {
            
            if (message instanceof ObjectMessage) {
                
                UserMessage userMessage = (UserMessage) ((ObjectMessage) message).getObject();
                UserMessageType type = userMessage.getType();
                
                if (type == UserMessageType.CREATE) {
                    UserMessage<String> typedMessage = userMessage;
                    
                    User user = userDao.getUserByName(typedMessage.getData());
                    
                    if (user == null) {
                        user = userDao.createUser(typedMessage.getData());
                    }
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject(user);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
                if (type == UserMessageType.GET_ALL) {
                    List<User> users = userDao.getAllUsers();
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject((Serializable) users);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
                if (type == UserMessageType.GET_BY_NAME) {
                    UserMessage<String> typedMessage = userMessage;
                    User user = userDao.getUserByName(typedMessage.getData());
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject(user);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
                if (type == UserMessageType.GET_INCOMING) {
                    UserMessage<String> typedMessage = userMessage;
                    User user = userDao.getUserByName(typedMessage.getData());
                    List<Request> requests = userDao.getUsersIncomingRequests(user.getId());
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject((Serializable) requests);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
                
                if (type == UserMessageType.GET_OUTGOING) {
                    UserMessage<String> typedMessage = userMessage;
                    User user = userDao.getUserByName(typedMessage.getData());
                    List<Request> requests = userDao.getUsersOutgoingRequests(user.getId());
                    
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    ObjectMessage replyMessage = session.createObjectMessage();
                    
                    replyMessage.setObject((Serializable) requests);
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    producer.send(replyMessage);
                    producer.close();
                    return;
                }
            }
            
        } catch (JMSException e) {
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
