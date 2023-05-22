/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.user;

import java.io.Serializable;

/**
 *
 * @author T Kujwane
 */
public class User implements Serializable{
    private final String emailAddress, userType;

    public User(String emailAddress, String userType) {
        this.emailAddress = emailAddress;
        this.userType = userType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUserType() {
        return userType;
    }
    
}
