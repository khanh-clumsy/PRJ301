<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reports</title>
        <link rel="stylesheet" href="assets/css/reportStyle.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
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
                <li class="active"><a href="../Assignment/report">Reports</a></li>
                <li><a href="../Assignment/saving-goals">Saving Goals</a></li>
                <li><a href="../Assignment/sharing-data">Sharing Data</a></li>
            </ul>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn">LOG OUT</button>
            </form>
        </div>
        <div class="main-content">
            <div class="header">
                <h2>Financial Reports</h2>
            </div>
            <div class="filter">
                <form id="reportForm" action="../Assignment/report" method="post">
                    <select name="year" onchange="this.form.submit()">
                        <option value="all" ${selectedYear eq 'all' ? 'selected="selected"' : ''}>All</option>
                        <c:forEach items="${years}" var="year">
                            <option value="${year}" ${selectedYear eq year.toString() ? 'selected="selected"' : ''}>${year}</option>
                        </c:forEach>
                    </select>
                </form>
            </div>
            <div class="chart-container">
                <div class="chart-header">
                    <h3>Yearly Analysis</h3>
                </div>
                <div class="chart-wrapper">
                    <canvas id="expenseChart"></canvas>
                </div>
            </div>
        </div>
        <script>
            const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
            const ctx = document.getElementById('expenseChart').getContext('2d');
            const incomeData = ${incomeData};
            const expenseData = ${expenseData};

            const expenseChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: months,
                    datasets: [
                        {
                            label: 'Income (VND)',
                            data: incomeData,
                            backgroundColor: '#4CAF50',
                            borderColor: '#388E3C',
                            borderWidth: 1,
                            borderRadius: 5,
                            hoverBackgroundColor: '#66BB6A'
                        },
                        {
                            label: 'Expense (VND)',
                            data: expenseData,
                            backgroundColor: '#F44336',
                            borderColor: '#D32F2F',
                            borderWidth: 1,
                            borderRadius: 5,
                            hoverBackgroundColor: '#E57373'
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {position: 'top'},
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    if (context.parsed.y !== null) {
                                        label += new Intl.NumberFormat('vi-VN', {
                                            style: 'currency',
                                            currency: 'VND',
                                            maximumFractionDigits: 0
                                        }).format(context.parsed.y);
                                    }
                                    return label;
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function (value) {
                                    if (value >= 1000000) {
                                        return (value / 1000000).toFixed(0) + ' M';
                                    } else if (value >= 1000) {
                                        return (value / 1000).toFixed(0) + ' K';
                                    }
                                    return value;
                                }
                            }
                        }
                    }
                }
            });

        </script>
    </body>
</html>