<%-- 
    Document   : getUserId
    Created on : 25 Apr 2023, 15:49:44
    Author     : Chocolate
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <p>
            Please enter your password below to view your application history
        </p>
        <form action="ViewApplicationWithdrawalServlet.do" method="POST">
            <table>
                <tr>
                    <td>
                        Identity Number(ID)
                    </td>
                    <td>
                        <input type="text" name="identityNumber">
                    </td>
                </tr>
                <tr>
                    <td>
                        
                    </td>
                    <td>
                        <input type="submit" value="ENTER">
                    </td>
                </tr>
            </table>
        </form>
    
    </body>
</html>
