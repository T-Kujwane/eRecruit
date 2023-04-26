/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.viewapplicationhistoryservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.application.Application;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.exception.IdentityNumberExistsException;

/**
 *
 * @author Chocolate
 */
public class ViewApplicationHistoryServlet extends HttpServlet {

    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        

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
            throws ServletException, IOException,IdentityNumberExistsException {

        HttpSession session = request.getSession(true);
        String identityNumber = request.getParameter("identityNumber");
        ArrayList<Application> applications = new ArrayList<>();
        Application application;
        boolean isValid = true;

        try {
            
            DatabaseManager dm = new DatabaseManager("jdbc:mysql://localhost:3306/recruitment_db?useSSL=false", "root", "root");
            String retrieveIdNumber = "select qualifying_applicant.applicant_id "
                    + "from applicant , qualifying_applicant "
                    + "where applicant.applicant_id = qualifying_applicant.applicant_id";
            ResultSet rs1 = dm.executeQuery(retrieveIdNumber);

            while (rs1.next()) {
                if (identityNumber.equalsIgnoreCase(rs1.getString("qualifying_applicant.applicant_id"))) {

                    isValid = true;
                    break;

                } else {
                    isValid = false;
                }

            }

            if (isValid == true) {

                String retrieveAplications = "SELECT applicant.applicant_id,"
                        + "first_name ,"
                        + "date_qualified,"
                        + "vacancy_description,"
                        + "closing_date,"
                        + "vacancy_type,"
                        + "reference_nr "
                        + "from applicant , qualifying_applicant, vacancy,vacancy_type "
                        + "where applicant.applicant_id = qualifying_applicant.applicant_id "
                        + "and reference_nr = vacancy_reference_nr "
                        + "and vacancy.vacancy_type_id = vacancy_type.vacancy_type_id "
                        + "and applicant.applicant_id = ? ";
                PreparedStatement ps = dm.prepareStatement(retrieveAplications);
                ps.setString(1, identityNumber);
                ResultSet rs = ps.executeQuery();

                while (rs.next() == true) {

                    String id = rs.getString("applicant.applicant_id");
                    String first_Name = rs.getString("first_name");
                    Date dateQualified = rs.getDate("date_qualified");
                    Date closingDate = rs.getDate("closing_date");
                    String vacancyDescription = rs.getString("vacancy_description");
                    String vacancyType = rs.getString("vacancy_type");
                    String vacancyReferenceNumber = rs.getString("reference_nr");
                    application = new Application(id, first_Name, vacancyDescription, dateQualified, closingDate, vacancyType, vacancyReferenceNumber);
                    applications.add(application);

                }
                session.setAttribute("identityNumber", identityNumber);
                session.setAttribute("applications", applications);

                RequestDispatcher rd = request.getRequestDispatcher("DisplayApplicationHistory.jsp");
                rd.forward(request, response);

            } else {
                session.setAttribute("identityNumber", identityNumber);
                throw new IdentityNumberExistsException();
           
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewApplicationHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewApplicationHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
