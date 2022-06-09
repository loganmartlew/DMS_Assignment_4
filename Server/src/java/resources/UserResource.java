/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.UserDAO;
import entities.Request;
import entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TemporaryQueue;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import messages.UserMessage;
import messages.UserMessageType;

/**
 *
 * @author Logan
 */
@Named
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Resource(mappedName = "jms/AddressBookConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/AddressBookUserQueue")
    private Queue queue;
    
    private Connection conn;
    private Session session;
    
    @EJB
    private UserDAO userDao;
    
    @GET
    public String getAllUsers() {
        JsonArrayBuilder array = Json.createArrayBuilder();
        
         try {
            UserMessage messageData = 
                new UserMessage(UserMessageType.GET_ALL, "");

            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);

            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            List<User> users = (List<User>) replyMessage.getObject();
            
            for(User user : users) {
                array.add(user.toJson(false));
            }

            return this.buildToString(array.build());
        } catch (JMSException e) {
            return "Messaging Error: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
    }
    
    @Path("{name}")
    @GET
    public String getUserByName(@PathParam("name") String name) {
        try {
            UserMessage<String> messageData = 
                new UserMessage<String>(UserMessageType.GET_BY_NAME, name);
            
            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);

            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            User user = (User) replyMessage.getObject();
            
            return this.buildToString(user.toJson(true));
        } catch (JMSException e) {
            return "Messaging Error: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
    }
    
    @Path("{name}/incoming")
    @GET
    public String getIncomingRequests(@PathParam("name") String name) {
        JsonArrayBuilder array = Json.createArrayBuilder();
        
        try {
            UserMessage<String> messageData = 
                new UserMessage<String>(UserMessageType.GET_INCOMING, name);
            
            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);

            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            List<Request> requests = (List<Request>) replyMessage.getObject();
            
            for(Request request : requests) {
                array.add(request.toJson());
            }
            
            return this.buildToString(array.build());
        } catch (JMSException e) {
            return "Messaging Error: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
    }
    
    @Path("{name}/outgoing")
    @GET
    public String getOutgoingRequests(@PathParam("name") String name) {
        JsonArrayBuilder array = Json.createArrayBuilder();
        
        try {
            UserMessage<String> messageData = 
                new UserMessage<String>(UserMessageType.GET_OUTGOING, name);
            
            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);

            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            List<Request> requests = (List<Request>) replyMessage.getObject();
            
            for(Request request : requests) {
                array.add(request.toJson());
            }
            
            return this.buildToString(array.build());
        } catch (JMSException e) {
            return "Messaging Error: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
    }
    
    @POST
    public String createUser(String body) {
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonStructure bodyJson = jsonReader.read();
        
        String name = ((JsonString) bodyJson.getValue("/name")).getString();
        
        try {
            UserMessage<String> messageData = 
                new UserMessage<String>(UserMessageType.CREATE, name);

            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);

            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            User user = (User) replyMessage.getObject();
            
            return this.buildToString(user.toJson(true));
        } catch (JMSException e) {
            return "Message Not Sent: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
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
