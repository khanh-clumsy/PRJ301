<%-- 
    Document   : register
    Created on : Feb 26, 2025, 4:45:32 PM
    Author     : admin
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" type="text/css" href="assets/css/registerStyle.css">    
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    </head>
    <body>
        <div class="container">
            <h2 class="title">Register</h2>
            <form action="register" method="post">
                <div class="c1">
                    <i class="fa-solid fa-user"></i>
                    <input class="input" type="text" name="username" value="${username}" placeholder="Username" required>
                </div>
                <div class="c1">
                    <i class="fa-solid fa-envelope"></i>
                    <input class="input" type="email" name="email" value="${email}" placeholder="Email" required>
                </div>
                <div class="c1">
                    <i class="fa-solid fa-lock"></i>
                    <input class="input" type="password" name="password" value="${password}" placeholder="Password" required>
                </div>
                <div class="c1">
                    <i class="fa-solid fa-lock"></i>
                    <input class="input" type="password" name="cpassword" value="${password}" placeholder="Confirm Password" required>
                </div>
                <p style="color: red; font-weight: bold">${requestScope.msg}</p>
                <input class="button" type="submit" value="Register">
            </form>
            <br>
            <a href="login">Already have an account? Login</a>    
        </div>
    </body>
</html>
