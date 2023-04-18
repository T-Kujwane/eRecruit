/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.applicant;

import java.sql.SQLException;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.interfaces.Matcher;

/**
 *
 * @author T Kujwane
 */
public class ApplicantHandler implements Matcher{
    
    private final DatabaseManager dbManager;
    
    @EJB
    private EmailSessionBean emailSessionBean;
    
    public ApplicantHandler() throws ClassNotFoundException, SQLException {
        this.dbManager = new DatabaseManager();
    }
    
    
    @Override
    public void match(Matchable entity) throws SQLException, ClassNotFoundException, MessagingException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public synchronized void notifyApplicant(Applicant applicant, String subject, String message) throws MessagingException{
        emailSessionBean.sendEmail(applicant.getEmailAddress().trim(), subject, message);
    }
}
