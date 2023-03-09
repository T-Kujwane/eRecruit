/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.application;

/**
 *
 * @author My HP
 */
public class Applicant {
    
    private String applicantID;
    private String firstName;
    private String middleName;
    private String surname;
    private String cellNumber;
    private String email;
    private String preferredVacancy;

    public Applicant(String applicantID, String firstName, String middleName, String surname, String cellNumber, String email, String preferredVacancy) {
        this.applicantID = applicantID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.cellNumber = cellNumber;
        this.email = email;
        this.preferredVacancy = preferredVacancy;
    }

    public Applicant() {
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredVacancy() {
        return preferredVacancy;
    }

    public void setPreferredVacancy(String preferredVacancy) {
        this.preferredVacancy = preferredVacancy;
    }
    
    
}
