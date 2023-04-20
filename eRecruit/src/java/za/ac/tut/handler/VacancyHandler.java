/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.ApplicantFields;
import za.ac.tut.enums.VacancyTypeFields;
import za.ac.tut.exception.VacancyExistsException;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.interfaces.Matcher;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class VacancyHandler extends Handler implements Matcher {

    public VacancyHandler(DatabaseManager dbManager, EmailSessionBean emailBean) throws ClassNotFoundException, SQLException {
        super(dbManager, emailBean);
    }
    
    private List getMatchedApplicantIDList(String vacancyRef) throws SQLException {
        List applicantIDsList = new ArrayList<>();

        ResultSet results = executeQuery("SELECT applicant_id FROM qualifying_applicant WHERE vacancy_reference_nr = \'" + vacancyRef + "\';");

        if (getDatabaseManager().hasData(results)) {
            while (results.next()) {
                applicantIDsList.add(getDatabaseManager().getData(ApplicantFields.APPLICANT_ID, results));
            }
        }

        return applicantIDsList;
    }

    @Override
    public List match(Matchable entity) throws SQLException, ClassNotFoundException {
        if ((!(entity instanceof Vacancy)) && (entity != null)) {
            throw new IllegalArgumentException("Parsed entity is of an unexpected type. Parsed an object of " + entity.getClass().getSimpleName() + " when expected an object of Vacancy");
        }

        Vacancy vacancy = (Vacancy) entity;

        List<Applicant> matchingApplicantsList = new ArrayList<>();

        List<Applicant> applicantsList = getAllApplicants();

        String vacancyType = getVacancyType(vacancy.getVacancyTypeId());

        List<String> matchedApplicantIDs = getMatchedApplicantIDList(vacancy.getReferenceNr());

        for (Applicant applicant : applicantsList) {
            if (!matchedApplicantIDs.contains(applicant.getApplicantID())) {
                boolean isInterestedInVacancyType = false;

                for (String applicantVacancyType : applicant.getPreferredVacancyTypes()) {
                    if (applicantVacancyType.equalsIgnoreCase(vacancyType)) {
                        isInterestedInVacancyType = true;
                        break;
                    }
                }

                boolean hasRequiredQualification = false;

                for (Qualification applicantQualification : applicant.getApplicantQualifications()) {
                    for (Qualification vacancyQualification : vacancy.getRequiredQualifications()) {
                        if (applicantQualification.equals(vacancyQualification)) {
                            hasRequiredQualification = true;
                            break;
                        }
                    }
                }

                boolean hasRequiredSkills = false;

                int possessedRequiredSkillsCount = 0;

                for (String applicantSkill : applicant.getSkills()) {
                    for (String vacancySkill : vacancy.getRequiredSkills()) {
                        if (applicantSkill.equalsIgnoreCase(vacancySkill)) {
                            possessedRequiredSkillsCount++;
                        }
                    }
                }

                if (possessedRequiredSkillsCount >= vacancy.getRequiredSkills().size()) {
                    hasRequiredSkills = true;
                }

                if (hasRequiredQualification && isInterestedInVacancyType && hasRequiredSkills) {
                    insertQualifyingApplicant(applicant, vacancy);

                    matchingApplicantsList.add(applicant);
                }
            }
        }
        return matchingApplicantsList;
    }

    public void addVacancy(Vacancy newVacancy) throws SQLException, VacancyExistsException {
        String refNr = newVacancy.getReferenceNr();

        ResultSet rs = getDatabaseManager().executeQuery("SELECT * FROM vacancy WHERE reference_nr = \'" + refNr + "\';");

        if (getDatabaseManager().hasData(rs)) {
            throw new VacancyExistsException("A vacancy with the referrence number " + refNr + " already exists");
        }

        PreparedStatement ps = getDatabaseManager().prepareStatement("INSERT INTO vacancy VALUES(?,?,?,?,?)");

        ps.setString(1, newVacancy.getReferenceNr());
        ps.setString(2, newVacancy.getDescription());
        ps.setDate(3, newVacancy.getClosingDate());
        ps.setInt(4, newVacancy.getVacancyTypeId());
        ps.setString(5, newVacancy.getPostingRecruiter().getEnterpriseNumber());

        ps.execute();

        for (String skill : newVacancy.getRequiredSkills()) {
            String insertSkillsQuery = "INSERT INTO required_skill(skill_id, vacancy_reference_nr) VALUES((SELECT skill_id FROM skill WHERE skill = \'" + skill + "\'),\'" + refNr + "\');";
            getDatabaseManager().executeUpdate(insertSkillsQuery);
        }

        for (Qualification requiredQuailification : newVacancy.getRequiredQualifications()) {
            String query = "INSERT INTO required_qualification(type_id, course_id, vacancy_reference_nr) VALUES((SELECT type_id FROM qualification_type WHERE type_name = \'" + requiredQuailification.getType() + "\'),"
                    + "(SELECT course_id FROM course WHERE course_name = \'" + requiredQuailification.getCourse() + "\'), \'" + refNr + "\');";
            executeUpdate(query);
        }
    }

    public List getVacancyTypes() throws SQLException {
        List<String> vacancyTypesList = new ArrayList<>();

        String query = "SELECT " + VacancyTypeFields.VACANCY_TYPE.name() + " FROM vacancy_type;";

        ResultSet results = getDatabaseManager().executeQuery(query);

        if (getDatabaseManager().hasData(results)) {
            while (results.next()) {
                vacancyTypesList.add(getDatabaseManager().getData(VacancyTypeFields.VACANCY_TYPE, results));
            }
        }

        return vacancyTypesList;
    }

    
}
