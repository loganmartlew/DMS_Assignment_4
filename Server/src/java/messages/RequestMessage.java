/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package messages;

import java.io.Serializable;

/**
 *
 * @author Logan
 */
public class RequestMessage<T> implements Serializable {
    
    private T data;
    private RequestMessageType type;
    
    public RequestMessage(RequestMessageType type, T data) {
        this.data = data;
        this.type = type;
    }
    
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RequestMessageType getType() {
        return type;
    }

    public void setType(RequestMessageType type) {
        this.type = type;
    }
}
