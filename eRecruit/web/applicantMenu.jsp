<%-- 
    Document   : applicantMenu
    Created on : 26 Apr 2023, 10:34:50
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css" type="text/css">
    </head>
    <body>
        <nav style="text-align: right; font-size: 20px;">
            <a href="logoutPage.jsp">Sign out</a>
        </nav>
        
        <h1>Menu</h1>
        <p>
            Use the navigation links below to perform various actions.
        </p>

        <table>
            <th>Navigation</th>
            <tr>
                <td>
                    <nav id="applicant_navigations">
                        <ul>
                            <li><a href="ApplicationServlet">Apply</a></li>
                            <li><a href="getUserInfo.html">View Application History</a></li>
                            <li><a href="UpdateProServlet.do">Update Profile</a></li>
                            <li><a id="deleteProfile" href="" onclick="getUserID()">Delete Profile</a></li>
                        </ul>
                    </nav>
                </td>
            </tr>
        </table>
        <script src="scripts/functions.js"></script>
    </body>
</html>
