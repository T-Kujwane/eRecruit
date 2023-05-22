<%-- 
    Document   : applicationWithdrawnConfirmation
    Created on : 26 May 2023, 10:23:53
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmation Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <nav style="text-align: right;">
            <a href="logoutPage.jsp">Sign-out</a>
        </nav>
        <h1>Application Withdrawal Successful</h1>
        
        <p>
            <div class="recycleBinImg">
                <img src="images/empty-recycle-bin.png" alt="Recycle Bin Image"/>
            </div>
            
            Your application has been successfully withdrawn.<br>
            Click <a href="ApplicantDashboardServlet">here</a> to navigate back to the dashboard.
        </p>
    </body>
</html>
