/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.handler.ApplicantHandler;
import za.ac.tut.handler.QualificationHandler;
import za.ac.tut.handler.SkillHandler;
import za.ac.tut.handler.VacancyHandler;
import za.ac.tut.user.User;

/**
 *
 * @author T Kujwane
 */
public class AddUserServlet extends HttpServlet {
    private final DatabaseManager dbManager;
    private final ApplicantHandler applicantHandler;
    private final VacancyHandler vacancyHandler;
    private final QualificationHandler qualificationHandler;
    private final SkillHandler skillHandler;

    @EJB
    @Inject
    private EmailSessionBean emailSessionBean;

    public AddUserServlet() throws ClassNotFoundException, SQLException {
        this.dbManager = new DatabaseManager();
        this.emailSessionBean = new EmailSessionBean();
        this.applicantHandler = new ApplicantHandler(this.dbManager, this.emailSessionBean);
        this.vacancyHandler = new VacancyHandler(this.dbManager, this.emailSessionBean);
        this.qualificationHandler = new QualificationHandler(dbManager);
        this.skillHandler = new SkillHandler(dbManager);
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
        
        request.getRequestDispatcher("addUser.jsp").forward(request, response);
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
        
        String emailAddress = getParameter("emailAddress", request);
        String loginPassword = getParameter("password", request);
        String loginPasswordConfirmation = getParameter("passwordConfirmation", request);
        
        boolean userIsApplicant = Boolean.parseBoolean(getParameter("userIsApplicant", request).toUpperCase());
        
        String userType = "recruiter";
        
        if (userIsApplicant){
            userType = "applicant";
        }
        
        HttpSession session = request.getSession();
        
        if (loginPassword.equals(loginPasswordConfirmation)) {
            try {
                
                this.dbManager.executeUpdate("INSERT INTO users(email_address, login_password, user_type) "
                        + "VALUES(\'" + emailAddress + "\', \'" + loginPassword + "\', \'" + userType + "\');");
                session.setAttribute("user", new User(emailAddress, userType));
                
                if (userIsApplicant){
                    response.sendRedirect("ApplicationServlet");
                }else {
                    response.sendRedirect("addRecruiterPage.jsp");
                }
                
                return;
            } catch (SQLException ex) {
                session.setAttribute("exceptionTitle", "Database Error");
                session.setAttribute("exceptionMsg", "The system is currently unable to add a new user due to a database error.");
                response.sendError(500);
                ex.printStackTrace(System.err);
                return;
            }
        }
        
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

    private String getParameter(String parameter, HttpServletRequest request) {
        return request.getParameter(parameter);
    }

    private void addSessionAttribute(String attributeName, Object value, HttpSession session) {
        session.setAttribute(attributeName, value);
    }

}
