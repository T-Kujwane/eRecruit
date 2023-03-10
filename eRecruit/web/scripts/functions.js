/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function getUserID(){
    let userID = prompt("Enter your identity (ID) number");
    
    while(userID.length !== 13){
        alert("Invalid ID number of " + userID.length + " digits.");
        userID = prompt("Enter your identity (ID) number");
    }
    
    document.getElementById("deleteProfile").href = "DeleteProfileServlet?id=" + userID;
}

function displayDropDown(elementID){
    
}