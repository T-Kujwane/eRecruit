/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.SQLException;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;

/**
 *
 * @author T Kujwane
 */
public class NotificationHandler extends Handler{
    @EJB
    private EmailSessionBean emailSessionBean;

    public NotificationHandler(DatabaseManager dbManager, EmailSessionBean emailSessionBean) throws ClassNotFoundException, SQLException {
        super(dbManager);
        this.emailSessionBean = emailSessionBean;
    }
    
    protected synchronized void notify(String recipientEmail, String subject, String message) throws MessagingException {
        emailSessionBean.sendEmail(recipientEmail, subject, message);
    }
}
