/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.handler.VacancyHandler;

/**
 *
 * @author T Kujwane
 */
public class WithdrawVacancyServlet extends HttpServlet {

    private DatabaseManager dbManager;
    private VacancyHandler vacancyHandler;

    public WithdrawVacancyServlet() throws ClassNotFoundException, SQLException {
        super();
        this.dbManager = new DatabaseManager();
        this.vacancyHandler = new VacancyHandler(dbManager, new EmailSessionBean());
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
        String vacancyRefNr = request.getParameter("vacancyReferenceNr");
        HttpSession session = request.getSession();

        try {
            this.vacancyHandler.withdrawVacancy(vacancyRefNr);
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "Failed to delete the vacancy from the database.");
            ex.printStackTrace(System.err);
            response.sendError(500);
        } catch (MessagingException ex) {
            session.setAttribute("exceptionTitle", "Messaging Error");
            session.setAttribute("exceptionMsg", "Failed to notify applicants that the vacancy is deleted.");
            response.sendError(500);
        }
        
        response.sendRedirect("vacancyDeleted.jsp");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vacancyRefNr = request.getParameter("vacancyReferenceNr");
        HttpSession session = request.getSession();

        try {
            this.vacancyHandler.withdrawVacancy(vacancyRefNr);
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "Failed to delete the vacancy from the database.");
            ex.printStackTrace(System.err);
            response.sendError(500);
        } catch (MessagingException ex) {
            session.setAttribute("exceptionTitle", "Messaging Error");
            session.setAttribute("exceptionMsg", "Failed to notify applicants that the vacancy is deleted.");
            response.sendError(500);
        }
        
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
