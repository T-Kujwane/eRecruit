<%-- 
    Document   : addUser
    Created on : 22 May 2023, 19:35:53
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add User Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>New User</h1>
        
        <p>
            Kindly fill out the form below to register.<br>
        </p>
        <br>
        
        <form action="AddUserServlet" method="POST">
            <table>
                <th colspan="2">User Login Information</th>
                <tr>
                    <td colspan="2">Fill in the details that you will use when logging into the system</td>
                </tr>
                
                <tr>
                    <td>Email Address</td>
                    <td><input type="text" name="emailAddress" required=""></td>
                </tr>
                
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password" required=""></td>
                </tr>
                
                <tr>
                    <td>Confirm Password</td>
                    <td><input type="password" name="passwordConfirmation"></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Add" onclick="validatePassword()"></td>
                </tr>
            </table>
        </form>
        
        <script src="scripts/functions.js">
    </body>
</html>
