/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.ApplicantFields;
import za.ac.tut.enums.VacancyFields;
import za.ac.tut.enums.VacancyTypeFields;
import za.ac.tut.exception.VacancyExistsException;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.interfaces.Matcher;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.recruiter.Recruiter;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class VacancyHandler extends NotificationHandler implements Matcher {

    private RecruiterHandler recruiterHandler;

    public VacancyHandler(DatabaseManager dbManager, EmailSessionBean emailBean) throws ClassNotFoundException, SQLException {
        super(dbManager, emailBean);
        this.recruiterHandler = new RecruiterHandler(dbManager);
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

    public List<Applicant> getMatchedApplicants(String vacancyRefNr) throws SQLException {
        List<Applicant> matchedApplicantsList = new ArrayList<>();

        List<String> matchedApplicantIDs = getMatchedApplicantIDList(vacancyRefNr);
        if (!matchedApplicantIDs.isEmpty()) {
            for (String applicantID : matchedApplicantIDs) {
                ResultSet rs = executeQuery("SELECT * FROM applicant WHERE applicant_id = \'" + applicantID + "\';");
                rs.next();

                String firstName = getStringData(ApplicantFields.FIRST_NAME, rs);
                String middleName = getStringData(ApplicantFields.MIDDLE_NAME, rs);
                String surname = getStringData(ApplicantFields.SURNAME, rs);
                String emailAddress = getStringData(ApplicantFields.EMAIL_ADDRESS, rs);
                String phoneNr = getStringData(ApplicantFields.PHONE_NR, rs);

                matchedApplicantsList.add(new Applicant(applicantID, firstName, middleName, surname, phoneNr, emailAddress));
            }
        }
        return matchedApplicantsList;
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
            executeUpdate(insertSkillsQuery);
        }

        for (Qualification requiredQuailification : newVacancy.getRequiredQualifications()) {
            System.out.println("Inserting qualification \"" + requiredQuailification + "\" into database from " + getClass().getSimpleName());
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

    public List<Vacancy> getAllVacancies(String enterpriseNr) throws SQLException, ClassNotFoundException {
        List<Vacancy> recruiterVacanciesList = new ArrayList<>();

        List<Vacancy> vacanciesList = super.getAllVacancies();

        if (!vacanciesList.isEmpty()) {
            for (Vacancy postedVacancy : vacanciesList) {
                if (postedVacancy.getPostingRecruiter().getEnterpriseNumber().equalsIgnoreCase(enterpriseNr)) {
                    recruiterVacanciesList.add(postedVacancy);
                }
            }
        }

        return recruiterVacanciesList;
    }

    public void withdrawVacancy(String vacancyRefNr) throws SQLException, MessagingException {
        Vacancy postedVacancy = getVacancy(vacancyRefNr);
        
        if (postedVacancy != null) {
            List<Applicant> qualifyingApplicantsList = getMatchedApplicants(vacancyRefNr);
            executeUpdate("DELETE FROM vacancy WHERE reference_nr = \'" + vacancyRefNr + "\';");
            
            for (Applicant qualifyingApplicant : qualifyingApplicantsList){
                this.notify(qualifyingApplicant.getEmailAddress(), "Vacancy Withdrawal", "Greetings " + qualifyingApplicant.getFirstName() + ",\n\n"
                        + "We regret to inform you that " + postedVacancy.getPostingRecruiter().getEnterpriseName() + " has decided to withdraw the vacancy " + postedVacancy.getReferenceNr() + 
                        " for which you were deemed as qualified.\n"
                        + "More vacancies will be posted on the eRecruit system, surely something will come up.\n"
                        + "As always, we wish you the best of luck on your endeavours.\n\n"
                        + "Regards, \n"
                        + "The eRecruit Team");
            }
        }
    }

    public Vacancy getVacancy(String refNr) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM VACANCY WHERE reference_nr = \'" + refNr + "\';");

        if (getDatabaseManager().hasData(rs)) {
            rs.next();

            String description = getStringData(VacancyFields.VACANCY_DESCRIPTION, rs);
            Date closingDate = rs.getDate(VacancyFields.CLOSING_DATE.name().toLowerCase());
            Integer vacancyTypeID = rs.getInt(VacancyFields.VACANCY_TYPE_ID.name().toLowerCase());
            String recruiterEnterpriseNr = getStringData(VacancyFields.RECRUITER_ENTERPRISE_NR, rs);

            Recruiter postingRecruiter = this.recruiterHandler.getRecruiter(recruiterEnterpriseNr);

            Vacancy postedVacancy = new Vacancy(refNr, description, closingDate, vacancyTypeID, postingRecruiter);

            rs = executeQuery("SELECT s.skill FROM skill s, required_skill r WHERE s.skill_id = r.skill_id AND LOWER(r.vacancy_reference_nr) = \'" + refNr.toLowerCase() + "\';");

            if (getDatabaseManager().hasData(rs)) {
                while (rs.next()) {
                    postedVacancy.getRequiredSkills().add(getStringData(1, rs));
                }
            }

            rs = executeQuery("SELECT qt.type_name FROM qualification_type qt, required_qualification rq WHERE qt.type_id = rq.type_id AND LOWER(rq.vacancy_reference_nr) = \'" + refNr.toLowerCase() + "\';");
            ArrayList<String> typesList = new ArrayList<>();

            if (getDatabaseManager().hasData(rs)) {
                while (rs.next()) {
                    typesList.add(getStringData(1, rs));
                }
            }

            rs = executeQuery("SELECT c.course_name FROM course c, required_qualification rq WHERE c.course_id = rq.course_id AND LOWER(rq.vacancy_reference_nr) = \'" + refNr + "\';");

            ArrayList<String> coursesList = new ArrayList<>();

            if (getDatabaseManager().hasData(rs)) {
                while (rs.next()) {
                    coursesList.add(getStringData(1, rs));
                }
            }

            if ((!typesList.isEmpty()) && (!coursesList.isEmpty())) {
                for (String type : typesList) {
                    postedVacancy.getRequiredQualifications().add(new Qualification(type, coursesList.get(typesList.indexOf(type))));
                }
            }

            return postedVacancy;
        }

        return null;
    }
    
    @Override
    public String getVacancyType(int typeID) throws SQLException{
        return super.getVacancyType(typeID);
    }
}
