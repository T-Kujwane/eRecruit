/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.vacancy;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.recruiter.Recruiter;

/**
 * This class models an object of vacancy
 * @author T Kujwane
 */
public class Vacancy implements Matchable{
    private final String referenceNr, description;
    private final Date closingDate;
    private final List<Qualification> requiredQualifications;
    private final List<String> requiredSkills;
    private final Integer vacancyTypeId;
    
    private final Recruiter postingRecruiter;
    
    public Vacancy(String referenceNr, String description, Date closingDate, List<Qualification> requiredQualifications, 
            List<String> requiredSkills, Integer vacancyTypeId, Recruiter postingRecruiter) 
    {
        this.referenceNr = referenceNr;
        this.description = description;
        this.closingDate = closingDate;
        this.requiredQualifications = requiredQualifications;
        this.requiredSkills = requiredSkills;
        this.vacancyTypeId = vacancyTypeId;
        this.postingRecruiter = postingRecruiter;
    }

    public Vacancy(String referenceNr, String description, Date closingDate, Integer vacancyTypeId, Recruiter postingRecruiter) {
        this.referenceNr = referenceNr;
        this.description = description;
        this.closingDate = closingDate;
        this.vacancyTypeId = vacancyTypeId;
        this.postingRecruiter = postingRecruiter;
        this.requiredQualifications = new ArrayList<>();
        this.requiredSkills = new ArrayList<>();
    }
    
    public String getReferenceNr() {
        return referenceNr;
    }

    public String getDescription() {
        return description;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public List<Qualification> getRequiredQualifications() {
        return requiredQualifications;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public Integer getVacancyTypeId() {
        return vacancyTypeId;
    }

    public Recruiter getPostingRecruiter() {
        return postingRecruiter;
    }
    
    public void addRequiredQualification(Qualification newRequiredQualification){
        this.requiredQualifications.add(newRequiredQualification);
    }
    
    public void addSkill(String newSkill){
        this.requiredSkills.add(newSkill);
    }
}
