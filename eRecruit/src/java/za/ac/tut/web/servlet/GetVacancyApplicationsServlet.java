/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdk.internal.net.http.HttpRequestImpl;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.handler.ApplicantHandler;
import za.ac.tut.handler.RecruiterHandler;
import za.ac.tut.handler.VacancyHandler;
import za.ac.tut.vacancy.Vacancy;

/**
 *
 * @author T Kujwane
 */
public class GetVacancyApplicationsServlet extends HttpServlet {
    private final DatabaseManager dbManager;
    private final ApplicantHandler applicantHandler;
    private final VacancyHandler vacancyHandler;
    private final RecruiterHandler recruiterHandler;
    private final PrintStream err;
    
    public GetVacancyApplicationsServlet() throws ClassNotFoundException, SQLException {
        super();
        this.dbManager = new DatabaseManager();
        this.applicantHandler = new ApplicantHandler(dbManager, new EmailSessionBean());
        this.vacancyHandler = new VacancyHandler(dbManager, new EmailSessionBean());
        this.recruiterHandler = new RecruiterHandler(dbManager);
        err = System.err;
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
        /*request.getSession().setAttribute("postingURL", getClass().getSimpleName());
        
        request.getRequestDispatcher("getRecruiterEnterpriseNumber.jsp").forward(request, response);*/
        
        doPost(request, response);
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
        String recruiterEnterpriseNr = (String)request.getSession().getAttribute("recruiterEnterpriseNr");
        
        if (recruiterEnterpriseNr == null){
            recruiterEnterpriseNr = request.getParameter("recruiterEnterpriseNr");
        }
        
        System.out.println("Recruiter Enterprise Nr: " + recruiterEnterpriseNr);
        List<String> vacancyTypesList = new ArrayList<>();

        List<Vacancy> allRecruiterVacancies;
        HttpSession session = request.getSession();

        try {
            allRecruiterVacancies = this.vacancyHandler.getAllVacancies(recruiterEnterpriseNr);
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "Failed to get vacancies published from the database.");
            ex.printStackTrace(System.err);
            response.sendError(500);
            return;
        } catch (ClassNotFoundException ex) {
            session.setAttribute("exceptionTitle", "Internal Server Error");
            session.setAttribute("exceptionMsg", "A server error occured. Please notify the developer at developer.tk_kujwane@outlook.com");
            response.sendError(500);
            return;
        }

        try {
            session.setAttribute("recruiterName",
                    new RecruiterHandler(
                            new DatabaseManager()
                    ).getRecruiter(recruiterEnterpriseNr).getEnterpriseName()
            );
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "Failed to get recruiter name from the database.");
            response.sendError(500);
            return;
        } catch (ClassNotFoundException ex) {
            session.setAttribute("exceptionTitle", "Internal Server Error");
            session.setAttribute("exceptionMsg", "A server error occured. Please notify the developer at developer.tk_kujwane@outlook.com");
            response.sendError(500);
            return;
        }

        for (Vacancy vacancy : allRecruiterVacancies) {
            try {
                vacancyTypesList.add(this.vacancyHandler.getVacancyType(vacancy.getVacancyTypeId()));
            } catch (SQLException ex) {
                session.setAttribute("exceptionTitle", "Database Error");
                session.setAttribute("exceptionMsg", "Failed to get vacancy types from the database.");
                response.sendError(500);
                return;
            }
        }
        
        session.setAttribute("vacancyTypes", vacancyTypesList);
        session.setAttribute("vacanciesList", allRecruiterVacancies);
        response.sendRedirect("recruiterDashboard.jsp");
    }

}
