<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard</title>
        <link rel="stylesheet" href="assets/css/transactionsStyle.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    </head>
    <body>
        <div class="sidebar">
            <div class="logo">Money Tracker</div>
            <div class="image-container">
                <img src="assets/img/user.jpg">
            </div>
            <p style="color: white; font-weight: bold; text-align: center">
                Hello ${sessionScope.username}
            </p>
            <ul>
                <li><a href="../Assignment/dashboard">Dashboard</a></li>
                <li><a href="../Assignment/transaction">Transactions</a></li>
                <li><a href="../Assignment/user">Accounts</a></li>
                <li><a href="../Assignment/report">Reports</a></li>
                <li><a href="../Assignment/saving-goals">Saving Goals</a></li>
                <li class="active"><a href="../Assignment/sharing-data">Sharing Data</a></li>
            </ul>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn">LOG OUT</button>
            </form>
        </div>
        <div class="main-content">
            <div class="header">
                <h2>Sharing Data</h2>
            </div>
            <div class="add-container">
                <div class="transactions-list">
                    <h3>Summary</h3>
                    <table id="transactions-table">
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>Income</th>
                                <th>Expense</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty sharedData}">
                                <c:forEach items="${sharedData}" var="data">
                                    <tr>
                                        <td>${data.username}</td> <!-- Hiển thị Username -->
                                        <td class="income-amount">
                                            <fmt:setLocale value="vi_VN"/>
                                            <fmt:formatNumber value="${data.income}" type="currency" currencyCode="VND"/>
                                        </td>
                                        <td class="expense-amount">
                                            <fmt:setLocale value="vi_VN"/>
                                            <fmt:formatNumber value="${data.expense}" type="currency" currencyCode="VND"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty sharedData}">
                                <tr>
                                    <td colspan="3" style="text-align: center;">No data available.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>

                    <form action="sharing-data" method="post">
                        <label for="userId">Select User:</label>
                        <select id="userId" name="userId">
                            <c:forEach var="user" items="${userList}">
                                <option value="${user.id}" <c:if test="${user.id == userId}">selected</c:if>>
                                    ${user.username}
                                </option>
                            </c:forEach>

                        </select>
                        <button type="submit">Share Data</button>
                    </form>
                    ${requestScope.err}
                </div>
            </div>
        </div>
    </body>
</html>