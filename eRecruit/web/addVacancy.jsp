<%-- 
    Document   : addVacancy
    Created on : 11 Mar 2023, 13:36:19
    Author     : T Kujwane
--%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Publish Vacancy Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css">
    </head>
    <body>
        <h1>Vacancy Pushing Page</h1>
        <%
            List<String> vacancyTypes = (List<String>) session.getAttribute("vacancyTypes");
            List<String> qualificationTypes = (List<String>) session.getAttribute("qualificationTypes");
        %>
        <form action="PublishVacancyServlet" method="POST">
            <table>
                <tr>
                    <th colspan="2">Vacancy Details</th>
                </tr>

                <tr>
                    <td>Reference Number</td>
                    <td><input type="text" required="" name="referenceNr"></td>
                </tr>

                <tr>
                    <td>Vacancy type</td>
                    <td>
                        <select name="vacancyType">
                            <%for (String vacancyType : vacancyTypes) {%>
                            <option value="<%=vacancyType%>"><%=vacancyType%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>Required qualification(s)</td>
                    <td>
                        <%for (String qualificationType : qualificationTypes) {%>
                        <input class="checkbox" type="checkbox" id="<%=qualificationType%>" name="requiredQualification" value="<%=qualificationType%>">
                        <label class="checkbox" for="<%=qualificationType%>"> <%=qualificationType%></label>
                            <%if (!qualificationType.contains("NSC")){%> in 
                                <select  name="courseName" id="<%=qualificationType + "dropDown"%>">
                                    <%for (String vacancyType : vacancyTypes) {%>
                                    <option value="<%=vacancyType%>"><%=vacancyType%></option>
                                    <%}%>
                                </select>
                            <%}%>
                        <br/>
                        <%}%>
                    </td>
                </tr>

                <tr>
                    <td>Closing Date</td>
                    <td><input type="date" name="closingDate" required=""></td>
                </tr>

                <tr>
                    <td>Description</td>
                    <td><textarea name="description" cols="50" maxlength="2000"></textarea></td>
                </tr>


                <tr>
                    <td colspan="2" style="text-align: right;"><input type="submit" value="Publish"></td>
                </tr>
            </table>
        </form>
    </body>
</html>
