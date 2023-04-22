/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.ac.tut.interfaces;

import java.sql.SQLException;
import java.util.List;
import javax.mail.MessagingException;

/**
 * This interface must be implemented by an object that is able to match applicants to vacancies or vice versa
 * @author T Kujwane
 */
public interface Matcher {
    List match(Matchable entity) throws SQLException, ClassNotFoundException;
}
