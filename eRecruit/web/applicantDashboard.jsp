<%-- 
    Document   : applicantDashboard
    Created on : 25 May 2023, 11:41:52
    Author     : T Kujwane
--%>

<%@page import="za.ac.tut.vacancy.Vacancy"%>
<%@page import="za.ac.tut.qualification.Qualification"%>
<%@page import="java.util.List"%>
<%@page import="za.ac.tut.application.Applicant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css"/>
    </head>
    <body>
        <%
            Applicant applicant = (Applicant) session.getAttribute("applicant");
            List<Qualification> applicantQualifications = applicant.getApplicantQualifications();
            List<String> applicantSkills = applicant.getSkills();
            List<String> preferedVacancies = applicant.getPreferredVacancyTypes();

            String applicantFullNames = applicant.getFirstName();

            if (applicant.getMiddleName() != null) {
                applicantFullNames += " " + applicant.getMiddleName();
            }

            applicantFullNames += " " + applicant.getSurname();

            List<Vacancy> applicantQualifyingVacancies = (List<Vacancy>) session.getAttribute("qualifyingVacancies");
            List<String> vacancyTypes = (List<String>) session.getAttribute("vacancyTypes");
        %>
        
        <nav style="text-align: right;">
            <a href="logoutPage.jsp">Sign-out</a>
        </nav>

        <h1>Dashboard</h1>

        <nav class="top_navigations_container">
            <table>
                <tr>
                    <td class="top_navigation"><a href="DisplayServlet.do?idNumber=<%=applicant.getApplicantID()%>">Update Profile</a></td>
                    <td class="top_navigation"><a href="DeleteProfileServlet?id=<%=applicant.getApplicantID()%>">Delete Profile</a></td>
                    <td class="top_navigation">Assess Applicants</td>
                    <td class="top_navigation">View Vacancy Application</td>
                </tr>
            </table>
        </nav>

        <h2 style="text-align: center">Profile</h2>
        
        <table>
            <tr>
                <td>Full Names</td>
                <td><%=applicantFullNames%></td>
            </tr>
            <tr>
                <td>Identity Number</td>
                <td><%=applicant.getApplicantID()%></td>
            </tr>
            <tr>
                <td>Qualifications</td>
                <td>
                    <%if (!applicantQualifications.isEmpty()) {%>
                        <ol>
                            <%for (Qualification applicantQualification : applicantQualifications) {%>
                                <%
                                    String qualification = applicantQualification.toString();
                                    
                                    if (qualification.toLowerCase().contains("matric")){
                                        qualification = qualification.split(" in ")[0];
                                    }
                                %>
                                
                                <li><%=qualification%></li>
                            <%}%>
                        </ol>
                    <%} else {%>
                        Applicant has no qualifications yet.
                    <%}%>
                </td>
            </tr>

            <tr>
                <td>Skills Possessed</td>
                <td>
                    <%if (!applicantSkills.isEmpty()) {%>
                        <ol>
                            <%for (String applicantSkill : applicantSkills) {%>
                                <li><%=applicantSkill.toString()%></li>
                            <%}%>
                        </ol>
                    <%} else {%>
                        Applicant has no skills yet.
                    <%}%>
                </td>
            </tr>
        
            <tr>
                <td>Preferred Vacancies</td>
                <td>
                    <ol>
                        <%for(String preferredVacancy : preferedVacancies){%>
                            <li><%=preferredVacancy%></li>
                        <%}%>
                    </ol>
                </td>
            </tr>
        
            <tr>
                <td>Contact Details</td>
                <td>Phone: <%=applicant.getPhoneNumber()%> &emsp;&emsp; Email: <%=applicant.getEmailAddress()%></td>
            </tr>
        </table>
            
        <hr>
        
        <h2 style="text-align: center;">Vacancies Qualified For</h2>
        <%if(!applicantQualifyingVacancies.isEmpty()){%>
            <table>
                <tr>
                    <td>Reference Number</td>
                    <td>Vacancy Description</td>
                    <td>Closing Date</td>
                    <td>Vacancy Type</td>
                    
                </tr>
                
                <%for (Vacancy qualifyingVacancy : applicantQualifyingVacancies){%>
                    <td><%=qualifyingVacancy.getReferenceNr()%></td>
                    <td><%=qualifyingVacancy.getDescription()%></td>
                    <td><%=qualifyingVacancy.getClosingDate()%></td>
                    <td><%=vacancyTypes.get(applicantQualifyingVacancies.indexOf(qualifyingVacancy))%></td>
                    <td><a href="WithdrawApplicationServlet?vacancyReferenceNr=<%=qualifyingVacancy.getReferenceNr()%>">Withdraw Application</a></td>
                <%}%>
            </table>
        <%}else{%>
            You do not qualify for any vacancies yet.<br><br>
            <div class="sadImg">
                <img src="images/sadImage.jpg" alt="Sad Image" width="15%"/>
            </div>
        <%}%>
        <script src="scripts/functions.js"></script>
    </body>
</html>
