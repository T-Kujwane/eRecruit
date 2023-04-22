/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.RecruiterFields;
import za.ac.tut.exception.ApplicantExistsException;
import za.ac.tut.handler.ApplicantHandler;
import za.ac.tut.handler.QualificationHandler;
import za.ac.tut.handler.SkillHandler;
import za.ac.tut.handler.VacancyHandler;
import za.ac.tut.qualification.Qualification;

/**
 *
 * @author T Kujwane
 */
public class ApplicationServlet extends HttpServlet {
    
    private final DatabaseManager dbManager;
    private final ApplicantHandler applicantHandler;
    private final VacancyHandler vacancyHandler;
    private final QualificationHandler qualificationHandler;
    private final SkillHandler skillHandler;
    
    @EJB
    @Inject
    private EmailSessionBean emailSessionBean;

    public ApplicationServlet() throws ClassNotFoundException, SQLException {
        this.dbManager = new DatabaseManager();
        this.emailSessionBean = new EmailSessionBean();
        this.applicantHandler = new ApplicantHandler(this.dbManager, this.emailSessionBean);
        this.vacancyHandler = new VacancyHandler(this.dbManager, this.emailSessionBean);
        this.qualificationHandler = new QualificationHandler(dbManager, emailSessionBean);
        this.skillHandler = new SkillHandler(dbManager, emailSessionBean);
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        
        try {   
            addSessionAttribute("skills", this.skillHandler.getSkills(), se);
        } catch (SQLException ex) {
            System.err.println("Unable to get skills.\n" + ex);
            return;
        }
        
        try {
            addSessionAttribute("vacancyTypes", this.vacancyHandler.getVacancyTypes(), se);
        } catch (SQLException ex) {
            System.err.println("Unable to get vacancy types.");
            response.sendError(500, "Unable to get vacancy types");
            return;
        }
        
        response.sendRedirect("addApplicantPage.jsp");
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
        String firstName = getParameter("firstName", request);
        String middleName = getParameter("middleName", request);
        String surname = getParameter("surname", request);
        String applicantID = getParameter("applicantID", request);
        String emailAddress = getParameter("emailAddress", request);
        String phoneNr = getParameter("phoneNumber", request);
        
        Applicant newApplicant = new Applicant(applicantID, firstName, middleName, surname, phoneNr, emailAddress);
        
        List<Qualification> applicantQualifications = getApplicantQualifications(request);
        
        newApplicant.getApplicantQualifications().addAll(applicantQualifications);
        
        newApplicant.getSkills().addAll(getApplicantSkills(request));
        
        newApplicant.getPreferredVacancyTypes().addAll(getApplicantPreferredVacancyTypes(request));
        
        try {
            this.applicantHandler.addApplicant(newApplicant);
        } catch (SQLException ex) {
            System.err.println("Unable to add applicant due to SQL error.\n" + ex);
            ex.printStackTrace(System.err);
            return;
        } catch (ApplicantExistsException ex) {
            System.err.println("Applicant already has a profile.\n" + ex);
            ex.printStackTrace(System.err);
            return;
        }
        
        try {
            this.applicantHandler.match(newApplicant);
        } catch (SQLException ex) {
            System.err.println("Unable to find vacancies matching applicant due to an SQL error.\n" + ex);
            return;
        } catch (ClassNotFoundException ex) {
            System.err.println("Parsed entity type unknown.\n" + ex);
            return;
        }
        
        response.sendRedirect("profileCreated.jsp");
    }
    
    private void addSessionAttribute(String name, Object attribute, HttpSession session) {
        session.setAttribute(name, attribute);
    }
    
    private String getParameter(String parameter, HttpServletRequest request) {
        return request.getParameter(parameter);
    }
    
    private List getApplicantQualifications(HttpServletRequest request){
        List<Qualification> applicantQualifications = new ArrayList<>();
        
        String[] applicantQualificationTypes = getParameterValues("applicantQualificationType", request);
        String[] courses = getParameterValues("course", request);
        
        ArrayList<String> coursesList = new ArrayList<>();
        
        for (String course : courses){
            if (! course.equalsIgnoreCase("null")){
                coursesList.add(course);
            }
        }
        
        if (Arrays.asList(applicantQualificationTypes).contains("National Senior Certificate (NSC)")){
            coursesList.add(0, "Matric Subjects");
        }
        
        for (String course : coursesList){
            applicantQualifications.add(
                    new Qualification(
                            applicantQualificationTypes[coursesList.indexOf(course)], course
                    )
            );
        }
        
        return applicantQualifications;
    }
    
    private List<String> getApplicantSkills(HttpServletRequest request){
        ArrayList<String> skillsList = new ArrayList<>();
        
        skillsList.addAll(Arrays.asList(getParameterValues("applicantSkill", request)));
        
        return skillsList;
    }
    
    private List<String> getApplicantPreferredVacancyTypes(HttpServletRequest request){
        ArrayList<String> preferredVacancyTypesList = new ArrayList<>();
        
        preferredVacancyTypesList.addAll(Arrays.asList(getParameterValues("preferredVacancyType", request)));
        
        return preferredVacancyTypesList;
    }

    private String[] getParameterValues(String paramName, HttpServletRequest request) {
        return request.getParameterValues(paramName);
    }
}
