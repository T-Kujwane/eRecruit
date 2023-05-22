<%-- 
    Document   : errorPage
    Created on : 20 May 2023, 15:30:13
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="background-color: black; color: red;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1><%=session.getAttribute("exceptionTitle")%></h1>
        
        <div class="errorGif"> 
            <img class="errorGif" src="images/systemError.gif" alt="System Error Picture"/>
        </div>
        
        <p>
            <%=session.getAttribute("exceptionMsg")%>
        </p>
        
        <script src="scripts/functions.js"></script>
    </body>
</html>
