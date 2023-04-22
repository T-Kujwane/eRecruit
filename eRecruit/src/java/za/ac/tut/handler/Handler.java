/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.ApplicantFields;
import za.ac.tut.enums.CourseFields;
import za.ac.tut.enums.QualificationTypeFields;
import za.ac.tut.enums.RecruiterFields;
import za.ac.tut.enums.SkillFields;
import za.ac.tut.enums.VacancyFields;
import za.ac.tut.enums.VacancyTypeFields;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.recruiter.Recruiter;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public abstract class Handler {

    private final DatabaseManager dbManager;

    public Handler(DatabaseManager dbManager) throws ClassNotFoundException, SQLException {
        this.dbManager = dbManager;
    }

    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }
    
    protected List<Applicant> getAllApplicants() throws SQLException, ClassNotFoundException {
        List<Applicant> applicantsList = new ArrayList<>();

        ResultSet resultSet = new DatabaseManager().executeQuery("SELECT * FROM applicant;");

        while (resultSet.next()) {

            String id = this.dbManager.getData(ApplicantFields.APPLICANT_ID, resultSet);
            String firstName = this.dbManager.getData(ApplicantFields.FIRST_NAME, resultSet);
            String middleName = this.dbManager.getData(ApplicantFields.MIDDLE_NAME, resultSet);
            String surname = this.dbManager.getData(ApplicantFields.SURNAME, resultSet);
            String emailAddress = this.dbManager.getData(ApplicantFields.EMAIL_ADDRESS, resultSet);
            String phoneNr = this.dbManager.getData(ApplicantFields.PHONE_NR, resultSet);

            Applicant applicant = new Applicant(id, firstName, middleName, surname, phoneNr, emailAddress);

            String query = "SELECT s.skill FROM skill s, applicant_skill aps WHERE aps.skill_id = s.skill_id AND aps.applicant_id = \'" + id + "\';";
            ResultSet tempResultSet = this.dbManager.executeQuery(query);

            if (this.dbManager.hasData(tempResultSet)) {
                while (tempResultSet.next()) {
                    applicant.addSkill(this.dbManager.getData(SkillFields.SKILL, tempResultSet));
                }
            }

            query = "SELECT qt.type_name, c.course_name FROM qualification_type qt, course c, applicant_qualification apq WHERE apq.type_id = qt.type_id AND "
                    + "apq.course_id = c.course_id AND apq.applicant_id = \'" + id + "\';";
            tempResultSet = this.dbManager.executeQuery(query);

            if (this.dbManager.hasData(tempResultSet)) {
                while (tempResultSet.next()) {
                    String type = this.dbManager.getData(QualificationTypeFields.TYPE_NAME, tempResultSet);
                    String course = this.dbManager.getData(CourseFields.COURSE_NAME, tempResultSet);

                    applicant.addQualification(new Qualification(type, course));
                }
            }

            query = "SELECT vt.vacancy_type FROM vacancy_type vt, prefered_vacancy_type pvt WHERE pvt.vacancy_type_id = vt.vacancy_type_id AND pvt.applicant_id = \'" + id + "\';";

            tempResultSet = this.dbManager.executeQuery(query);

            if (this.dbManager.hasData(tempResultSet)) {
                while (tempResultSet.next()) {
                    applicant.addPreferedVacancy(this.dbManager.getData(VacancyTypeFields.VACANCY_TYPE, tempResultSet));
                }
            }

            applicantsList.add(applicant);
        }

        return applicantsList;
    }

    protected Recruiter getRecruiter(String enterpriseNr) throws SQLException {
        ResultSet rs = this.dbManager.executeQuery("SELECT * FROM recruiter WHERE enterprise_nr = \'" + enterpriseNr + "\';");

        if (this.dbManager.hasData(rs)) {
            rs.next();

            return new Recruiter(dbManager.getData(RecruiterFields.ENTERPRISE_NAME, rs),
                    this.dbManager.getData(RecruiterFields.ENTERPRISE_EMAIL, rs),
                    this.dbManager.getData(RecruiterFields.ENTERPRISE_PHONE_NR, rs),
                    enterpriseNr);
        }

        return null;
    }

    protected ResultSet executeQuery(String sqlQuery) throws SQLException {
        return this.dbManager.executeQuery(sqlQuery);
    }

    protected void executeUpdate(String sqlUpdate) throws SQLException {
        this.dbManager.executeUpdate(sqlUpdate);
    }

    protected List getAllVacancies() throws SQLException, ClassNotFoundException {
        List<Vacancy> vacanciesList = new ArrayList<>();

        ResultSet results = new DatabaseManager().executeQuery("SELECT * FROM vacancy");

        if (getDatabaseManager().hasData(results)) {
            while (results.next()) {
                String refNr = getDatabaseManager().getData(VacancyFields.REFERENCE_NR, results);
                String description = getDatabaseManager().getData(VacancyFields.VACANCY_DESCRIPTION, results);
                Recruiter postingRecruiter = getRecruiter(getDatabaseManager().getData(VacancyFields.RECRUITER_ENTERPRISE_NR, results));
                Date closingDate = results.getDate(VacancyFields.CLOSING_DATE.name().toLowerCase());
                int vacancyTypeID = results.getInt(VacancyFields.VACANCY_TYPE_ID.name().toLowerCase());

                Vacancy vacancy = new Vacancy(refNr, description, closingDate, vacancyTypeID, postingRecruiter);

                ResultSet tempResultSet = executeQuery("SELECT vacancy_type FROM vacancy_type WHERE vacancy_type_id = " + vacancyTypeID + ";");

                getDatabaseManager().moveCursor(tempResultSet);
                
                tempResultSet = executeQuery("SELECT skill FROM skill WHERE skill_id = (SELECT skill_id FROM required_skill WHERE vacancy_reference_nr = \'" + refNr + "\');");

                if (getDatabaseManager().hasData(tempResultSet)) {
                    while (tempResultSet.next()) {
                        vacancy.addSkill(getDatabaseManager().getData(SkillFields.SKILL, results));
                    }
                }

                List<String> qualificationTypesList = new ArrayList<>();

                tempResultSet = executeQuery("SELECT type_name FROM qualification_type WHERE type_id = (SELECT type_id FROM required_qualification WHERE vacancy_reference_nr = \'" + refNr + "\');");

                if (getDatabaseManager().hasData(tempResultSet)) {
                    while (tempResultSet.next()) {
                        qualificationTypesList.add(getStringData(1, results));
                    }
                }

                ArrayList<String> courseList = new ArrayList<>();
                tempResultSet = executeQuery("SELECT course_name FROM course WHERE course_id = (SELECT course_id FROM required_qualification WHERE vacancy_reference_nr = \'" + refNr + "\');");

                if (getDatabaseManager().hasData(tempResultSet)) {
                    while (tempResultSet.next()) {
                        courseList.add(getStringData(1, results));
                    }
                }

                for (String type : qualificationTypesList) {
                    String course = courseList.get(qualificationTypesList.indexOf(type));

                    vacancy.addRequiredQualification(new Qualification(type, course));
                }

                vacanciesList.add(vacancy);
            }
        }

        return vacanciesList;
    }

    protected String getVacancyType(int typeID) throws SQLException {
        ResultSet resultSet = executeQuery("SELECT vacancy_type FROM vacancy_type WHERE vacancy_type_id = " + typeID + ";");
        resultSet.next();

        return this.dbManager.getData(VacancyTypeFields.VACANCY_TYPE, resultSet);
    }

    protected void insertQualifyingApplicant(Applicant qualifyingApplicant, Vacancy qualifiedVacancy) throws SQLException {
        String query = "INSERT INTO qualifying_applicant (vacancy_reference_nr, applicant_id, date_qualified) VALUES(\'" + qualifiedVacancy.getReferenceNr()
                + "\',\'" + qualifyingApplicant.getApplicantID() + "\',STR_TO_DATE(\'" + LocalDate.now().toString() + "\', \'%Y-%c-%d\'));";
        executeUpdate(query);
    }
    
    protected String getStringData(Enum columnName, ResultSet queryResults) throws SQLException{
        return this.dbManager.getData(columnName, queryResults);
    }
    
    protected String getStringData(int columnIndex, ResultSet queryResult) throws SQLException{
        return queryResult.getString(columnIndex);
    }
}
