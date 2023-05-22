<%-- 
    Document   : loginPage
    Created on : 08 Mar 2023, 14:11:15
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login </title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>Login</h1>
        <%String errorMsg = (String) session.getAttribute("loginErrorMsg");%>
        
        <%if (errorMsg != null){%>
            <p style="color: red; font-size: 20px;">
                <%=errorMsg%><br/>
            </p>
        <%}%>
        
        <form action="LogInServlet" method="POST">
            <table>
                <th colspan="2" style="text-align: center;">
                    Enter your credentials to login.
                </th>

                <tr>
                    <td>User email</td>
                    <td><input type="text" name="userEmail" required=""></td>
                </tr>
                <tr>
                    <td>Login password</td>
                    <td><input type="password" name="loginPassword" required=""></td>
                </tr>

                <tr>
                    <td colspan="2" class="submitBtn"><input type="submit" value="Login"></td>
                </tr>
            </table>
        </form>

    </body>
</html>
