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
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;
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
        List<User> users = userDao.getAllUsers();
        
        for(User user : users) {
            array.add(user.toJson(false));
        }
        
        return this.buildToString(array.build());
    }
    
    @Path("{name}")
    @GET
    public Response getUserByName(@PathParam("name") String name) {
        User user = userDao.getUserByName(name);
        
        if (user == null) {
            return Response.ok("null").build();
        }
        
        String body = this.buildToString(user.toJson(true));
        
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        
        ResponseBuilder builder = Response.ok(body);
        builder.cacheControl(cc);
        return builder.build();
    }
    
    @Path("{name}/incoming")
    @GET
    public String getIncomingRequests(@PathParam("name") String name) {
        User user = userDao.getUserByName(name);
        
        JsonArrayBuilder array = Json.createArrayBuilder();
        List<Request> requests = userDao.getUsersIncomingRequests(user.getId());
        
        for(Request request : requests) {
            array.add(request.toJson());
        }
        
        return this.buildToString(array.build());
    }
    
    @Path("{name}/outgoing")
    @GET
    public String getOutgoingRequests(@PathParam("name") String name) {
        User user = userDao.getUserByName(name);
        
        JsonArrayBuilder array = Json.createArrayBuilder();
        List<Request> requests = userDao.getUsersOutgoingRequests(user.getId());
        
        for(Request request : requests) {
            array.add(request.toJson());
        }
        
        return this.buildToString(array.build());
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

            message.setObject(messageData);
            producer.send(message);
            
            return "Message Sent";
        } catch (JMSException e) {
            return "Message Not Sent: " + e.getMessage();
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
