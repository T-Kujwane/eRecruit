/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.application;

import java.util.ArrayList;
import java.util.List;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.qualification.Qualification;

/**
 *
 * @author My HP
 */
public class Applicant implements Matchable{
    
    private final String applicantID;
    private final String firstName;
    private final String middleName;
    private final String surname;
    private final String phoneNr;
    private final String emailAddress;
    private final List<String> preferredVacancyTypes;
    private final List<Qualification> applicantQualifications;
    private final List<String> skills;

    public Applicant(String applicantID, String firstName, String middleName, String surname, String phoneNr, String emailAddress, 
            List<String> preferredVacancyTypes, List<Qualification> applicantQualifications, List<String> skills) 
    {
        this.applicantID = applicantID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.phoneNr = phoneNr;
        this.emailAddress = emailAddress;
        this.preferredVacancyTypes = preferredVacancyTypes;
        this.applicantQualifications = applicantQualifications;
        this.skills = skills;
    }

    public Applicant(String applicantID, String firstName, String middleName, String surname, String phoneNr, String emailAddress) {
        this.applicantID = applicantID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.phoneNr = phoneNr;
        this.emailAddress = emailAddress;
        this.preferredVacancyTypes = new ArrayList<>();
        this.applicantQualifications = new ArrayList<>();
        this.skills = new ArrayList<>();
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
    
    public List<String> getPreferredVacancyTypes() {
        return preferredVacancyTypes;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public List<Qualification> getApplicantQualifications() {
        return applicantQualifications;
    }

    public List<String> getSkills() {
        return skills;
    }
    
    public void addPreferedVacancy(String vacancyType){
        this.preferredVacancyTypes.add(vacancyType);
    }
    
    public void addQualification(Qualification applicantQualification){
        this.applicantQualifications.add(applicantQualification);
    }
    
    public void addSkill(String newSkill){
        this.skills.add(newSkill);
    }

    @Override
    public String toString() {
        return "Applicant{" + "applicantID=" + applicantID + ", firstName=" + firstName + ", middleName=" + middleName + ", surname=" + surname + ", phoneNr=" + phoneNr + ", emailAddress=" + emailAddress + ", preferredVacancyTypes=" + preferredVacancyTypes + ", applicantQualifications=" + applicantQualifications + ", skills=" + skills + '}';
    }
    
    
}
