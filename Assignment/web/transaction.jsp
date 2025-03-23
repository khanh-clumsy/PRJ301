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
                <li class="active"><a href="../Assignment/transaction">Transactions</a></li>
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
                <h2>Transactions</h2>
            </div>
            <div class="add-container">
                <form id="finance-form" action="../Assignment/transaction" method="post">
                    <input type="hidden" name="code" value="${trans.id}">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="transaction-date">Date:</label>
                            <input type="date" id="transaction-date" name="date" value="${selectedDate != null ? selectedDate : trans.date}" required>
                        </div>

                        <div class="form-group">
                            <label for="transaction-type">Type:</label>
                            <select id="transaction-type" name="type" required onchange="filterCategories()">
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

                                <c:forEach items="${incomeCategories}" var="cat">
                                    <option value="${cat.id}" class="income-category" ${trans.categoryId eq cat.id ? 'selected' : ''}>${cat.name}</option>
                                </c:forEach>

                                <c:forEach items="${expenseCategories}" var="cat">
                                    <option value="${cat.id}" class="expense-category" ${trans.categoryId eq cat.id ? 'selected' : ''}>${cat.name}</option>
                                </c:forEach>
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
                    <c:if test="${not empty msg}">
                        <div class="error-message">
                            ${msg}
                        </div>
                    </c:if>                   
                    <div class="form-buttons">
                        <button type="submit" class="btn btn-primary" name="add">Add New</button>
                        <button type="submit" class="btn btn-primary" name="update">Update</button>
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
                                    <td>${t.id}</td>
                                    <td><fmt:formatDate value="${t.date}" pattern="dd/MM/yyyy"/></td>
                                    <td>${t.transactionType eq 'income' ? 'Income' : 'Expense'}</td>
                                    <td>${t.categoryName}</td>
                                    <td class="${t.transactionType eq 'income' ? 'income-amount' : 'expense-amount'}">
                                        <fmt:setLocale value="vi_VN"/>
                                        <fmt:formatNumber value="${t.amount}" type="currency" currencyCode="VND"/>
                                    </td>
                                    <td>${t.note}</td>
                                    <c:if test="${t.categoryName ne 'Saving Goal'}">
                                        <td>
                                            <a href="transaction?code=${t.id}&mode=1" class="action-btn edit-btn">Edit</a>
                                            <a href="transaction?code=${t.id}&mode=2" class="action-btn delete-btn">Delete</a>
                                        </td>
                                    </c:if>

                                </tr>
                            </c:forEach>
                            <c:if test="${empty transactions}">
                                <tr>
                                    <td colspan="7" style="text-align: center;">No transactions found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <script>
            function resetForm() {
                document.getElementById("transaction-date").value = "";
                document.getElementById("transaction-type").value = "";
                document.getElementById("category").value = "";
                document.getElementById("amount").value = "";
                document.getElementById("description").value = "";
                filterCategories();
            }
            function filterCategories() {
                var type = document.getElementById("transaction-type").value;
                var categorySelect = document.getElementById("category");
                var incomeOptions = document.querySelectorAll(".income-category");
                var expenseOptions = document.querySelectorAll(".expense-category");

                var selectedOption = categorySelect.options[categorySelect.selectedIndex];
                var selectedIsValid = (type === "income" && selectedOption.classList.contains("income-category")) ||
                        (type === "expense" && selectedOption.classList.contains("expense-category"));

                if (!selectedIsValid) {
                    categorySelect.value = "";
                }

                incomeOptions.forEach(opt => {
                    opt.style.display = (type === "income" && !opt.textContent.includes("Saving Goal")) ? "block" : "none";
                });

                expenseOptions.forEach(opt => {
                    opt.style.display = (type === "expense" && !opt.textContent.includes("Saving Goal")) ? "block" : "none";
                });
            }

            window.onload = filterCategories;
            document.getElementById("transaction-type").addEventListener("change", filterCategories);

            document.addEventListener("DOMContentLoaded", function () {
                const deleteButtons = document.querySelectorAll(".delete-btn");

                deleteButtons.forEach(button => {
                    button.addEventListener("click", function (event) {
                        event.preventDefault();

                        const deleteUrl = this.getAttribute("href");

                        Swal.fire({
                            title: "Are you sure delete this transaction?",
                            text: "This action cannot be undone!",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#d33",
                            cancelButtonColor: "#3085d6",
                            confirmButtonText: "Yes, delete it!"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = deleteUrl;
                            }
                        });
                    });
                });
            });

        </script>
    </body>
</html>
