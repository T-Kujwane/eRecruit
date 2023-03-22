<%-- 
    Document   : addVacancy
    Created on : 11 Mar 2023, 13:36:19
    Author     : T Kujwane
--%>

<%@page import="za.ac.tut.recruiter.Recruiter"%>
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
            List<String> courses = (List<String>) session.getAttribute("courses");
            List<String> recruiters = (List<String>) session.getAttribute("recruiters");
            List<String> skills = (List<String>) session.getAttribute("skills");
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
                        <select name="vacancyType" onchange="addVacancyType()">
                            <%for (String vacancyType : vacancyTypes) {%>
                            <option value="<%=vacancyType%>"><%=vacancyType%></option>
                            <%}%>
                            
                            <option value="newVacancyType">Other</option>
                        </select>
                            
                        <table id="addVacancyTypeTbl" >

                        </table>
                    </td>
                </tr>

                <tr>
                    <td>Required qualification(s)</td>
                    <td>
                        <!--<%for (String qualificationType : qualificationTypes) {%>
                        
                        <input class="checkbox" type="checkbox" id="<%=qualificationType%>" name="requiredQualification" 
                               value="<%=qualificationType%>" onclick="displayDropDown("<%=qualificationType%>")">
                        
                            <label class="checkbox" for="<%=qualificationType%>"> <%=qualificationType%></label>
                            
                            <%if (!qualificationType.contains("NSC")){%> in 
                                <select  name="courseName" id="<%=qualificationType + "DropDown"%>" hidden="">
                                    <%for (String course : courses) {%>
                                        <option value="<%=course%>"><%=course%></option>
                                    <%}%>
                                </select>
                            <%}%>
                        
                        <br/>
                        <%}%>-->
                        
                        <%for (String qualificationType : qualificationTypes){%>
                        <%String attributeVal = qualificationType.replace(" ", "");%>
                        
                        <input type="checkbox" id="<%=attributeVal + "CheckBox"%>" name="requiredQualification" value="<%=qualificationType%>" onclick="displayDropDown('<%=attributeVal%>')">
                            
                            <label for="<%=attributeVal + "CheckBox"%>"> <%=qualificationType%> </label>
                            
                            <%if (!qualificationType.contains("NSC")){%>
                            
                                <select name="course" id="<%=attributeVal + "DropDown"%>" hidden="">
                                    
                                    <%for (String course : courses){%>
                                        <option value="<%=course%>"> <%=course%> </option>
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
                    <td>Recruiter</td>
                    <td>
                        <select name="recruiter" id="recruiterDropDown" onchange="getRecruiterAddForm()">
                            
                            <%for (String recruiter : recruiters){%>
                                <option value="<%=recruiter%>"> <%=recruiter%> </option>
                            <%}%>
                            
                            <option value="newRecruiter">Other</option>
                        </select>
                            
                        <table id="addRecruiterTbl" hidden="">
                            <tr>
                                <td>Enterprise Number</td>
                                <td><input name="newRecruiterEnterpriseNr" type="text" maxlength="30" required=""></td>
                            </tr>
                            <tr>
                                <td>Enterprise Name</td>
                                <td><input name="newRecruiterEnterpriseName" type="text" maxlength="50" required=""></td>
                            </tr>
                            <tr>
                                <td>Enterprise Email</td>
                                <td><input name="newRecruiterEnterpriseEmail" type="text" maxlength="50" required=""></td>
                            </tr>
                            <tr>
                                <td>Enterprise Phone Number</td>
                                <td><input name="newRecruiterEnterprisePhone" type="text" maxlength="10" required=""></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                
                <tr>
                    <td>Required Skill(s)</td>
                    <td>
                        
                        <%for (String skill : skills){%>
                            <%String skillValue = skill.replace(" ", "");%>
                            <input type="checkbox" value="<%=skill%>" name="<%=skill%>" >
                            <label for="<%=skillValue + "CheckBox"%>"> <%=skill%> </label>
                            <br/>
                        <%}%>
                        
                        <input type="checkbox" id="noSkill" name="noSkill">
                        <label for="noSkill"> None </label>
                        <br/>
                        
                        <input type="checkbox" id="otherSkillCheck" name="otherSkill" onclick="getAddSkillForm()">
                        <label for="otherSkillCheck"> Other </label>
                        
                        <br/>
                        
                        <table id="otherSkillInput" hidden="">
                            <tr>
                                <td>Enter skill(s) (separated '#' if there are multiple skills)</td>
                                <td><textarea name="otherSkills" cols="50" ></textarea></td>
                            </tr>
                        </table>
                    </td>
                    
                </tr>
                
                <tr>
                    <td colspan="2" style="text-align: right;"><input type="submit" value="Publish"></td>
                </tr>
            </table>
        </form>
                    
        <script type="text/javascript" src="scripts/functions.js"></script>
    </body>
</html>
