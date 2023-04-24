/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package za.ac.tut.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.database.manager.DatabaseManager;

/**
 *
 * @author My HP
 */
public class UpdateProTwoServlet extends HttpServlet {

    final private DatabaseManager db;
    
    String url = "jdbc:mysql://localhost:3306/recruitment_db?useSSL=false";
    String userName  = "root";
    String password = "root";
    
    public UpdateProTwoServlet()throws ClassNotFoundException,IOException, SQLException {
 
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
            out.println("<title>Servlet UpdateProTwoServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProTwoServlet at " + request.getContextPath() + "</h1>");
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
        
        String firstName = request.getParameter("firstName");
        String middileName = request.getParameter("middleName");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        
        String idNumber = (String)session.getAttribute("idNumber");
        
        System.out.println(idNumber);
        
         //FIRST_NAME, MIDDLE_NAME, SURNAME, EMAIL_ADDRESS, PHONE_NR;
        try {
            //Execute Update statements
            db.executeUpdate(updateSet("FIRST_NAME", firstName, idNumber));
            db.executeUpdate(updateSet("MIDDLE_NAME", middileName, idNumber));
            db.executeUpdate(updateSet("SURNAME", surname, idNumber));
            db.executeUpdate(updateSet("EMAIL_ADDRESS", email, idNumber));
            db.executeUpdate(updateSet("PHONE_NR", phoneNumber, idNumber));
        } catch (SQLException ex) {
            Logger.getLogger(UpdateProTwoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       RequestDispatcher disp = request.getRequestDispatcher("confirmed.jsp");
       disp.forward(request, response);
        
    }
    
    private String updateSet(String column,String newValue,String idNumber){
        
        String query = "";
        
        query = "UPDATE applicant"+
                "SET " + "\'" + column + "\'" +"="+ "\'" + newValue + "\'"+
                "WHERE applicant_id = " + "\'" + idNumber + "\';";
     
        
        return query;
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
        processRequest(request, response);
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
