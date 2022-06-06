/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import dao.LocationDAO;
import dto.LocationDTO;
import entities.Location;
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
@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {
    
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
        Location location = locationDao.getLocationById(Integer.parseInt(id));
        
        if (location == null) {
            return "null";
        }
        
        return this.buildToString(location.toJson(true));
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
        
        Location newLocation = locationDao.createLocation(dto);
        return this.buildToString(newLocation.toJson(true));
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
