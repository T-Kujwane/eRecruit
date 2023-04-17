<%-- 
    Document   : updateProfile
    Created on : 13 Mar 2023, 13:45:53
    Author     : My HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Profile</title>
    </head>
    <body>
        <h1>Welcome</h1>
        
        May you please enter your ID number to begin the procedure </br>
        
        <form action="DisplayServlet.do" method="POST">
            <tr>
                <td>Enter ID Number:</td>
                <td><input type="text" name="idNumber" placeholder=""></br></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="ENTER"></td>
            </tr>
        </form>          
    </body>
</html>
