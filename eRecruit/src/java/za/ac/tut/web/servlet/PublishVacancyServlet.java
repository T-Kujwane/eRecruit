/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.ApplicantFields;
import za.ac.tut.enums.CourseFields;
import za.ac.tut.enums.QualificationTypeFields;
import za.ac.tut.enums.RecruiterFields;
import za.ac.tut.enums.SkillFields;
import za.ac.tut.enums.VacancyTypeFields;
import za.ac.tut.interfaces.Matchable;
import za.ac.tut.interfaces.Matcher;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.recruiter.Recruiter;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class PublishVacancyServlet extends HttpServlet implements Matcher {

    @EJB
    private EmailSessionBean emailSessionBean;

    private final DatabaseManager dbManager;
    
    public PublishVacancyServlet() throws ClassNotFoundException, SQLException {
        super();
        this.dbManager = new DatabaseManager("jdbc:mysql://localhost:3306/recruitment_db?useSSL=false", "root", "root");
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession se = request.getSession();

        String query = "SELECT " + VacancyTypeFields.VACANCY_TYPE.name() + " FROM vacancy_type;";
        ResultSet resultSet;

        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get vacancy types in " + this.getClass().getSimpleName());
            return;
        }

        try {
            if (resultSet.isBeforeFirst()) {
                List<String> valuesList = createList();
                while (resultSet.next()) {
                    String vacancyType = dbManager.getData(VacancyTypeFields.VACANCY_TYPE, resultSet);
                    valuesList.add(vacancyType);
                }
                addSessionAttribute("vacancyTypes", valuesList, se);
            }
        } catch (SQLException ex) {
            System.err.println("Unable to process result set in " + getClass().getSimpleName());
        }

        query = "SELECT type_name FROM qualification_type;";

        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get qualification types.");
            return;
        }

        try {
            if (resultSet.isBeforeFirst()) {
                List<String> qualificationTypes = createList();
                while (resultSet.next()) {
                    qualificationTypes.add(dbManager.getData(QualificationTypeFields.TYPE_NAME, resultSet));
                }
                addSessionAttribute("qualificationTypes", qualificationTypes, se);
            }
        } catch (SQLException ex) {
            System.err.println("Unable to get qualification types from result set");
            return;
        }

        query = "SELECT course_name FROM course;";

        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get courses from db.");
            return;
        }

        try {
            List<String> coursesList = createList();
            while (resultSet.next()) {
                coursesList.add(dbManager.getData(CourseFields.COURSE_NAME, resultSet));
            }
            addSessionAttribute("courses", coursesList, se);
        } catch (SQLException ex) {
            System.err.println("Unable to get courses from result set.");
        }

        try {
            query = "SELECT enterprise_name FROM recruiter;";
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get recruiters from db.");
            return;
        }

        try {
            List<String> recruitersList = createList();
            while (resultSet.next()) {
                String enterpriseName = dbManager.getData(RecruiterFields.ENTERPRISE_NAME, resultSet);
                recruitersList.add(enterpriseName);
            }
            addSessionAttribute("recruiters", recruitersList, se);
        } catch (SQLException ex) {
            System.err.println("Unable to get recruiters from result set.");
            return;
        }

        query = "SELECT skill FROM skill;";
        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get skills from db.");
            return;
        }

        try {
            List<String> skillsList = createList();
            while (resultSet.next()) {
                skillsList.add(dbManager.getData(SkillFields.SKILL, resultSet));
            }
            addSessionAttribute("skills", skillsList, se);
        } catch (SQLException ex) {
            System.err.println("Unable to get skills from result set.");
            return;
        }

        response.sendRedirect("addVacancy.jsp");

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String referenceNumber = getParameter("referenceNr", request);
        String vacancyType = getParameter("vacancyType", request);

        Date closingDate = Date.valueOf(getParameter("closingDate", request));

        String description = getParameter("description", request);

        List<Qualification> requiredQualifications = getRequiredQualifications(request);

        String recruiterEnterpriseNr;

        try {
            Recruiter r = getRecruiter(request);
            recruiterEnterpriseNr = r.getEnterpriseNumber();
        } catch (SQLException ex) {
            System.err.println("Unable to get recruiter. \n"
                    + "See the exception message below.\n" + ex.getMessage());
            return;
        }

        List<String> requiredSkills;

        try {
            requiredSkills = getRequiredSkills(request);
        } catch (SQLException ex) {
            System.err.println("Unable to insert new skills into database."
                    + "See exception message below\n" + ex + "\n");
            return;
        }

        int vacancyTypeId;

        try {
            vacancyTypeId = getVacancyTypeId(vacancyType);
        } catch (SQLException ex) {
            System.err.println("Unable to get vacancyType. See exception message below.\n" + ex);
            return;
        }

        Vacancy newVacancy;

        try {
            newVacancy = new Vacancy(referenceNumber, recruiterEnterpriseNr, description, closingDate, requiredQualifications, requiredSkills, vacancyTypeId);
            newVacancy.persist(dbManager);
        } catch (SQLException ex) {
            System.err.println("Unable to add vacancy " + referenceNumber + " to database. See exception message below.\n" + ex);
            ex.printStackTrace();
            response.sendError(500, "Unable to add vacancy to database.");
            return;
        }

        try {
            match(newVacancy);
        } catch (SQLException ex) {
            System.err.println("Unable to match applicants to vacancy. An SQL error has occured\n");
            ex.printStackTrace();
            response.sendError(500, "Unable to find qualifying applicants.");
            return;
        } catch (ClassNotFoundException ex) {
            System.err.println("Unable to match applicants to vacancy. The argument type is unknown");
            response.sendError(500, "Unable to match an entity of an unknown type.");
            return;
        } catch (MessagingException ex) {
            System.err.println("Unable to send emails to qualifying applicants.");
            ex.printStackTrace(System.err);
            response.sendError(500, "An error has occured while trying to notify applicants.");
            return;
        }

        response.sendRedirect("vacancyAdded.jsp");
    }

    private String getParameter(String parameter, HttpServletRequest request) {
        return request.getParameter(parameter);
    }

    private ArrayList createList() {
        return new ArrayList<>();
    }

    private void addSessionAttribute(String name, Object attribute, HttpSession session) {
        session.setAttribute(name, attribute);
    }

    private String[] getParameterValues(String parameter, HttpServletRequest request) {
        return request.getParameterValues(parameter);
    }

    private List<String> getRequiredQualificationTypes(HttpServletRequest request) {
        String[] requiredQualifications = getParameterValues("requiredQualification", request);

        ArrayList<String> requiredQualificationsList = createList();

        requiredQualificationsList.addAll(Arrays.asList(requiredQualifications));

        return requiredQualificationsList;
    }

    private List<String> getRequiredCourses(HttpServletRequest request) {
        String[] courses = getParameterValues("course", request);

        List<String> requiredCourses = createList();

        for (String course : courses) {
            if (!course.equalsIgnoreCase("null")) {
                requiredCourses.add(course);
            }
        }

        return requiredCourses;
    }

    private Recruiter getRecruiter(HttpServletRequest request) throws SQLException {
        String recruiter = getParameter("recruiter", request);

        if (recruiter.equalsIgnoreCase("newRecruiter")) {
            String enterpriseNr = getParameter("newRecruiterEnterpriseNr", request);
            String name = getParameter("newRecruiterEnterpriseName", request);
            String email = getParameter("newRecruiterEnterpriseEmail", request);
            String phone = getParameter("newRecruiterEnterprisePhone", request);

            String query = "INSERT INTO recruiter VALUES(?,?,?,?)";
            PreparedStatement ps = this.dbManager.getConnection().prepareStatement(query);

            ps.setString(1, enterpriseNr);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);

            ps.execute();

            return new Recruiter(name, email, phone, enterpriseNr);
        }

        String query = "SELECT enterprise_nr FROM recruiter WHERE enterprise_name = \'" + recruiter + "\';";

        ResultSet rs = dbManager.executeQuery(query);

        rs.next();

        return new Recruiter(recruiter, null, null, dbManager.getData(RecruiterFields.ENTERPRISE_NR, rs));
    }

    private List getRequiredQualifications(HttpServletRequest request) {
        List<String> requiredQualificationTypesList = getRequiredQualificationTypes(request);
        List<String> requiredCoursesList = getRequiredCourses(request);

        List<Qualification> qualificationsList = createList();

        String nsc = ((List<String>) request.getSession().getAttribute("qualificationTypes")).get(0);

        if (requiredQualificationTypesList.contains(nsc)) {
            requiredCoursesList.add(requiredQualificationTypesList.indexOf(nsc), "Matric Subjects");
        }

        for (int i = 0; i < requiredQualificationTypesList.size(); i++) {
            String type = requiredQualificationTypesList.get(i);
            String course = requiredCoursesList.get(i);

            qualificationsList.add(new Qualification(type, course));
        }

        return qualificationsList;
    }

    private List getRequiredSkills(HttpServletRequest request) throws SQLException {
        ArrayList<String> requiredSkills = createList();

        String[] skills = getParameterValues("requiredSkill", request);

        if (skills != null) {
            for (String skill : skills) {
                if (!skill.equalsIgnoreCase("otherSkill")) {
                    requiredSkills.add(skill);
                } else {
                    String[] newSkills = getParameter("newSkills", request).split("#");
                    requiredSkills.addAll(Arrays.asList(newSkills));

                    for (String newSkill : newSkills) {
                        dbManager.executeUpdate("INSERT INTO skill(skill) VALUES(\'" + newSkill + "\');");
                    }
                }
            }
        }

        return requiredSkills;
    }

    private Integer getVacancyTypeId(String vacancyType) throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = \'" + vacancyType + "\';");
        rs.next();

        return rs.getInt("vacancy_type_id");
    }

    @Override
    public void match(Matchable entity) throws SQLException, ClassNotFoundException, MessagingException {

        if (!(entity instanceof Vacancy)) {
            throw new IllegalArgumentException("Parsed entity is of an invalid type.");
        }

        Vacancy vacancy = (Vacancy) entity;

        ResultSet resultSet = new DatabaseManager("jdbc:mysql://localhost:3306/recruitment_db?useSSL=false", "root", "root").executeQuery("SELECT * FROM applicant;");
        List<String> applicantIds = createList();
        
        List<Applicant> applicantsList = createList();
        
        while (resultSet.next()) {
            
            String id = dbManager.getData(ApplicantFields.APPLICANT_ID, resultSet);
            String firstName = dbManager.getData(ApplicantFields.FIRST_NAME, resultSet);
            String middleName = dbManager.getData(ApplicantFields.MIDDLE_NAME, resultSet);
            String surname = dbManager.getData(ApplicantFields.SURNAME, resultSet);
            String emailAddress = dbManager.getData(ApplicantFields.EMAIL_ADDRESS, resultSet);
            String phoneNr = dbManager.getData(ApplicantFields.PHONE_NR, resultSet);

            Applicant applicant = new Applicant(id, firstName, middleName, surname, phoneNr, emailAddress);

            String query = "SELECT s.skill FROM skill s, applicant_skill aps WHERE aps.skill_id = s.skill_id AND aps.applicant_id = \'" + id + "\';";
            ResultSet tempResultSet = dbManager.executeQuery(query);

            if (hasData(tempResultSet)) {
                while (tempResultSet.next()) {
                    applicant.addSkill(dbManager.getData(SkillFields.SKILL, tempResultSet));
                }
            }

            query = "SELECT qt.type_name, c.course_name FROM qualification_type qt, course c, applicant_qualification apq WHERE apq.type_id = qt.type_id AND "
                    + "apq.course_id = c.course_id AND apq.applicant_id = \'" + id + "\';";
            tempResultSet = dbManager.executeQuery(query);

            if (hasData(tempResultSet)) {
                while (tempResultSet.next()) {
                    String type = dbManager.getData(QualificationTypeFields.TYPE_NAME, tempResultSet);
                    String course = dbManager.getData(CourseFields.COURSE_NAME, tempResultSet);

                    applicant.addQualification(new Qualification(type, course));
                }
            }

            query = "SELECT vt.vacancy_type FROM vacancy_type vt, prefered_vacancy_type pvt WHERE pvt.vacancy_type_id = vt.vacancy_type_id AND pvt.applicant_id = \'" + id + "\';";

            tempResultSet = dbManager.executeQuery(query);

            if (hasData(tempResultSet)) {
                while (tempResultSet.next()) {
                    applicant.addPreferedVacancy(dbManager.getData(VacancyTypeFields.VACANCY_TYPE, tempResultSet));
                }
            }

            applicantsList.add(applicant);
        }

        resultSet = dbManager.executeQuery("SELECT vacancy_type FROM vacancy_type WHERE vacancy_type_id = " + vacancy.getVacancyTypeId() + ";");
        resultSet.next();

        String vacancyType = dbManager.getData(VacancyTypeFields.VACANCY_TYPE, resultSet);

        for (Applicant applicant : applicantsList) {
            System.out.println(applicant.toString());
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
                LocalDate now = LocalDate.now();
                String query = "INSERT INTO qualifying_applicant (vacancy_reference_nr, applicant_id, date_qualified) VALUES(\'" + vacancy.getReferenceNr()
                        + "\',\'" + applicant.getApplicantID() + "\',STR_TO_DATE(\'" + now.toString() + "\', \'%Y-%c-%d\'));";
                this.emailSessionBean.sendEmail(applicant.getEmailAddress(), "Application For Vacancy", "Your profile has been submited for a vacant post at " + vacancy.getRecruiterEnterpriseNr());
                dbManager.executeUpdate(query);
            }
        }
    }

    private boolean hasData(ResultSet rs) throws SQLException {
        return rs.isBeforeFirst();
    }
    //System.err.println("Unable to send emails to qualifying applicant");
                    //return;
}
