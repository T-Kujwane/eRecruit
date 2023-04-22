<%-- 
    Document   : deleteError
    Created on : 10 Mar 2023, 16:44:10
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile Deletion Error</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>Profile Not Deleted</h1>
        
        <p>
            An applicant with the ID Number: <%=session.getAttribute("applicantID")%> does not exist.<br/>
            <a href="index.html">Go Home</a>
        </p>
    </body>
</html>
