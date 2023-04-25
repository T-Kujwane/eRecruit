<%-- 
    Document   : DisplayApplicationHistory
    Created on : 24 Apr 2023, 23:16:32
    Author     : Chocolate
--%>

<%@page import="za.ac.tut.application.Application"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table> 
            <tr bgcolor="lightgrey">
                <td>
                    Identity Number:
                </td>
                <td>
                    ${identityNumber}
                </td>
            </tr>
        <%

            ArrayList<Application> applications = (ArrayList<Application>) session.getAttribute("applications");
            
            if(applications.size()>=0){
                
            for (int i = 0; i < applications.size(); i++) {
                        
                    
        %>
        <tr bgcolor = "lightblue">
            <td >
                Vacancy refernce Number:
            </td>
            <td>
                <%=applications.get(i).getVacancyReferenceNumber()%>
            </td>
        
        </tr>
        
        <tr bgcolor = "lightgrey">
            
            <td>
                Vacancy Type:
            </td>
            <td>
                <%=applications.get(i).getVacancyType()%>
            </td>
            
        </tr>
        <tr bgcolor = "lightblue">
            <td>
                Date of submission:
            </td>
            <td>
                <%=applications.get(i).getDateQualified()%>
            </td>
        </tr>
        <tr bgcolor = "lightgrey">
            <td>
                Closing Date:
            </td>
            <td>
                <%=applications.get(i).getClosingDate()%>
            </td>
        </tr>
        
        <%
            }
        
        %>
        <%
            }else{
        %>
        <tr>
            <td>
                No data found
            </td>
        </tr>
        <%
            }
        %>
        
        </table>
        
        <p>Press <a href="index.html">here</a> when done viewing you application history</p>
        
    </body>
</html>
