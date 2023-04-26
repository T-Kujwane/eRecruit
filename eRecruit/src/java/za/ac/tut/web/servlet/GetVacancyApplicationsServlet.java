/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        request.getSession().setAttribute("postingURL", getClass().getSimpleName());
        
        request.getRequestDispatcher("getRecruiterEnterpriseNumber.jsp").forward(request, response);
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
        String enterpriseNr = request.getParameter("enterpriseNr");
        try {
            request.getSession().setAttribute("recruiterName", this.recruiterHandler.getRecruiter(enterpriseNr).getEnterpriseName());
        } catch (SQLException ex) {
            System.err.println("Unable to get recruiter name from database.");
            ex.printStackTrace(err);
            response.sendError(500, "A database error occured while trying to get rectuiter name from database.");
            return;
        }
        
        List<Vacancy> recruiterVacancies;
        try {
            recruiterVacancies = this.vacancyHandler.getAllVacancies(enterpriseNr);
        } catch (SQLException ex) {
            System.err.println("Unable to get vacancies due to a database issue that has occured.");
            response.sendError(500, "A database issue has occured.");
            ex.printStackTrace(err);
            return;
        } catch (ClassNotFoundException ex) {
            String message = "Unable to get vacancies due to an unkonwn entity.";
            System.err.println(message);
            ex.printStackTrace(err);
            response.sendError(500, message);
            return;
        }
        
        request.getSession().setAttribute("vacanciesList", recruiterVacancies);
        
        response.sendRedirect("recruiterDashboard.jsp");
    }

}
