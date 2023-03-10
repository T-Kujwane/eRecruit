/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.application;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author My HP
 */
public class Applicant {
    
    private final String applicantID;
    private final String firstName;
    private final String middleName;
    private final String surname;
    private final String phoneNr;
    private final String emailAddress;
    private final List<String> preferredVacancies;

    public Applicant(String applicantID, String firstName, String middleName, String surname, String phoneNr, String emailAddress, List<String> preferredVacancies) {
        this.applicantID = applicantID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.phoneNr = phoneNr;
        this.emailAddress = emailAddress;
        this.preferredVacancies = preferredVacancies;
    }

    public Applicant(String applicantID, String firstName, String middleName, String surname, String phoneNr, String emailAddress) {
        this.applicantID = applicantID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.phoneNr = phoneNr;
        this.emailAddress = emailAddress;
        this.preferredVacancies = new ArrayList<>();
    }
    
    public String getApplicantID() {
        return applicantID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }
    
    public String getPhoneNumber() {
        return phoneNr;
    }

    public String getEmail() {
        return emailAddress;
    }

    public List<String> getPreferredVacancies() {
        return preferredVacancies;
    }

    public void addPreferedVacancy(String vacancy_type){
        this.preferredVacancies.add(vacancy_type);
    }
}
