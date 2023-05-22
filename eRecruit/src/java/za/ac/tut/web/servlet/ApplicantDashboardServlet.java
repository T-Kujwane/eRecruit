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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.handler.ApplicantHandler;
import za.ac.tut.handler.VacancyHandler;
import za.ac.tut.user.User;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class ApplicantDashboardServlet extends HttpServlet {

    private DatabaseManager dbManager;
    private ApplicantHandler applicantHandler;

    public ApplicantDashboardServlet() throws ClassNotFoundException, SQLException {
        this.dbManager = new DatabaseManager();
        this.applicantHandler = new ApplicantHandler(dbManager);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ApplicantDashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApplicantDashboardServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        HttpSession session = request.getSession();
        String applicantID;

        try {
            ResultSet rs = this.dbManager.executeQuery(
                    "SELECT applicant_id FROM applicant WHERE email_address = \'" + ((User) session.getAttribute("user")).getEmailAddress() + "\';"
            );
            dbManager.moveCursor(rs);
            applicantID = rs.getString(1);
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "The system has failed to get applicant information from the database, and the development team has been notified.");
            response.sendError(500);
            ex.printStackTrace(System.err);
            return;
        }
        
        Applicant currentApplicant;
        
        try {
            currentApplicant = this.applicantHandler.getApplicant(applicantID);
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "The system has failed to get applicant information from the database, and the development team has been notified.");
            response.sendError(500);
            ex.printStackTrace(System.err);
            return;
        } catch (ClassNotFoundException ex) {
            session.setAttribute("exceptionTitle", "Internal Server Error");
            session.setAttribute("exceptionMsg", "A server error occured. Please notify the developer at developer.tk_kujwane@outlook.com");
            response.sendError(500);
            return;
        }
        
        List<Vacancy> applicantQualifyingVacancies;
        List<String> vacancyTypes = new ArrayList<>();
        
        try {
            applicantQualifyingVacancies = this.applicantHandler.getApplicantQualifyingVacancies(currentApplicant);
            
            for(Vacancy vacancy : applicantQualifyingVacancies){
                vacancyTypes.add(new VacancyHandler(dbManager, null).getVacancyType(vacancy.getVacancyTypeId()));
            }
            
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "The system encountered an error while trying to get the vacancies you qualify for. The development team has been notified..");
            response.sendError(500);
            ex.printStackTrace(System.err);
            return;
        } catch (ClassNotFoundException ex) {
            session.setAttribute("exceptionTitle", "Internal Server Error");
            session.setAttribute("exceptionMsg", "A server error occured. Please notify the developer at developer.tk_kujwane@outlook.com");
            ex.printStackTrace(System.err);
            response.sendError(500);
            return;
        }
        
        session.setAttribute("vacancyTypes", vacancyTypes);
        session.setAttribute("qualifyingVacancies", applicantQualifyingVacancies);
        session.setAttribute("applicant", currentApplicant);
        
        response.sendRedirect("applicantDashboard.jsp");
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
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
