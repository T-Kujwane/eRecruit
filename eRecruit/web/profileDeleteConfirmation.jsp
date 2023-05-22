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
        <nav style="text-align: right;">
            <a href="logoutPage.jsp">Sign-out</a>
        </nav>
        <h1>Account Deleted Successfully</h1>
        
        <%Applicant applicant = (Applicant) session.getAttribute("applicant");%>
        
        <p>
            <div class="recycleBinImg">
                <img src="images/empty-recycle-bin.png" alt="recycle bin"/>
            </div>
            We are sad to see you go <%=applicant.getFirstName()%>. Nonetheless, your profile has been deleted.<br>
            <br><br>
            <div class="sadImg">
                <img src="images/sadImage.jpg" alt="sad image" width="20%"/>
            </div>
        </p>
    </body>
</html>
