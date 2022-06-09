package messages;


import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Logan
 */
public class UserMessage<T> implements Serializable {
    
    private T data;
    private UserMessageType type;
    
    public UserMessage(UserMessageType type, T data) {
        this.data = data;
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public UserMessageType getType() {
        return type;
    }

    public void setType(UserMessageType type) {
        this.type = type;
    }
}
