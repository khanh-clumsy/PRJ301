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
                <li class="active"><a href="../Assignment/transaction">Transactions</a></li>
                <li><a href="../Assignment/user">Accounts</a></li>
                <li><a href="../Assignment/report">Reports</a></li>
                <li><a href="../Assignment/budget">Budget</a></li>
                <li><a href="../Assignment/setting">Settings</a></li>
            </ul>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn">LOG OUT</button>
            </form>
        </div>
        <div class="main-content">
            <div class="header">
                <h2>Transactions</h2>
            </div>
            <div class="add-container">
                <form id="finance-form" action="../Assignment/transaction" method="post">
                    <input type="hidden" name="code" value="${trans.id}">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="transaction-date">Date:</label>
                            <input type="date" id="transaction-date" name="date" value="${trans.date}" required>
                        </div>

                        <div class="form-group">
                            <label for="transaction-type">Type:</label>
                            <select id="transaction-type" name="type" required>
                                <option value="">-- Select transaction type --</option>
                                <option value="income" ${trans.transactionType eq 'income' ? 'selected' : ''}>Income</option>
                                <option value="expense" ${trans.transactionType eq 'expense' ? 'selected' : ''}>Expense</option>
                            </select>   
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="category">Category:</label>
                            <select id="category" name="category" required>
                                <option value="">-- Select category --</option>
                                <optgroup label="Income">
                                    <c:forEach items="${incomeCategories}" var="cat">
                                        <option value="${cat.id}" ${trans.categoryId eq cat.id ? 'selected' : ''}>${cat.name}</option>
                                    </c:forEach>
                                </optgroup>
                                <optgroup label="Expense">
                                    <c:forEach items="${expenseCategories}" var="cat">
                                        <option value="${cat.id}" ${trans.categoryId eq cat.id ? 'selected' : ''}>${cat.name}</option>
                                    </c:forEach>
                                </optgroup>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="amount">Amount:</label>
                            <input type="number" id="amount" name="amount" value="${trans.amount}" required>
                        </div>
                    </div>

                    <div class="form-group full-width">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description" rows="2">${trans.note}</textarea>
                    </div>
                    ${msg}
                    <div class="form-buttons">
                        <button type="submit" class="btn btn-primary" id="submit-btn" name="add">Add New</button>
                        <button type="submit" class="btn btn-primary" id="submit-btn" name="update">Update</button>
                        <button type="button" class="btn btn-neutral" onclick="resetForm();">Reset</button>


                    </div>
                </form>

                <div class="transactions-list">
                    <h3>Recent Transactions List</h3>
                    <table id="transactions-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Date</th>
                                <th>Type</th>
                                <th>Category</th>
                                <th>Amount</th>
                                <th>Description</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${transactions}" var="t">
                                <tr>
                                    <td>${t.id}</td> <!-- Kiểm tra id có hiển thị không -->
                                    <td><fmt:formatDate value="${t.date}" pattern="dd/MM/yyyy"/></td>
                                    <td>${t.transactionType eq 'income' ? 'Income' : 'Expense'}</td>
                                    <td>${t.categoryName}</td>
                                    <td class="${t.transactionType eq 'income' ? 'income-amount' : 'expense-amount'}">
                                        <fmt:setLocale value="vi_VN"/>
                                        <fmt:formatNumber value="${t.amount}" type="currency" currencyCode="VND"/>
                                    </td>
                                    <td>${t.note}</td>
                                    <td>
                                        <a href="transaction?code=${t.id}&mode=1" class="action-btn edit-btn">Edit</a>
                                        <a href="transaction?code=${t.id}&mode=2" class="action-btn delete-btn">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>


                            <c:if test="${empty transactions}">
                                <tr>
                                    <td colspan="6" style="text-align: center;">No transactions found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

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
    </body>
    <script>
        function resetForm() {
            document.getElementById("finance-form").reset();
            document.getElementById("transaction-type").removeAttribute("disabled");
        }
    </script> 
</html>
