/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.user.User;

/**
 *
 * @author T Kujwane
 */
public class AddRecruiterServlet extends HttpServlet {

    private DatabaseManager dbManager;

    public AddRecruiterServlet() throws ClassNotFoundException, SQLException {
        this.dbManager = new DatabaseManager();
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
            out.println("<title>Servlet AddRecruiterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddRecruiterServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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

        try {
            String enterpriseNr = getParameter("recruiterEnterpriseNr", request);
            String name = getParameter("recruiterEnterpriseName", request);
            String email = getParameter("recruiterEnterpriseEmail", request);
            String phone = getParameter("recruiterEnterprisePhone", request);

            String query = "INSERT INTO recruiter VALUES(?,?,?,?)";
            PreparedStatement ps = this.dbManager.prepareStatement(query);

            ps.setString(1, enterpriseNr);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);

            ps.execute();
            User currentUser = (User) request.getSession().getAttribute("user");
            request.getSession().setAttribute("recruiterEnterpriseNr", enterpriseNr);
            this.dbManager.executeUpdate("UPDATE users SET enterprise_nr = \'" + enterpriseNr + "\' WHERE "
                    + "email_address = \'" + 
                    currentUser.getEmailAddress() + "\';"
            );
            
            request.getRequestDispatcher("GetVacancyApplicationsServlet").forward(request, response);
        } catch (SQLException ex) {
            HttpSession session = request.getSession();
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "The system is currently unable to add a new recruiter due to a database error.");
            response.sendError(500);
            ex.printStackTrace(System.err);
        }/* catch (ClassNotFoundException ex) {
            HttpSession session = request.getSession();
            session.setAttribute("exceptionTitle", "Server Error");
            session.setAttribute("exceptionMsg", "The system is currently unable to add a new recruiter due to an unidentified system object.");
            response.sendError(500);
            ex.printStackTrace(System.err);
        }*/
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

    private String getParameter(String paramenterName, HttpServletRequest request) {
        return request.getParameter(paramenterName);
    }

}
