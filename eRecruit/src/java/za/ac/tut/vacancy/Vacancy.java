/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.vacancy;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.qualification.Qualification;

/**
 * This class models an object of vacancy
 * @author T Kujwane
 */
public class Vacancy implements Matchable{
    private final String referenceNr, recruiterEnterpriseNr, description;
    private final Date closingDate;
    private final List<Qualification> requiredQualifications;
    private final List<String> requiredSkills;
    private final Integer vacancyTypeId;

    public Vacancy(String referenceNr, String recruiterEnterpriseNr, String description, Date closingDate, List<Qualification> requiredQualifications, List<String> requiredSkills, Integer vacancyTypeId) {
        this.referenceNr = referenceNr;
        this.recruiterEnterpriseNr = recruiterEnterpriseNr;
        this.description = description;
        this.closingDate = closingDate;
        this.requiredQualifications = requiredQualifications;
        this.requiredSkills = requiredSkills;
        this.vacancyTypeId = vacancyTypeId;
    }
    
    public String getReferenceNr() {
        return referenceNr;
    }

    public String getRecruiterEnterpriseNr() {
        return recruiterEnterpriseNr;
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
    
    public void persist(DatabaseManager dbManager) throws SQLException{
        PreparedStatement ps = dbManager.getConnection().prepareStatement("INSERT INTO vacancy VALUES(?,?,?,?,?)");
        
        ps.setString(1, this.referenceNr);
        ps.setString(2, this.description);
        ps.setDate(3, this.closingDate);
        ps.setInt(4, this.vacancyTypeId);
        ps.setString(5, recruiterEnterpriseNr);
        
        ps.execute();
        
        for (String skill : this.requiredSkills){
            String insertSkillsQuery = "INSERT INTO required_skill(skill_id, vacancy_reference_nr) VALUES((SELECT skill_id FROM skill WHERE skill = \'" + skill + "\'),\'" + referenceNr + "\');";
            dbManager.executeUpdate(insertSkillsQuery);
        }
        
        for (Qualification requiredQuailification : this.requiredQualifications){
            String query = "INSERT INTO required_qualification(type_id, course_id, vacancy_reference_nr) VALUES((SELECT type_id FROM qualification_type WHERE type_name = \'" + requiredQuailification.getType() +"\'),"
                    + "(SELECT course_id FROM course WHERE course_name = \'" + requiredQuailification.getCourse() +"\'), \'" + referenceNr + "\');";
            dbManager.executeUpdate(query);
        }
    }
}
