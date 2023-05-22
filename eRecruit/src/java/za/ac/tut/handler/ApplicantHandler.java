/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.QualifyingApplicantFields;
import za.ac.tut.enums.VacancyTypeFields;
import za.ac.tut.exception.ApplicantExistsException;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.interfaces.Matcher;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class ApplicantHandler extends NotificationHandler implements Matcher {

    public ApplicantHandler(DatabaseManager dbManager, EmailSessionBean emailBean) throws ClassNotFoundException, SQLException {
        super(dbManager, emailBean);
    }

    public ApplicantHandler(DatabaseManager dbManager) throws ClassNotFoundException, SQLException {
        super(dbManager, new EmailSessionBean());
    }

    public void addApplicant(Applicant newApplicant) throws SQLException, ApplicantExistsException {
        ResultSet rs = getDatabaseManager().executeQuery("SELECT first_name FROM applicant WHERE applicant_id = \'" + newApplicant.getApplicantID() + "\';");

        if (!getDatabaseManager().hasData(rs)) {
            PreparedStatement ps = getDatabaseManager().prepareStatement("INSERT INTO applicant(applicant_id, first_name, middle_name, surname, email_address, phone_nr) VALUES(?,?,?,?,?,?);");

            ps.setString(1, newApplicant.getApplicantID());
            ps.setString(2, newApplicant.getFirstName());
            ps.setString(3, newApplicant.getMiddleName());
            ps.setString(4, newApplicant.getSurname());
            ps.setString(5, newApplicant.getEmailAddress());
            ps.setString(6, newApplicant.getPhoneNumber());

            ps.execute();

            for (String skill : newApplicant.getSkills()) {
                getDatabaseManager().executeUpdate("INSERT INTO applicant_skill(skill_id, applicant_id) VALUES("
                        + "(SELECT skill_id FROM skill WHERE skill = \'" + skill + "\'), \'" + newApplicant.getApplicantID() + "\');");
            }

            for (String preferedVacancyType : newApplicant.getPreferredVacancyTypes()) {
                getDatabaseManager().executeUpdate("INSERT INTO prefered_vacancy_type (vacancy_type_id, applicant_id) VALUES("
                        + "(SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = \'" + preferedVacancyType + "\'), \'" + newApplicant.getApplicantID() + "\');");
            }

            for (Qualification applicantQualification : newApplicant.getApplicantQualifications()) {
                getDatabaseManager().executeUpdate("INSERT INTO applicant_qualification (type_id, course_id, applicant_id) VALUES("
                        + "(SELECT type_id FROM qualification_type WHERE type_name = \'" + applicantQualification.getType() + "\'), "
                        + "(SELECT course_id FROM course WHERE course_name = \'" + applicantQualification.getCourse() + "\'), \'" + newApplicant.getApplicantID() + "\');");
            }

            return;
        }

        throw new ApplicantExistsException("Applicant " + newApplicant.getApplicantID() + " already exists.");

    }

    @Override
    public List match(Matchable entity) throws SQLException, ClassNotFoundException {

        if ((!(entity instanceof Applicant)) && (entity != null)) {
            throw new IllegalArgumentException("Parsed entity is of an unexpected type. Parsed an object of " + entity.getClass().getSimpleName() + " when expected an object of Applicant");
        }

        Applicant newApplicant = (Applicant) entity;

        List<Vacancy> vacanciesList = getAllVacancies(), matchedVacanciesList = new ArrayList<>();
        List<String> vacanciesQualifiedFor = getQualifiedVacancies(newApplicant.getApplicantID());

        for (Vacancy vacantPost : vacanciesList) {
            if (!vacanciesQualifiedFor.contains(vacantPost.getReferenceNr())) {
                boolean isInterestedInVacancy = false;

                for (String preferredType : newApplicant.getPreferredVacancyTypes()) {
                    if (preferredType.equalsIgnoreCase(getVacancyType(vacantPost.getVacancyTypeId()))) {
                        isInterestedInVacancy = true;
                    }
                }

                boolean hasRequiredQualification = false;

                for (Qualification requiredQualification : vacantPost.getRequiredQualifications()) {
                    if (newApplicant.getApplicantQualifications().contains(requiredQualification)) {
                        hasRequiredQualification = true;
                    }
                }

                boolean hasRequiredSkills = false;
                int applicantSkillsCount = 0;

                for (String skill : vacantPost.getRequiredSkills()) {
                    if (newApplicant.getSkills().contains(skill)) {
                        applicantSkillsCount++;
                    }
                }

                if (applicantSkillsCount >= vacantPost.getRequiredSkills().size()) {
                    hasRequiredSkills = true;
                }

                if (hasRequiredSkills && hasRequiredQualification && isInterestedInVacancy) {
                    matchedVacanciesList.add(vacantPost);
                    insertQualifyingApplicant(newApplicant, vacantPost);
                }
            }
        }

        return matchedVacanciesList;
    }

    private List getQualifiedVacancies(String applicantID) throws SQLException {
        List<String> qualifiedVacanciesList = new ArrayList<>();

        ResultSet results = executeQuery("SELECT vacancy_reference_nr FROM qualifying_applicant WHERE applicant_id = \'" + applicantID + "\';");

        if (getDatabaseManager().hasData(results)) {
            while (results.next()) {
                qualifiedVacanciesList.add(getDatabaseManager().getData(QualifyingApplicantFields.VACANCY_REFERENCE_NR, results));
            }
        }

        return qualifiedVacanciesList;
    }

    public void notifyApplicant(Applicant matchedApplicant, Vacancy vacancyQualified) throws SQLException, MessagingException {
        notify(matchedApplicant.getEmailAddress(), "Vacancy Application",
                "Your profile has been submited for a vacant post application at " + vacancyQualified.getPostingRecruiter().getEnterpriseName() + ".\n"
                + "The post for which you have been identified as a potential candidate has the following details.\n"
                + "Company: " + vacancyQualified.getPostingRecruiter().getEnterpriseName() + "\n"
                + "Vacancy type: "
                + getDatabaseManager().getData(VacancyTypeFields.VACANCY_TYPE,
                        getDatabaseManager().moveCursor(executeQuery("SELECT vacancy_type FROM vacancy_type WHERE vacancy_type_id = " + vacancyQualified.getVacancyTypeId() + ";"))
                ) + "\n"
                + "Vacancy reference number: " + vacancyQualified.getReferenceNr() + "\n"
                + "Vacancy description: " + vacancyQualified.getDescription() + "\n\n"
                + "For more enquiries, please email " + vacancyQualified.getPostingRecruiter().getEnterpriseName() + " at " + vacancyQualified.getPostingRecruiter().getEnterpriseEmail() + ".\n"
                + "This email was automatically sent by the eRecruit system. Please log on to the system to perform various actions.\n"
                + "The eRecruit team wishes you the best of luck on your endeavors.\n\n"
                + "Regards,\nThe eRecruit Team");
    }

    public Applicant getApplicant(String applicantID) throws SQLException, ClassNotFoundException {
        ResultSet rs = executeQuery("SELECT * FROM applicant WHERE applicant_id = \'" + applicantID + "\';");
        Applicant applicant = null;

        if (getDatabaseManager().hasData(rs)) {
            getDatabaseManager().moveCursor(rs);
            String firstName = getStringData(2, rs);
            String middleName = getStringData(3, rs);
            String surname = getStringData(4, rs);
            String emailAddress = getStringData(5, rs);
            String phoneNumber = getStringData(6, rs);
            applicant = new Applicant(applicantID, firstName, middleName, surname, phoneNumber, emailAddress);

            rs = executeQuery("SELECT s.skill FROM skill s, applicant_skill aps WHERE s.skill_id = aps.skill_id AND aps.applicant_id = \'" + applicantID + "\';");

            if (getDatabaseManager().hasData(rs)) {
                while (rs.next()) {
                    applicant.addSkill(getStringData(1, rs));
                }
            }

            rs = executeQuery("SELECT v.vacancy_type FROM vacancy_type v, prefered_vacancy_type pvt WHERE pvt.vacancy_type_id = v.vacancy_type_id AND pvt.applicant_id = \'" + applicantID + "\';");

            if (getDatabaseManager().hasData(rs)) {
                while (rs.next()) {
                    applicant.addPreferedVacancy(getStringData(1, rs));
                }
            }

            rs = executeQuery("SELECT qt.type_name FROM qualification_type qt, applicant_qualification aq WHERE aq.type_id = qt.type_id AND aq.applicant_id = \'" + applicantID + "\';");

            if (getDatabaseManager().hasData(rs)) {
                ResultSet coursesResultSet = new DatabaseManager().executeQuery("SELECT c.course_name FROM course c, applicant_qualification aq WHERE c.course_id = aq.course_id AND aq.applicant_id = \'" + applicantID + "\';");

                while (rs.next()) {
                    coursesResultSet.next();

                    String course = getStringData(1, coursesResultSet);
                    String qualificationType = getStringData(1, rs);

                    applicant.addQualification(new Qualification(qualificationType, course));
                }
            }
        }

        return applicant;
    }

    private boolean applicantQualifies(Applicant applicant, Vacancy vacancy) throws SQLException{
        boolean isInterestedInVacancyType = false;

        for (String applicantVacancyType : applicant.getPreferredVacancyTypes()) {
            String vacancyType = getDatabaseManager().moveCursor(
                    executeQuery("SELECT vacancy_type FROM vacancy_type WHERE vacancy_type_id = " + vacancy.getVacancyTypeId() + ";")
            ).getString(1);
            
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

        return hasRequiredQualification && isInterestedInVacancyType && hasRequiredSkills;
    }

    public List<Vacancy> getApplicantQualifyingVacancies(Applicant applicant) throws SQLException, ClassNotFoundException {
        List<Vacancy> applicantQualifyingVacancies = new ArrayList<>();
        List<Vacancy> postedVacancies = getAllVacancies();
        
        for (Vacancy postedVacancy : postedVacancies){
            System.out.println("Determining if " + applicant.toString() + " qualifies for " + postedVacancy.toString());
            if(applicantQualifies(applicant, postedVacancy)){
                System.out.println(applicant.getFirstName() + " qualifies.");
                applicantQualifyingVacancies.add(postedVacancy);
            }
        }
        
        return applicantQualifyingVacancies;
    }
    
    public void withdrawApplication(Applicant applicant, String vacancyReferenceNr) throws SQLException{
        executeUpdate("DELETE FROM qualifying_applicant WHERE vacancy_reference_nr = \'" + vacancyReferenceNr + "\' AND applicant_id = \'" + applicant.getApplicantID() + "\';");
    }
}
