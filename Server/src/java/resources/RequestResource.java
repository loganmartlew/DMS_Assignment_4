/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.RequestDAO;
import dto.RequestDTO;
import entities.Request;
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
@Path("/requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RequestResource {
    
    @EJB
    RequestDAO requestDao;
    
    @GET
    public String getAllRequests() {
        JsonArrayBuilder array = Json.createArrayBuilder();
        List<Request> requests = requestDao.getAllRequests();
        
        for(Request request : requests) {
            array.add(request.toJson());
        }
        
        return this.buildToString(array.build());
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
        
        Request newRequest = requestDao.createRequest(dto);
        return this.buildToString(newRequest.toJson());
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
