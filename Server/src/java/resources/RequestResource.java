/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.RequestDAO;
import dto.RequestDTO;
import entities.Location;
import entities.Request;
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
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import messages.LocationMessage;
import messages.LocationMessageType;
import messages.RequestMessage;
import messages.RequestMessageType;

/**
 *
 * @author Logan
 */
@Named
@Path("/requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RequestResource {
    
    @Resource(mappedName = "jms/AddressBookConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/AddressBookRequestQueue")
    private Queue queue;
    
    private Connection conn;
    private Session session;
    
    @EJB
    RequestDAO requestDao;
    
    @GET
    public String getAllRequests() {
        JsonArrayBuilder array = Json.createArrayBuilder();
        
        try {
            RequestMessage messageData = 
                new RequestMessage(RequestMessageType.GET_ALL, "");

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
    public String createRequest(String body) {
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonStructure bodyJson = jsonReader.read();
        
        String locationId = ((JsonString) bodyJson.getValue("/locationId")).getString();
        String fromUserName = ((JsonString) bodyJson.getValue("/fromUserName")).getString();
        String toUserName = ((JsonString) bodyJson.getValue("/toUserName")).getString();
        
        RequestDTO dto = new RequestDTO();
        dto.setLocationId(locationId);
        dto.setFromUserName(fromUserName);
        dto.setToUserName(toUserName);
        
        try {
            RequestMessage<RequestDTO> messageData = 
                new RequestMessage<RequestDTO>(RequestMessageType.CREATE, dto);

            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();
            
            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);
            
            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            Request request = (Request) replyMessage.getObject();
            
            return this.buildToString(request.toJson());
        } catch (JMSException e) {
            return "Message Not Sent: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
    }
    
    @PATCH
    @Path("{id}/accept")
    public String acceptRequest(@PathParam("id") String id) {
        try {
            RequestMessage<String> messageData = 
                new RequestMessage<String>(RequestMessageType.ACCEPT, id);

            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);
            
            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            Request request = (Request) replyMessage.getObject();
            
            return this.buildToString(request.toJson());
        } catch (JMSException e) {
            return "Message Not Sent: " + e.getMessage();
        } catch (NullPointerException e) {
            return "Reply Message Not Recieved";
        }
    }
    
    @PATCH
    @Path("{id}/deny")
    public String denyRequest(@PathParam("id") String id) {
        try {
            RequestMessage<String> messageData = 
                new RequestMessage<String>(RequestMessageType.DENY, id);

           MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(tempQueue);

            message.setObject(messageData);
            message.setJMSReplyTo(tempQueue);
            producer.send(message);
            
            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
            Request request = (Request) replyMessage.getObject();
            
            return this.buildToString(request.toJson());
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
