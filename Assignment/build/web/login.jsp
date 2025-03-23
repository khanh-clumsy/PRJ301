<%-- 
    Document   : login
    Created on : Feb 26, 2025, 4:45:26 PM
    Author     : admin
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="assets/css/loginStyle.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    </head>
    <body>
        <div class="container">
            <c:if test="${not empty sessionScope.SessionRegister}">
                <div class="alert alert-success">
                    ${sessionScope.SessionRegister}
                </div>
                <c:remove var="SessionRegister" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.deleteUserMessage}">
                <div class="alert alert-success">
                    ${sessionScope.deleteUserMessage}
                </div>
                <c:remove var="deleteUserMessage" scope="session"/>
            </c:if>
            <h2 class="title">Login</h2>
            <form action="login" method="post">
                <div class="c1">
                    <i class="fa-solid fa-user"></i>
                    <input class="input" type="text" name="username" value="${sessionScope.username}" placeholder="Username" required></div>
                <div class="c1">
                    <i class="fa-solid fa-lock"></i>
                    <input class="input" type="password" name="password" value="${sessionScope.password}" placeholder="Password" required>
                </div>
                <p style="color: red; font-weight: bold">${requestScope.msg}</p>
                <input class="button" type="submit" value="Login">
            </form>
            </br>
            <a href="register">Don't have an account? Register</a>    
        </div>
    </body>
</html>
