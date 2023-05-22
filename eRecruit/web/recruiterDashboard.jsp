<%-- 
    Document   : recruiterDashboard
    Created on : 22 Apr 2023, 16:48:54
    Author     : T Kujwane
--%>

<%@page import="java.util.List"%>
<%@page import="za.ac.tut.vacancy.Vacancy"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <%List<Vacancy> recruiterVacancies = (List<Vacancy>) session.getAttribute("vacanciesList");%>
        <%String recruiter = (String) session.getAttribute("recruiterName");%>
        <%List<String> vacancyTypes = (List<String>) session.getAttribute("vacancyTypes");%>
        
        <h1><%= recruiter%> Dashboard</h1>
        
        <%if (!recruiterVacancies.isEmpty()){%>
           <p>
               <table>
                    <th colspan="5">Vacancies Published By <%=recruiter%></th>
                    <tr>
                        <td>Reference Number</td>
                        <td>Vacancy Description</td>
                        <td>Closing Date</td>
                        <td>Vacancy Type</td>
                    </tr>
                    <%for (Vacancy recruiterVacancy : recruiterVacancies){%>
                        <tr>
                            <td><%=recruiterVacancy.getReferenceNr()%></td>
                            <td><%=recruiterVacancy.getDescription()%></td>
                            <td><%=recruiterVacancy.getClosingDate()%></td>
                            <td><%=vacancyTypes.get(recruiterVacancies.indexOf(recruiterVacancy))%></td>
                            <td>
                                <a href="WithdrawVacancyServlet?vacancyReferenceNr=<%=recruiterVacancy.getReferenceNr()%>">Withdraw</a>
                            </td>
                        </tr>
                    <%}%>
               </table>
            </p> 
        <%}else {%>
            <%=recruiter%> has not yet published any vacancies.
        <%}%>
        Click <a href="index.html">here</a> to navigate to the home page.
    </body>
</html>
