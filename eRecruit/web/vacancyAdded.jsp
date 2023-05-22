<%-- 
    Document   : vacancyAdded
    Created on : 28 Mar 2023, 15:21:09
    Author     : T Kujwane
--%>

<%@page import="za.ac.tut.recruiter.Recruiter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmation Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>Vacancy Added!</h1>
        <p>
            <div class="tickImg">
                <img class="tickImg" src="images/tick.png" alt="tick"/>
            </div>
            Your vacancy has been successfully added. Click 
            <a href="GetVacancyApplicationsServlet?recruiterEnterpriseNr=<%=((Recruiter) session.getAttribute("recruiter")).getEnterpriseNumber()%>">here</a> to go back to the dashboard page.
        </p>
    </body>
</html>
