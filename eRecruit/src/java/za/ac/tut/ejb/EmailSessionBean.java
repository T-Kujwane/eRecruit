/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package za.ac.tut.ejb;

import java.util.Properties;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author T Kujwane
 */
@Stateless
@LocalBean
public class EmailSessionBean {

    private final int port;
    private final String host;
    private final String sender;
    private final boolean mustAuthenticate;
    private final String username;
    private final String password;
    private final String protocol;
    private final boolean debug;

    public EmailSessionBean() {
        port = 587;
        host = "smtp-mail.outlook.com";
        sender = "developer.tk_kujwane@outlook.com";
        mustAuthenticate = true;
        username = this.sender;
        password = "outlook.getPassword()";
        protocol = "SMTP";
        debug = true;
    }
    
    public synchronized void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.ssl.enable", false);
        props.put("mail.smtp.socketFactory.fallback", true);
        Authenticator authenticator = null;
        
        if (mustAuthenticate) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }
        
        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sender));
        InternetAddress[] address = {new InternetAddress(to)};
        message.setRecipients(Message.RecipientType.TO, address);
        message.setSubject(subject);
        message.setSentDate(new java.util.Date());
        message.setText(body);
        Transport.send(message);
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
