<%-- 
    Document   : displayDetails
    Created on : 14 Mar 2023, 00:42:53
    Author     : My HP
--%>

<%@page import="java.util.List"%>
<%@page import="za.ac.tut.application.Applicant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Details</title>
    </head>
    <body>
         <header>
            <h1>Profile</h1>
        </header>
          <%Applicant applicant = (Applicant) session.getAttribute("applicant");%>
        
          <form action="userEntry.jsp" method="POST">
            <table>
                <th>
                    <%= applicant.getFirstName() + "\t"%>
                    <%if (applicant.getMiddleName() != null) {%>
                    <%=applicant.getMiddleName() + "\t"%>
                    <%}%>
                    <%=applicant.getSurname()%>
                </th>

                <tr>
                    <td>First name(s)</td>
                    <td>
                        <%= applicant.getFirstName() + "\t"%>
                        <%if (applicant.getMiddleName() != null) {%>
                            <%=applicant.getMiddleName() + "\t"%>
                        <%}%>
                    </td>
                </tr>

                <tr>
                    <td>Surname</td>
                    <td><%=applicant.getSurname()%></td>
                </tr>

                <tr>
                    <td>Email address</td>
                    <td><%=applicant.getEmail()%></td>
                </tr>

                <tr>
                    <td>Phone number</td>
                    <td><%=applicant.getPhoneNumber()%></td>
                </tr>

                <tr>
                    <%List<String> preferedVacancies = applicant.getPreferredVacancies();%>
                    <td>Vacancies Looking For</td>
                    <td>
                        <ol>
                            <%for (String vacancy : preferedVacancies) {%>
                                <li><%=vacancy%></li>
                            <%}%>
                        </ol>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="UPDATE DETAILS"></td>
                </tr>
            </table>
        </form>
                        
    </body>
</html>
