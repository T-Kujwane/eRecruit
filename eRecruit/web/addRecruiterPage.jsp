<%-- 
    Document   : addRecruiterPage
    Created on : 24 May 2023, 08:46:55
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Recruiter Add Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <h1>Add Recruiter</h1>

        <form action="AddRecruiterServlet" method="POST">
            <table id="addRecruiterTbl">
                <th colspan="2" style="text-align: center;">Recruiter Information</th>
                <tr>
                    <td>Enterprise Number</td>
                    <td><input name="recruiterEnterpriseNr" type="text" maxlength="30" ></td>
                </tr>
                <tr>
                    <td>Enterprise Name</td>
                    <td><input name="recruiterEnterpriseName" type="text" maxlength="50" ></td>
                </tr>
                <tr>
                    <td>Enterprise Email</td>
                    <td><input name="recruiterEnterpriseEmail" type="text" maxlength="50" ></td>
                </tr>
                <tr>
                    <td>Enterprise Phone Number</td>
                    <td><input name="recruiterEnterprisePhone" type="text" maxlength="10" ></td>
                </tr>
                
                <tr>
                    <td colspan="2" class="submitBtn"><input type="submit" value="Add"></td>
                </tr>
            </table>
        </form>
        <script src="scripts/functions.js"></script>
    </body>
</html>
