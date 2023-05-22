<%-- 
    Document   : vacancyDeleted
    Created on : 20 May 2023, 18:13:48
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Confirmation Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>Vacancy Deleted Successfully</h1>
        
        <p>
            <div class="recycleBinImg">
                <img src="images/empty-recycle-bin.png" alt="recycle bin"/>
            </div>
                Your vacancy has been deleted. 
            <br/>
            Click <a href="GetVacancyApplicationsServlet?recruiterEnterpriseNr=<%=(String)session.getAttribute("recruiterEnterpriseNr")%>">
                here</a> to navigate back to the dashboard.
        </p>
    </body>
</html>
