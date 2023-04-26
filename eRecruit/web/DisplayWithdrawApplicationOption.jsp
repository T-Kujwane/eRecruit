<%-- 
    Document   : DisplayWithdrawApplicationOption
    Created on : 26 Apr 2023, 07:32:17
    Author     : Chocolate
--%>

<%@page import="za.ac.tut.application.Application"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>select the vacancy reference number that you want to delete</p>
        <form action="WithdrawParticularAppServlet.do" method="POST">
            <table> 
                <tr bgcolor="lightgrey">
                    <td>
                        Identity Number:
                    </td>
                    <td>
                        ${identityNumber}
                    </td>
                </tr>
                <%

                    ArrayList<Application> applications = (ArrayList<Application>) session.getAttribute("applications");

                    if (applications.size() >= 0) {

                        for (int i = 0; i < applications.size(); i++) {


                %>
                <tr bgcolor = "lightblue">
                    <td >
                        Vacancy reference Number:
                    </td>
                    <td>
                        <%=applications.get(i).getVacancyReferenceNumber()%>
                    </td>

                </tr>

                <tr bgcolor = "lightgrey">

                    <td>
                        Vacancy Type:
                    </td>
                    <td>
                        <%=applications.get(i).getVacancyType()%>
                    </td>

                </tr>
                <tr bgcolor = "lightblue">
                    <td>
                        Date of submission:
                    </td>
                    <td>
                        <%=applications.get(i).getDateQualified()%>
                    </td>
                </tr>
                <tr bgcolor = "lightgrey">
                    <td>
                        Closing Date:
                    </td>
                    <td>
                        <%=applications.get(i).getClosingDate()%>
                    </td>
                </tr>
               
                <%
                    }

                %>
                <%        }
                %>
                
                <tr>
                    <td>
                        <select name="vacancy">
                            <%

                    
                    if (applications.size() >= 0) {

                        for (int i = 0; i < applications.size(); i++) {


                %>
                            <option value=<%=applications.get(i).getVacancyReferenceNumber()%>>
                                <%=applications.get(i).getVacancyReferenceNumber()%>
                            </option>
                  <%}
}%>
                        </select>
                    </td>
                </tr>
                
                <tr>
                    <td>

                    </td>
                    <td>
                        <input type="submit" value="SUBMIT">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
