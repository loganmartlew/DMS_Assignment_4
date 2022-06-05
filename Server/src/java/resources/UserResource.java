/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.UserDAO;
import entities.User;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
