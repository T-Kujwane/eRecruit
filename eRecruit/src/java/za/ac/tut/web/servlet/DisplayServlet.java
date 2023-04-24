/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Applicant;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.enums.ApplicantFields;

/**
 *"jdbc:mysql://localhost:3306/recruitment_db?useSSL=false", "root", "021121ZWELISHa_"
 * @author My HP
 */
public class DisplayServlet extends HttpServlet {

    private DatabaseManager db;
    
    String url = "jdbc:mysql://localhost:3306/recruitment_db?useSSL=false";
    String userName  = "root";
    String password = "root";
    
    public DisplayServlet() throws ClassNotFoundException,IOException, SQLException{
        
        super();
        this.db = new DatabaseManager(url, userName, password);
    }
    
    
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
            out.println("<title>Servlet DisplayServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DisplayServlet at " + request.getContextPath() + "</h1>");
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
        
        String idNumber = (String)request.getParameter("idNumber");
        
        

        session.setAttribute("idNumber", idNumber);

        String query = "SELECT * FROM applicant WHERE applicant_id = \'" + idNumber + "\';";

        ResultSet resultSet;
        Applicant applicant;

        try {
            resultSet = db.executeQuery(query);
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
                applicant = new Applicant(idNumber, firstName, middleName, surname, phoneNr, emailAddress);
            } else {
                //response.sendRedirect("deleteError.jsp");
                //RequestDispatcher disp = request.getRequestDispatcher("delete.jsp");
                System.out.println("Error 1\n" + "Result set: " + resultSet);
                return;
            }
        } catch (SQLException ex) {
            System.err.println("Unable to get applicant details from result set");
            return;
        }

        query = "SELECT vacancy_type FROM vacancy_type vt, prefered_vacancy_type pf WHERE vt.vacancy_type_id = pf.vacancy_type_id AND pf.applicant_id = \'" + idNumber + "\';";

        try {
            resultSet = db.executeQuery(query);
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

        //response.sendRedirect("displayDetails.jsp");
        RequestDispatcher disp = request.getRequestDispatcher("displayDetails.jsp");
        disp.forward(request, response);
    }
        
        //DatabaseManager dm = new DatabaseManager(idNumber, idNumber, idNumber)
    

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
        
        String idNumber = (String)request.getParameter("idNumber");
        
        

        session.setAttribute("idNumber", idNumber);

        String query = "SELECT * FROM applicant WHERE applicant_id = \'" + idNumber + "\';";

        ResultSet resultSet;
        Applicant applicant;

        try {
            resultSet = db.executeQuery(query);
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
                applicant = new Applicant(idNumber, firstName, middleName, surname, phoneNr, emailAddress);
            } else {
                //response.sendRedirect("deleteError.jsp");
                //RequestDispatcher disp = request.getRequestDispatcher("delete.jsp");
                System.out.println("Error 1");
                return;
            }
        } catch (SQLException ex) {
            System.err.println("Unable to get applicant details from result set");
            return;
        }

        query = "SELECT vacancy_type FROM vacancy_type vt, prefered_vacancy_type pf WHERE vt.vacancy_type_id = pf.vacancy_type_id AND pf.applicant_id = \'" + idNumber + "\';";

        try {
            resultSet = db.executeQuery(query);
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

        //response.sendRedirect("displayDetails.jsp");
        RequestDispatcher disp = request.getRequestDispatcher("displayDetails.jsp");
        disp.forward(request, response);
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

private String getApplicantData(ApplicantFields field, ResultSet results) throws SQLException {
        return (String) results.getObject(field.name().toLowerCase());
    }

}
