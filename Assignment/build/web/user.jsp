<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User</title>
        <link rel="stylesheet" href="assets/css/userStyle.css">
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
                <li class="active"><a href="../Assignment/user">Accounts</a></li>
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
                <h2>Account Management</h2>
            </div>
            <div class="add-container">
                <form id="user-form" action="../Assignment/user" method="post">
                    <input type="hidden" name="id" value="${user.id}">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="username">Username:</label>
                            <input type="text" id="username" name="username" value="${user.username}" readonly>
                        </div>

                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" value="${user.email}" required>
                        </div>
                    </div>

                    <input type="hidden" id="balance" name="balance" value="${user.balance}" readonly>


                    <input type="hidden" id="created-date" name="createdTime" value="<fmt:formatDate value="${user.createdTime}" pattern="dd/MM/yyyy HH:mm:ss"/>" readonly>

                    <input type="hidden" name="userPassword" value="${user.password}">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="password">Password:</label>
                            <input type="password" id="password" name="password" value="${password}" placeholder="Leave blank to keep current password">
                        </div>

                        <div class="form-group">
                            <label for="confirm-password">Confirm Password:</label>
                            <input type="password" id="confirm-password" name="confirmPassword" value="${confirmPassword}" placeholder="Confirm new password">
                        </div>
                    </div>

                    ${msg}

                    <div class="form-buttons">
                        <button type="submit" class="btn btn-primary" id="submit-btn" name="update">Update Profile</button>
                        <button type="button" class="btn btn-neutral delete-btn" id="delete-action" name="delete">Delete account</button>
                    </div>
                </form>

                <div class="account-summary">
                    <h3>Account Summary</h3>
                    <div class="summary-card">
                        <div class="summary-item">
                            <div class="summary-label">Total Balance</div>
                            <div class="summary-value">
                                <fmt:setLocale value="vi_VN"/>
                                <fmt:formatNumber value="${user.balance}" type="currency" currencyCode="VND"/>
                            </div>
                        </div>
                        <div class="summary-item">
                            <div class="summary-label">Account Status</div>
                            <div class="summary-value ${user.getActive() ? 'active-status' : 'inactive-status'}">
                                ${user.getActive() ? 'Active' : 'Inactive'}
                            </div>
                        </div>
                        <div class="summary-item">
                            <div class="summary-label">Member Since</div>
                            <div class="summary-value">
                                <fmt:formatDate value="${user.createdTime}" pattern="dd MMMM yyyy"/>
                            </div>
                        </div>
                    </div>
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
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                document.querySelector(".delete-btn").addEventListener("click", function (event) {
                    event.preventDefault(); // Ngăn form gửi ngay lập tức

                    Swal.fire({
                        title: "Are you sure to delete your account?",
                        text: "This action cannot be undone!",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#d33",
                        cancelButtonColor: "#3085d6",
                        confirmButtonText: "Yes, delete it!"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            const form = document.getElementById("user-form");
                            let actionInput = document.createElement("input");
                            actionInput.type = "hidden";
                            actionInput.name = "delete";
                            actionInput.value = "true";
                            form.appendChild(actionInput);
                            form.submit();
                        }
                    });
                });
            });

        </script>
    </body>

</html>