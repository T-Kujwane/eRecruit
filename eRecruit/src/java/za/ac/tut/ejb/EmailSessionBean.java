/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package za.ac.tut.ejb;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author T Kujwane
 */
@Stateless
@LocalBean
public class EmailSessionBean {
    
    private final int port = 587;
    private final String host = "smtp-mail.outlook.com";
    private final String sender = "218719473@tut4life.ac.za";
    private final boolean mustAuthenticate = true;
    private final String username = "218719473@tut4life.ac.za";
    private final String password = "218053767@Uj";
    private final String protocol = "SMTP";
    public void sendEmail(String to, String subject, String body) {
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
