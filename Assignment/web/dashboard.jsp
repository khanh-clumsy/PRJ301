<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard</title>
        <link rel="stylesheet" href="assets/css/dashboardStyle.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                <li class="active"><a href="../Assignment/dashboard">Dashboard</a></li>
                <li><a href="../Assignment/transaction">Transactions</a></li>
                <li><a href="../Assignment/user">Accounts</a></li>
                <li><a href="../Assignment/report">Reports</a></li>
                <li><a href="../Assignment/saving-goals">Saving Goals</a></li>            
                <li><a href="../Assignment/sharing-data">Sharing Data</a></li>

            </ul>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn">LOG OUT</button>
            </form> 
        </div>
        <div class="main-content">
            <div class="header">
                <h2>Dashboard</h2>
            </div>
            <div class="filter">
                <form id="reportForm" action="../Assignment/dashboard" method="post">
                    <select name="type" onchange="this.form.submit()">
                        <option value="expense" ${selectedType eq 'expense' ? 'selected' : ''}>Only Expense</option>
                        <option value="income" ${selectedType eq 'income' ? 'selected' : ''}>Only Income</option>
                    </select>

                    <select name="year" onchange="this.form.submit()">
                        <option value="all" ${selectedYear eq 'all' ? 'selected="selected"' : ''}>All</option>
                        <c:forEach items="${years}" var="year">
                            <option value="${year}" ${selectedYear eq year.toString() ? 'selected="selected"' : ''}>${year}</option>
                        </c:forEach>
                    </select>
                </form>
            </div>

            <!-- Container for pie chart and table -->
            <div class="report-container">
                <div class="chart-section">
                    <h3>
                        <c:choose>
                            <c:when test="${selectedType == 'income'}">Income Distribution</c:when>
                            <c:otherwise>Expense Distribution</c:otherwise>
                        </c:choose>
                    </h3>
                    <div class="chart-wrapper">
                        <canvas id="pieChart"></canvas>
                    </div>
                </div>
                <div class="table-section">
                    <h3>Monthly Summary</h3>
                    <div class="table-wrapper">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Month</th>
                                    <th>Income</th>
                                    <th>Expense</th>
                                    <th>Balance</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="i" begin="0" end="11">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i == 0}">January</c:when>
                                                <c:when test="${i == 1}">February</c:when>
                                                <c:when test="${i == 2}">March</c:when>
                                                <c:when test="${i == 3}">April</c:when>
                                                <c:when test="${i == 4}">May</c:when>
                                                <c:when test="${i == 5}">June</c:when>
                                                <c:when test="${i == 6}">July</c:when>
                                                <c:when test="${i == 7}">August</c:when>
                                                <c:when test="${i == 8}">September</c:when>
                                                <c:when test="${i == 9}">October</c:when>
                                                <c:when test="${i == 10}">November</c:when>
                                                <c:when test="${i == 11}">December</c:when>
                                            </c:choose>
                                        </td>
                                        <td class="amount income"><fmt:formatNumber value="${incomeData[i]}" type="currency" currencyCode="VND" maxFractionDigits="0"/></td>
                                        <td class="amount expense"><fmt:formatNumber value="${expenseData[i]}" type="currency" currencyCode="VND" maxFractionDigits="0"/></td>
                                        <td class="amount balance"><fmt:formatNumber value="${balanceData[i]}" type="currency" currencyCode="VND" maxFractionDigits="0"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <th>Total</th>
                                    <th class="amount income"><fmt:formatNumber value="${totalIncome}" type="currency" currencyCode="VND" maxFractionDigits="0"/></th>
                                    <th class="amount expense"><fmt:formatNumber value="${totalExpense}" type="currency" currencyCode="VND" maxFractionDigits="0"/></th>
                                    <th class="amount balance"><fmt:formatNumber value="${totalBalance}" type="currency" currencyCode="VND" maxFractionDigits="0"/></th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Footer section -->
            <div class="footer">
                <div class="footer-content">
                    <div class="footer-section">
                        <h3>Money Tracker</h3>
                        <p>Track your finances easily and efficiently.</p>
                    </div>
                    <div class="footer-section">
                        <h3>Quick Links</h3>
                        <ul>
                            <li><a href="../Assignment/dashboard">Dashboard</a></li>
                            <li><a href="../Assignment/transaction">Transactions</a></li>
                            <li><a href="../Assignment/report">Reports</a></li>
                        </ul>
                    </div>
                    <div class="footer-section">
                        <h3>Contact</h3>
                        <p>Email: support@moneytracker.com</p>
                        <p>Phone: +84 364828685</p>
                    </div>
                </div>
                <div class="footer-bottom">
                    <p>&copy; 2025 Money Tracker. All rights reserved.</p>
                </div>
            </div>
        </div>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                let categoryNames = JSON.parse('${categoryNames}');
                let categoryValues = JSON.parse('${categoryValues}');

                if (!Array.isArray(categoryNames) || categoryNames.length === 0) {
                    categoryNames = ["No Data"];
                    categoryValues = [0];
                }

                const predefinedColors = [
                    "#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF", "#FF9F40",
                    "#8D6E63", "#D32F2F", "#7CB342", "#303F9F", "#E64A19", "#5D4037"
                ];

                function getRandomColor() {
                    let r = Math.floor(Math.random() * 256);
                    let g = Math.floor(Math.random() * 256);
                    let b = Math.floor(Math.random() * 256);
                    return `rgba(${r}, ${g}, ${b}, 0.7)`;
                }

                let categoryColors = categoryNames.map((_, index) =>
                    predefinedColors[index % predefinedColors.length] || getRandomColor()
                );

                // Vẽ biểu đồ Pie Chart
                let ctxPie = document.getElementById('pieChart').getContext('2d');
                new Chart(ctxPie, {
                    type: 'pie',
                    data: {
                        labels: categoryNames,
                        datasets: [{
                                data: categoryValues,
                                backgroundColor: categoryColors
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {position: 'right'}
                        }
                    }
                });
            });
        </script>


    </body>
</html>