<%-- 
    Document   : logoutPage
    Created on : 23 May 2023, 14:01:05
    Author     : T Kujwane
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign-out Page</title>
        <link rel="stylesheet" href="styles/primaryStyles.css" type="text/css">
    </head>
    <body>
        <nav style="font-size: 20px;">
            <a href="index.html">Home</a>
        </nav>
        <h1>Log-out Successful</h1>
        
        <p>
            You have been successfully logged out of the eRecruit system. <br>
            Please revisit the system for more opportunities. You can also share your experiences about the 
            services offered by the eRecruit team, and also comment on the overall functionality of the system 
            on various social media platforms.
        </p>
        
        <%session.invalidate();%>
        
        <footer style="margin-top: 50px;">
            Social media links<br>
            <table class="socialMediaLinks" style="border: none;">
                <tr>
                    <td style="border: none; padding-right: 10px;">
                        <a href="https://www.facebook.com" target="_blank">
                            <img src="images/facebookLogo.png" alt="Facbook" width="50px"/>
                            <br>
                            Facebook
                        </a>
                    </td>
                    
                    <td style="border: none; padding-right: 10px;">
                        <a href="https://www.twitter.com" target="_blank">
                            <img src="images/twitterIcon.png" alt="twitter Icon" width="50px"/><br>
                            Twitter
                        </a>
                    </td>
                    
                    <td style="border: none; padding-right: 10px;">
                        <a href="https://www.linkedin.com" target="_blank">
                            <img src="images/linkedInIcon.png" alt="Linked Icon" width="50px"/><br>
                            LinkedIn
                        </a>
                    </td>
                    
                </tr>
            </table>
            
        </footer>
    </body>
</html>
