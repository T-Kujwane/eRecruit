<%-- 
    Document   : recruiterMenu
    Created on : 26 Apr 2023, 10:40:38
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
        <h1>Menu</h1>
        <table>
            <th>Navigation</th>
            <tr>
                <td>
                    <nav id="recruiter_navigation">
                        <ul>
                            <li><a href="PublishVacancyServlet">Publish Vacancy</a></li>
                            <li><a id="GetVacancyApplicationsServlet" href="GetVacancyApplicationsServlet" >View Vacancy Applications</a></li>
                            <li><a id="WithdrawVacancyServlet" href="WithdrawVacancyServlet">Withdraw Vacancy</a></li>
                        </ul>
                    </nav>
                    </td>
            </tr>
        </table>
    </body>
</html>
