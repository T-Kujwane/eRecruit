<%-- 
    Document   : comingSoon
    Created on : 23 Mar 2023, 14:14:40
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=session.getAttribute("pageTitle")%></title>
    </head>
    <body>
        <h1><%=session.getAttribute("pageHeader")%></h1>
        <h2>Functionality Coming Soon!</h2>
        <p>
            This functionality is still under development. <br/>
            Click <a href="index.html">here</a> to navigate to the home page.
        </p>
    </body>
</html>
