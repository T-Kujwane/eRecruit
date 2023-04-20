<%-- 
    Document   : confirmation
    Created on : 10 Mar 2023, 15:32:34
    Author     : T Kujwane
--%>

<%@page import="za.ac.tut.application.Applicant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmation Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>Account Deleted Successfully</h1>
        
        <%Applicant applicant = (Applicant) session.getAttribute("applicant");%>
        
        <p>
            We are sad to see you go <%=applicant.getFirstName()%>. Nonetheless, your profile has been deleted. <br/>
            Click <a href="index.html">here</a> to navigate back home.
        </p>
    </body>
</html>
