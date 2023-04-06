/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.enums.ApplicantFields;

/**
 *
 * @author Thato Keith Kujwane
 */
public class DeleteProfileServlet extends HttpServlet {

    private final DatabaseManager dbManager;

    public DeleteProfileServlet() throws ClassNotFoundException, SQLException {
        super();
        this.dbManager = new DatabaseManager("jdbc:mysql://localhost:3306/recruitment_db?useSSL=false", "root", "021121ZWELISHa_");
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
        HttpSession session = getSession(request);

        String applicantID = request.getParameter("id");

        session.setAttribute("applicantID", applicantID);

        String query = "SELECT * FROM applicant WHERE applicant_id = \'" + applicantID + "\';";

        ResultSet resultSet;
        Applicant applicant;

        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get applicant details from database.");
            return;
        }

        try {
            if (resultSet.isBeforeFirst()) {
                resultSet.next();

                String firstName = getApplicantData(ApplicantFields.FIRST_NAME, resultSet);
                String middleName = getApplicantData(ApplicantFields.MIDDLE_NAME, resultSet);
                String surname = getApplicantData(ApplicantFields.SURNAME, resultSet);
                String emailAddress = getApplicantData(ApplicantFields.EMAIL_ADDRESS, resultSet);
                String phoneNr = getApplicantData(ApplicantFields.PHONE_NR, resultSet);
                applicant = new Applicant(applicantID, firstName, middleName, surname, phoneNr, emailAddress);
            } else {
                response.sendRedirect("deleteError.jsp");
                return;
            }
        } catch (SQLException ex) {
            System.err.println("Unable to get applicant details from result set");
            return;
        }

        query = "SELECT vacancy_type FROM vacancy_type vt, prefered_vacancy_type pf WHERE vt.vacancy_type_id = pf.vacancy_type_id AND pf.applicant_id = \'" + applicantID + "\';";

        try {
            resultSet = dbManager.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Unable to get applicant prefared vacancy types from databse.");
            return;
        }

        try {
            while (resultSet.next()) {
                applicant.addPreferedVacancy((String) resultSet.getObject("vacancy_type"));
            }
        } catch (SQLException ex) {
            System.err.println("Unable to process vacancy_types result set");
            return;
        }

        session.setAttribute("applicant", applicant);

        response.sendRedirect("displayDetails.jsp");
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
        HttpSession session = getSession(request);

        String query = "DELETE FROM applicant WHERE applicant_id = \'" + session.getAttribute("applicantID") + "\';";

        try {
            dbManager.executeUpdate(query);
        } catch (SQLException ex) {
            System.err.println("Unable to delete applicant " + session.getAttribute("applicantID"));
            return;
        }
        response.sendRedirect("confirmation.jsp");
    }

    private HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    private String getApplicantData(ApplicantFields field, ResultSet results) throws SQLException {
        return (String) results.getObject(field.name().toLowerCase());
    }
}
