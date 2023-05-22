/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
public class LogInServlet extends HttpServlet {

    private final DatabaseManager dbManager;

    public LogInServlet() throws ClassNotFoundException, SQLException {
        super();
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
            out.println("<title>Servlet LogInServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LogInServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("loginPage.jsp").forward(request, response);
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
        String email = request.getParameter("userEmail");
        String logInPassword = request.getParameter("loginPassword");
        HttpSession session = request.getSession();

        ResultSet rs;

        try {
            rs = this.dbManager.executeQuery("SELECT * FROM users WHERE email_address = \'" + email + "\';");
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Error Validating Credentials");
            session.setAttribute("exceptionMsg",
                    "The system encountered a database error while trying to validate the provided credentials.<br>"
                    + "Our developers are working on this issue.");
            response.sendError(500);
            return;
        }

        boolean resultSetHasData = false;

        try {
            if (rs.isBeforeFirst()) {
                rs.next();
                resultSetHasData = true;
            } else {
                session.setAttribute("loginErrorMsg",
                        "Unkown Email Address Provided. Please consider creating a profile by clicking "
                        + "<a href=\"addUser.jsp\">here</a>");
            }
        } catch (SQLException ex) {
            session.setAttribute("exceptionTitle", "Database Error");
            session.setAttribute("exceptionMsg", "An unknown database error has occured and the development team has been notified.<br/>"
                    + "The error will be resolved soon.");
            response.sendError(500);
            ex.printStackTrace(System.err);
            return;
        }

        if (resultSetHasData) {
            try {
                String dbPassword = rs.getString("login_password");
                
                if (dbPassword.equals(logInPassword)) {
                    String userType = rs.getString("user_type");
                    session.setAttribute("user", new User(email, userType));

                    if (userType.equalsIgnoreCase("recruiter")) {
                        session.setAttribute("recruiterEnterpriseNr", rs.getString("enterprise_nr"));
                        request.getRequestDispatcher("GetVacancyApplicationsServlet").forward(request, response);
                        return;
                    }
                    request.getRequestDispatcher("ApplicantDashboardServlet").forward(request, response);
                    return;
                }
                session.setAttribute("loginErrorMsg", "Incorrect Password Provided");
            } catch (SQLException ex) {
                session.setAttribute("exceptionTitle", "Database Error");
                session.setAttribute("exceptionMsg", "An unknown database error has occured and the development team has been notified.<br/>"
                        + "The error will be resolved soon.");
                response.sendError(500);
                ex.printStackTrace(System.err);
                return;
            }
        }
        response.sendRedirect("loginPage.jsp");
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
