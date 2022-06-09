/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package entities;

/**
 *
 * @author Logan
 */
public enum RequestStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");
    
    private final String text;
    
    RequestStatus(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }
}
