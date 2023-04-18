<%-- 
    Document   : userEntry
    Created on : 27 Mar 2023, 09:40:33
    Author     : My HP
--%>

<%@page import="za.ac.tut.application.Applicant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Entry Page</title>
    </head>
    <body>
        <h1>UPDATE DETAILS</h1>
        <%Applicant applicant = (Applicant) session.getAttribute("applicant");
            String name = applicant.getFirstName();
           // applicant.getPhoneNumber()
        %>

        <form action="UpdateProTwoServlet.do">
            <table>
                <tr>
                    <td>First Name:</td>
                    <td><input type="text" name="firstName" value=<%=name%>></td>
                </tr></br>
                <tr>
                    <td>Middle Name:</td>
                    <td><input type="text" name="middleNmeame" value=<%=applicant.getMiddleName()%>></td>
                </tr></br><!-- comment -->
                <tr>
                    <td>Surname:</td>
                    <td><input type="text" name="surname" value=<%=applicant.getSurname()%>></td>
                </tr></br>
                <tr>
                    <td>Email:</td>
                    <td><input type="text" name="email" value=<%=applicant.getEmailAddress()%>></td>
                </tr></br>
                <tr>
                    <td>Phone number:</td>
                    <td><input type="text" name="phoneNumber" value=<%=applicant.getPhoneNumber()%>></td>
                </tr></br>
                <tr>
                    <td></td>
                    <td><input type="submit" value="SUBMIT DETAILS"></td>
                </tr>
            </table>
        </form>
    </body>
</html>
