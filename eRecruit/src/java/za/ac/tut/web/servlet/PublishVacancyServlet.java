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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.RecruiterFields;
import za.ac.tut.enums.VacancyTypeFields;
import za.ac.tut.exception.VacancyExistsException;
import za.ac.tut.handler.ApplicantHandler;
import za.ac.tut.handler.QualificationHandler;
import za.ac.tut.handler.SkillHandler;
import za.ac.tut.handler.VacancyHandler;
import za.ac.tut.qualification.Qualification;
import za.ac.tut.recruiter.Recruiter;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class PublishVacancyServlet extends HttpServlet {

    private final DatabaseManager dbManager;
    private final ApplicantHandler applicantHandler;
    private final VacancyHandler vacancyHandler;
    private final QualificationHandler qualificationHandler;
    private final SkillHandler skillHandler;
    
    @EJB
    @Inject
    private EmailSessionBean emailSessionBean;

    public PublishVacancyServlet() throws ClassNotFoundException, SQLException {
        super();
        this.dbManager = new DatabaseManager();
        this.emailSessionBean = new EmailSessionBean();
        this.applicantHandler = new ApplicantHandler(this.dbManager, this.emailSessionBean);
        this.vacancyHandler = new VacancyHandler(this.dbManager, this.emailSessionBean);
        this.qualificationHandler = new QualificationHandler(dbManager);
        this.skillHandler = new SkillHandler(dbManager);
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

        try {
            addSessionAttribute("vacancyTypes", this.vacancyHandler.getVacancyTypes(), se);
        } catch (SQLException ex) {
            System.err.println("Unable to get vacancy types in " + this.getClass().getSimpleName());
            return;
        }
        
        try {
            addSessionAttribute("qualificationTypes", this.qualificationHandler.getQualificationTypes(), se);
        } catch (SQLException ex) {
            System.err.println("Unable to get qualification types.");
            return;
        }

        try {
            addSessionAttribute("courses", this.qualificationHandler.getCourses(), se);
        } catch (SQLException ex) {
            System.err.println("Unable to get courses.");
        }
        
        ResultSet resultSet;
        
        try {
            String query = "SELECT enterprise_name FROM recruiter;";
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

        try {   
            addSessionAttribute("skills", this.skillHandler.getSkills(), se);
        } catch (SQLException ex) {
            System.err.println("Unable to get skills.\n" + ex);
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
        
        String referenceNumber = getParameter("referenceNr", request);
        String vacancyType = getParameter("vacancyType", request);

        Date closingDate = Date.valueOf(getParameter("closingDate", request));

        String description = getParameter("description", request);

        List<Qualification> requiredQualifications = getRequiredQualifications(request);
        
        Recruiter postingRecruiter;

        try {
            postingRecruiter = getRecruiter(request);
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
            newVacancy = new Vacancy(referenceNumber, description, closingDate, requiredQualifications, requiredSkills, vacancyTypeId, postingRecruiter);
            this.vacancyHandler.addVacancy(newVacancy);
        } catch (SQLException ex) {
            System.err.println("Unable to add vacancy " + referenceNumber + " to database. Due to an SQL exception. See exception message below.\n" + ex);
            ex.printStackTrace(System.err);
            response.sendError(500, "Unable to add vacancy to database.");
            return;
        } catch (VacancyExistsException ex) {
            System.err.println("Unable to add vacancy " + referenceNumber + " to database. See exception message below.\n" + ex);
            ex.printStackTrace(System.err);
            response.sendError(500, "Unable to add vacancy to database.");
            return;
        }

        List<Applicant> matchingApplicantsList;

        try {
            matchingApplicantsList = this.vacancyHandler.match(newVacancy);
        } catch (SQLException ex) {
            System.err.println("Unable to match applicants to vacancy. An SQL error has occured\n");
            ex.printStackTrace(System.err);
            response.sendError(500, "Unable to find qualifying applicants.");
            return;
        } catch (ClassNotFoundException ex) {
            System.err.println("Unable to match applicants to vacancy. The argument type is unknown");
            response.sendError(500, "Unable to match an entity of an unknown type.");
            return;
        }

        if (!matchingApplicantsList.isEmpty()) {
            for (Applicant matchedApplicant : matchingApplicantsList) {
                System.out.println("Notifying " + matchedApplicant.getEmailAddress() + " of the vacancy " + newVacancy.getReferenceNr() + " from " + newVacancy.getPostingRecruiter().getEnterpriseName());
                try {
                    this.applicantHandler.notifyApplicant(matchedApplicant, newVacancy);
                } catch (MessagingException ex) {
                    System.err.println("Unable to send emails to qualifying applicants.");
                    ex.printStackTrace(System.err);
                    response.sendError(500, "An error has occured while trying to notify applicants.");
                    return;
                } catch (SQLException ex) {
                    System.err.println("An error has occured while trying to get the vacancy type from the database.");
                    ex.printStackTrace(System.err);
                    response.sendError(500, "Unable to get vacancy type from database when notifying applicant.");
                    return;
                }
            }
        }

        if (!matchingApplicantsList.isEmpty()) {
            for (Applicant matchedApplicant : matchingApplicantsList) {
                System.out.println("Notifying " + matchedApplicant.getEmailAddress() + " of the vacancy " + newVacancy.getReferenceNr() + " from " + newVacancy.getPostingRecruiter().getEnterpriseName());
                try {
                    this.applicantHandler.notify(matchedApplicant.getEmailAddress(), "Vacancy Application", 
                                    "Your profile has been submited for a vacant post application at " + newVacancy.getPostingRecruiter().getEnterpriseName() + ".\n" + 
                                    "The post for which you have been identified as a potential candidate has the following details.\n" + 
                                    "Company: " + newVacancy.getPostingRecruiter().getEnterpriseName() + "\n" + 
                                    "Vacancy type: " + 
                                            dbManager.getData(VacancyTypeFields.VACANCY_TYPE, 
                                                    dbManager.moveCursor(dbManager.executeQuery("SELECT vacancy_type FROM vacancy_type WHERE vacancy_type_id = " + newVacancy.getVacancyTypeId() + ";"))
                                            ) + "\n" +
                                    "Vacancy reference number: " + newVacancy.getReferenceNr() +"\n" + 
                                    "Vacancy description: " + newVacancy.getDescription() + "\n\n" + 
                                    "For more enquiries, please email " + newVacancy.getPostingRecruiter().getEnterpriseName() + " at " + newVacancy.getPostingRecruiter().getEnterpriseEmail() + ".\n" +
                                    "This email was automatically sent by the eRecruit system. Please log on to the system to perform various actions.\n" + 
                                    "The eRecruit team wishes you the best of luck on your endeavors.\n\n" + 
                                    "Regards,\nThe eRecruit Team");
                } catch (MessagingException ex) {
                    System.err.println("Unable to send emails to qualifying applicants.");
                    ex.printStackTrace(System.err);
                    response.sendError(500, "An error has occured while trying to notify applicants.");
                    return;
                } catch (SQLException ex) {
                    System.err.println("An error has occured while trying to get the vacancy type from the database.");
                    ex.printStackTrace(System.err);
                    response.sendError(500, "Unable to get vacancy type from database when notifying applicant.");
                    return;
                }
            }
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
            PreparedStatement ps = this.dbManager.prepareStatement(query);

            ps.setString(1, enterpriseNr);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);

            ps.execute();

            return new Recruiter(name, email, phone, enterpriseNr);
        }

        String query = "SELECT * FROM recruiter WHERE enterprise_name = \'" + recruiter + "\';";

        ResultSet rs = dbManager.executeQuery(query);

        rs.next();

        return new Recruiter(recruiter, dbManager.getData(RecruiterFields.ENTERPRISE_EMAIL, rs), dbManager.getData(RecruiterFields.ENTERPRISE_PHONE_NR, rs), dbManager.getData(RecruiterFields.ENTERPRISE_NR, rs));
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

    //System.err.println("Unable to send emails to qualifying applicant");
    //return;
}
