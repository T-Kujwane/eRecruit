/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function getUserID() {
    let userID = prompt("Enter your identity (ID) numbers");

    while (userID.length !== 13) {

        alert("Invalid ID number of " + userID.length + " digits.");
        userID = prompt("Enter your identity (ID) number");
    }

    document.getElementById("deleteProfile").href = "DeleteProfileServlet?id=" + userID;

}

function getRecruiterEnterpriseNumber() {
    let recruiterEnterpriseNr;

    do {
        recruiterEnterpriseNr = prompt("Enter your institution's enterprise number.");
        if (recruiterEnterpriseNr === null) {
            break;
        } else {
            if (Boolean(recruiterEnterpriseNr.length < 13)) {
                alert("Invalid enterprise number. Try again.");
            }
        }
    } while (Boolean(recruiterEnterpriseNr.length < 13));

    if (recruiterEnterpriseNr === null) {
        document.getElementById("getVacancyApplications").href = "index.html";
    } else {
        document.getElementById("getVacancyApplications").href = "GetVacancyApplicationsServlet?enterpriseNr=" + recruiterEnterpriseNr;
    }
}

function addVacancyType() {
    var dropDown = getElementById("vacancyTypeDropDown");
    var addVacancyTable = getElementById("addVacancyTypeTbl");

    if (dropDown.value === "newVacancyType") {
        show(addVacancyTable);
    } else {
        hide(addVacancyTable);
    }
}

function displayDropDown(elementID) {
    if (!elementID.includes("NSC")) {
        var checkBox = document.getElementById(elementID + "CheckBox");

        if (checkBox.checked === true) {
            document.getElementById(elementID + "DropDown").hidden = false;
        } else {
            document.getElementById(elementID + "DropDown").hidden = true;
        }
    }
}

function getRecruiterAddForm() {

    var dropDown = document.getElementById("recruiterDropDown");

    if (dropDown.value === "newRecruiter") {
        document.getElementById("addRecruiterTbl").hidden = false;
    } else {
        document.getElementById("addRecruiterTbl").hidden = true;
    }
}

function getAddSkillForm() {
    var checkBox = document.getElementById("otherSkillCheck");

    if (checkBox.checked === true) {
        document.getElementById("otherSkillInput").hidden = false;
    } else {
        document.getElementById("otherSkillInput").hidden = true;
    }
}

function getElementById(elementId) {
    return document.getElementById(elementId);
}

function hide(object) {
    object.hidden = true;
}

function show(object) {
    object.hidden = false;
}


function verifyPassword(form){
    var initialPassword = form.password.value;
    var confPassword = form.password_confirmation.value;
    
    if(initialPassword !== confPassword){
        document.write("Passwords do not match");
        return false;
    }
    return true;
}


function verifyPassword(){
    var password = document.getElementsByName("password").value;
    var passwordConfirmation = document.getElementsByName("passwordConfirmation").value;
    
    if (password !== passwordConfirmation){
        document.write("Password mismatch");
        alert("Incorrect passwords");
        return;
    }
    alert("Matched passwrds");
    document.write("Password Correct");
}