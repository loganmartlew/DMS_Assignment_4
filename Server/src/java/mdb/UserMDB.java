/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mdb;

import dao.UserDAO;
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
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
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
                    userDao.createUser(typedMessage.getData());
                }
                
            }
            
        } catch (JMSException e) {
        }
    }
    
    @PostConstruct
    public void setupConnection() {
        try {
            conn = connectionFactory.createConnection();
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
