/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package messages;

import dto.LocationDTO;
import java.io.Serializable;
import java.util.Optional;

/**
 *
 * @author Logan
 */
public class LocationMessage<T> implements Serializable {
    private T data;
    private LocationMessageType type;
    
    public LocationMessage(LocationMessageType type, T data) {
        this.data = data;
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocationMessageType getType() {
        return type;
    }

    public void setType(LocationMessageType type) {
        this.type = type;
    }
}
