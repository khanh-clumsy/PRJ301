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
                <li><a href="../Assignment/budget">Budget</a></li>
                <li><a href="../Assignment/setting">Settings</a></li>
            </ul>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn">LOG OUT</button>
            </form>
        </div>
        <div class="main-content">
            <div class="header">
                <h2>Financial Reports</h2>
            </div>
            <form action="report" method="POST">
                <div class="chart-container">
                    <div class="chart-header">
                        <h3>Monthly Expense Analysis</h3>
                        <div class="chart-filters">
                            <select id="chart-year">
                                <option value="2025">2025</option>
                                <option value="2024">2024</option>
                                <option value="2023">2023</option>
                            </select>
                            <select id="chart-type">
                                <option value="expense">Expenses</option>
                                <option value="income">Income</option>
                                <option value="balance">Balance</option>
                            </select>
                        </div>
                    </div>
            </form>
            <div class="chart-wrapper">
                <canvas id="expenseChart"></canvas>
            </div>
        </div>
    </div>

    <script>
        // Sample data for the chart
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

        // Get the chart canvas
        const ctx = document.getElementById('expenseChart').getContext('2d');

        // Sample expense data - in a real app, this would come from the server
        const expenseData = [1200000, 980000, 1450000, 1100000, 1300000, 900000, 750000, 1050000, 1380000, 1200000, 1100000, 1600000];

        // Create the chart
        const expenseChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: months,
                datasets: [{
                        label: 'Monthly Expenses (VND)',
                        data: expenseData,
                        backgroundColor: '#3F51B5',
                        borderColor: '#303f9f',
                        borderWidth: 1,
                        borderRadius: 5,
                        hoverBackgroundColor: '#536DFE'
                    }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
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

        // Event listeners for filters
        document.getElementById('chart-type').addEventListener('change', function () {
            const selectedType = this.value;
            let data;
            let label;

            if (selectedType === 'income') {
                data = [1500000, 1650000, 1400000, 1550000, 1620000, 1680000, 1700000, 1750000, 1800000, 1900000, 1950000, 2100000];
                label = 'Monthly Income (VND)';
                expenseChart.data.datasets[0].backgroundColor = '#4CAF50';
                expenseChart.data.datasets[0].borderColor = '#388E3C';
                expenseChart.data.datasets[0].hoverBackgroundColor = '#66BB6A';
            } else if (selectedType === 'balance') {
                data = [300000, 670000, -50000, 450000, 320000, 780000, 950000, 700000, 420000, 700000, 850000, 500000];
                label = 'Monthly Balance (VND)';
                expenseChart.data.datasets[0].backgroundColor = '#FFC107';
                expenseChart.data.datasets[0].borderColor = '#FFA000';
                expenseChart.data.datasets[0].hoverBackgroundColor = '#FFCA28';
            } else {
                data = expenseData;
                label = 'Monthly Expenses (VND)';
                expenseChart.data.datasets[0].backgroundColor = '#3F51B5';
                expenseChart.data.datasets[0].borderColor = '#303f9f';
                expenseChart.data.datasets[0].hoverBackgroundColor = '#536DFE';
            }

            expenseChart.data.datasets[0].data = data;
            expenseChart.data.datasets[0].label = label;
            expenseChart.update();
        });
    </script>
</body>
</html>