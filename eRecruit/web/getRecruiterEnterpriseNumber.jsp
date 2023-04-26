<%-- 
    Document   : getRecruiterEnterpriseNumber
    Created on : 26 Apr 2023, 06:57:47
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Recruiter Entry Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css">
    </head>
    <body>
        <h1>Organization Enterprise Number</h1>
        Enter your institution's enterprise number below.
        
        <form action="<%= (String) session.getAttribute("postingURL")%>" method="POST">
            <table>
                <th>Recruiter enterprise number</th>
                <tr>
                    <td>
                        <input type="text" required="" name="enterpriseNr">
                    </td>
                </tr>
                <tr><td class="submitBtn"><input type="submit" value="Submit"></td></tr>
            </table>
        </form>
    </body>
</html>
