/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.persistence.Column;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author Logan
 */
@Entity
@Table(name = "tkj2567_addressbook_users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(unique=true)
    private String name;
    
    @OneToMany(mappedBy="user")
    private List<Location> locations;
    
    @OneToMany(mappedBy="fromUser")
    private List<Request> outgoingRequests;
    
    @OneToMany(mappedBy="toUser")
    private List<Request> incomingRequests;
    
    public JsonObject toJson(boolean withLocations) {
        JsonObjectBuilder out = Json.createObjectBuilder();
        out.add("id", this.id);
        out.add("name", this.name);
        
        if (withLocations) {
            JsonArrayBuilder locations = Json.createArrayBuilder();
            for (Location location : this.locations) {
                locations.add(location.toJson(false));
            }
            out.add("locations", locations);
        }
        
        return out.build();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Request> getOutgoingRequests() {
        return outgoingRequests;
    }

    public void setOutgoingRequests(List<Request> outgoingRequests) {
        this.outgoingRequests = outgoingRequests;
    }

    public List<Request> getIncomingRequests() {
        return incomingRequests;
    }

    public void setIncomingRequests(List<Request> incomingRequests) {
        this.incomingRequests = incomingRequests;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ id=" + id + " ]";
    }
    
}
