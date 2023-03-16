/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.enums.CourseFields;
import za.ac.tut.enums.QualificationTypeFields;
import za.ac.tut.enums.VacancyTypeFields;

/**
 *
 * @author T Kujwane
 */
public class PublishVacancyServlet extends HttpServlet {
    
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
        
        List<String> valuesList = new ArrayList<>();
        
        try {
            if (resultSet.isBeforeFirst()){
                while (resultSet.next()) {                    
                    String vacancyType = dbManager.getData(VacancyTypeFields.VACANCY_TYPE, resultSet);
                    valuesList.add(vacancyType);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Unable to process result set in " + getClass().getSimpleName());
        }
        
        se.setAttribute("vacancyTypes", valuesList);
        
        
        query = "SELECT type_name FROM qualification_type;";
        
        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get qualification types.");
            return;
        }
        
        List<String> qualificationTypes = new ArrayList<>();
        
        try {
            if (resultSet.isBeforeFirst()){
                while (resultSet.next()) {                    
                    qualificationTypes.add(dbManager.getData(QualificationTypeFields.TYPE_NAME, resultSet));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Unable to get qualification types from result set");
            return;
        }
        
        se.setAttribute("qualificationTypes", qualificationTypes);
        
        query = "SELECT course_name FROM courses;";
        
        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get coursesList");
        }
        
        List<String> coursesList = new ArrayList<>();
        
        query = "SELECT course_name FROM course;";
        
        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get courses from db.");
            return;
        }
        
        try {
            while(resultSet.next()){
                coursesList.add(dbManager.getData(CourseFields.COURSE_NAME, resultSet));
            }
        } catch (SQLException ex) {
            System.err.println("Unable to get courses from result set.");
        }
        
        se.setAttribute("courses", coursesList);
        
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
        //Date closingDate = new Date(getParameter("closingDate", request));
        
        Date closingDate = Date.valueOf(vacancyType);
        
        String description = getParameter("description", request);
        
        //String query = ""
    }

    private String getParameter(String parameter, HttpServletRequest request) {
        return request.getParameter(parameter);
    }
    
}
