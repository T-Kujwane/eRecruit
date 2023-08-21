<%-- 
    Document   : addApplicantPage
    Created on : 20 Apr 2023, 16:12:17
    Author     : T Kujwane
--%>

<%@page import="za.ac.tut.user.User"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New Applicant Page</title>
        <link rel="stylesheet" type="text/css" href="styles/primaryStyles.css"/>
    </head>
    
    <body>
        <h1>Apply</h1>
        <%
            List<String> vacancyTypes = (List<String>) session.getAttribute("vacancyTypes");
            List<String> qualificationTypes = (List<String>) session.getAttribute("qualificationTypes");
            List<String> courses = (List<String>) session.getAttribute("courses");
            List<String> skills = (List<String>) session.getAttribute("skills");
        %>
        
        <p>
            Please fill in the form to capture your information into the database.
        </p>

        <form action="ApplicationServlet" method="POST" onsubmit="verifyPassword(this)">
            <table>
                <tr>
                    <th colspan="2">Create Profile</th>
                </tr>
                
                <tr>
                    <td>First Name</td>
                    <td><input type="text" name="firstName" required="" placeholder="Thato"></td>
                </tr>
                
                <tr>
                    <td>Middle Name</td>
                    <td><input type="text" name="middleName" placeholder="Keith"></td>
                </tr>
                
                <tr>
                    <td>Surname</td>
                    <td><input type="text" name="surname" required="" placeholder="Kujwane"></td>
                </tr>
                
                <tr>
                    <td>ID. Number</td>
                    <td><input type="text" name="applicantID" required="" placeholder="" maxlength="13"></td>
                </tr>
                
                <tr>
                    <td>Email Address</td>
                    <td><input type="text" name="emailAddress" readonly="" value="<%=((User)session.getAttribute("user")).getEmailAddress()%>"></td>
                </tr>
                
                <tr>
                    <td>Primary Phone Number</td>
                    <td><input type="text" name="phoneNumber" required="" placeholder="" maxlength="10"></td>
                </tr>
                
                <tr>
                    <td>Preferred Vacancies</td>
                    <td>
                        <%for (String vacancyType : vacancyTypes){%>
                            <%String attributeVal = vacancyType.replace(" ", "");%>
                            <input type="checkbox" id="<%=attributeVal + "CheckBox"%>" name="preferredVacancyType" value="<%=vacancyType%>">
                            <label for="<%=attributeVal + "CheckBox"%>"> <%=vacancyType%> </label>
                        <%}%>
                    </td>
                </tr>
                
                <tr>
                    <td>Qualification(s)</td>
                    <td>
                        
                        <%for (String qualificationType : qualificationTypes){%>
                        <%String attributeVal = qualificationType.replace(" ", "");%>
                        
                        <input type="checkbox" id="<%=attributeVal + "CheckBox"%>" name="applicantQualificationType" value="<%=qualificationType%>" onclick="displayDropDown('<%=attributeVal%>')">
                            
                            <label for="<%=attributeVal + "CheckBox"%>"> <%=qualificationType%> </label>
                            
                            <%if (!qualificationType.contains("NSC")){%>
                            
                                <select name="course" id="<%=attributeVal + "DropDown"%>" hidden="">
                                    <option value="null">Choose a course</option>
                                    <%for (String course : courses){%>
                                        <%if (!course.equalsIgnoreCase("Matric Subjects")){%>
                                            <option value="<%=course%>"> <%=course%> </option>
                                        <%}%>
                                    <%}%>
                                    
                                </select>
                                
                            <%}%>
                            
                            <br/>
                            
                        <%}%>
                    </td>
                </tr>
                
                <tr>
                    <td>Skill(s)</td>
                    <td>
                        <%for (String skill : skills){%>
                            <%String skillValue = skill.replace(" ", "");%>
                            <input type="checkbox" value="<%=skill%>" name="applicantSkill" >
                            <label for="<%=skillValue + "CheckBox"%>"> <%=skill%> </label>
                            <br/>
                        <%}%>
                        
                        <input type="checkbox" id="noSkill" value="noSkill" name="applicantSkill">
                        <label for="noSkill"> None </label>
                        <br/>
                        
                        <input type="checkbox" id="otherSkillCheck" value="otherSkill" onclick="getAddSkillForm()" name="applicantSkill">
                        <label for="otherSkillCheck"> Other </label>
                        
                        <br/>
                        
                        <table id="otherSkillInput" hidden="">
                            <tr>
                                <td>Enter skill(s) (separated '#' if there are multiple skills)</td>
                                <td><textarea name="newSkills" cols="50" ></textarea></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                
                <tr>
                    <td id="password_prompt">Password</td>
                    <td><input type="password" name="loginPassword" required=""></td>
                </tr>
                <tr>
                    <td>Confirm Password</td>
                    <td><input type="password" name="password_confirmation"></td>
                </tr>
                <tr>
                    <td colspan="2" class="submitBtn">
                        <input type="submit" value="Create Profile"></td>
                </tr>
            </table>
        </form>
        <script type="text/javascript" src="scripts/functions.js"></script>
    </body>
</html>
