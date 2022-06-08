/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.LocationDAO;
import dto.LocationDTO;
import entities.Location;
import entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
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
import jakarta.ws.rs.core.MediaType;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import messages.LocationMessage;
import messages.LocationMessageType;

/**
 *
 * @author Logan
 */
@Named
@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {
    
    @Resource(mappedName = "jms/AddressBookConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(mappedName = "jms/AddressBookLocationQueue")
    private Queue queue;
    
    private Connection conn;
    private Session session;
    
    @EJB
    LocationDAO locationDao;
    
    @GET
    public String getAllLocations() {
        JsonArrayBuilder array = Json.createArrayBuilder();
        List<Location> locations = locationDao.getAllLocations();
        
        for(Location location : locations) {
            array.add(location.toJson(false));
        }
        
        return this.buildToString(array.build());
    }
    
    @Path("{id}")
    @GET
    public String getLocationById(@PathParam("id") String id) {
//        try {
//            LocationMessage<Integer> messageData = 
//                new LocationMessage<Integer>(LocationMessageType.GET_BY_ID, Integer.parseInt(id));
//
//            MessageProducer producer = session.createProducer(queue);
//            ObjectMessage message = session.createObjectMessage();
//
//            TemporaryQueue tempQueue = session.createTemporaryQueue();
//            MessageConsumer consumer = session.createConsumer(tempQueue);
////            consumer.setMessageListener(new Listener());
//
//            message.setObject(messageData);
//            message.setJMSReplyTo(tempQueue);
//            producer.send(message);
//
//            ObjectMessage replyMessage = (ObjectMessage) consumer.receive(5000);
//            Location location = (Location) replyMessage.getObject();

            Location location = locationDao.getLocationById(Integer.parseInt(id));

            if (location == null) {
                return "null";
            }

            return this.buildToString(location.toJson(true));
//        } catch (JMSException e) {
//            return "Messaging Error: " + e.getMessage();
//        } catch (NullPointerException e) {
//            return "Reply Message Not Recieved";
//        }
    }
    
    @POST
    public String createLocation(String body) {
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonStructure bodyJson = jsonReader.read();
        
        String name = ((JsonString) bodyJson.getValue("/name")).getString();
        String address = ((JsonString) bodyJson.getValue("/address")).getString();
        String latitude = ((JsonString) bodyJson.getValue("/latitude")).getString();
        String longitude = ((JsonString) bodyJson.getValue("/longitude")).getString();
        String userName = ((JsonString) bodyJson.getValue("/userName")).getString();
        
        LocationDTO dto = new LocationDTO();
        dto.setName(name);
        dto.setAddress(address);
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        dto.setUserName(userName);

        try {
            LocationMessage<LocationDTO> messageData = 
                new LocationMessage<LocationDTO>(LocationMessageType.CREATE, dto);

            MessageProducer producer = session.createProducer(queue);
            ObjectMessage message = session.createObjectMessage();

            message.setObject(messageData);
            producer.send(message);
            
            return "Message Sent";
        } catch (JMSException e) {
            return "Message Not Sent: " + e.getMessage();
        }
    }
    
    private class Listener implements MessageListener {
        @Override
        public void onMessage(Message msg) {
            System.out.println("Message Recieved");
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
