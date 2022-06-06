/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.UserDAO;
import entities.Request;
import entities.User;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
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

/**
 *
 * @author Logan
 */
@Named
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
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
    public String getUserByName(@PathParam("name") String name) {
        User user = userDao.getUserByName(name);
        
        if (user == null) {
            return "null";
        }
        
        return this.buildToString(user.toJson(true));
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
        
        User existingUser = userDao.getUserByName(name);
        
        if (existingUser == null) {
            User newUser = userDao.createUser(name);
            return this.buildToString(newUser.toJson(true));
        }
        
        return this.buildToString(existingUser.toJson(true));
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
