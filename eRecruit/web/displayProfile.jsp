<%-- 
    Document   : displayProfile
    Created on : 10 Mar 2023, 11:16:44
    Author     : T Kujwane
--%>

<%@page import="java.util.List"%>
<%@page import="za.ac.tut.application.Applicant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Profile</title>
    </head>
    <body>
        <header>
            <h1>Profile</h1>
        </header>
        <%Applicant applicant = (Applicant) session.getAttribute("applicant");%>

        <form action="DeleteProfileServlet" method="POST">
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
                    <td colspan="2" style="text-align: right;"><input type="submit" value="Delete profile"></td>
                </tr>
            </table>
        </form>

    </body>
</html>
