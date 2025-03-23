<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Saving Goals</title>
        <link rel="stylesheet" href="assets/css/savingGoalsStyle.css">
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
                <li><a href="../Assignment/transaction">Transactions</a></li>
                <li><a href="../Assignment/user">Accounts</a></li>
                <li><a href="../Assignment/report">Reports</a></li>
                <li class="active"><a href="../Assignment/saving-goals">Saving Goals</a></li>
                <li><a href="../Assignment/sharing-data">Sharing Data</a></li>

            </ul>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn">LOG OUT</button>
            </form> 
        </div>
        <div class="main-content">
            <div class="header">
                <h2>Saving Goals</h2>
            </div>

            <!-- Add New Goal Section -->
            <div class="action-section">
                <a href="?action=showAddForm" class="add-goal-btn">+ Add New Goal</a>
            </div>
            <% String error = (String) session.getAttribute("error"); %>
            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
            <% session.removeAttribute("error"); %>
            <% } %>
            <!-- Form display based on action parameter -->
            <c:if test="${param.action == 'showAddForm'}">
                <div class="form-container">
                    <h3>Create New Saving Goal</h3>
                    <form action="../Assignment/saving-goals" method="post">
                        <input type="hidden" name="action" value="add">
                        <div class="form-group">
                            <label for="goalName">Goal Name</label>
                            <input type="text" id="goalName" name="goalName" required>
                        </div>
                        <div class="form-group">
                            <label for="targetAmount">Target Amount (VND)</label>
                            <input type="number" id="targetAmount" name="targetAmount" min="0" required>
                        </div>
                        <div class="form-group">
                            <label for="deadline">Deadline</label>
                            <input type="date" id="deadline" name="deadline" required>
                        </div>
                        <div class="form-group">
                            <label for="initialAmount">Initial Amount (VND)</label>
                            <input type="number" id="initialAmount" name="initialAmount" value="0" min="0">
                        </div>
                        <div class="form-buttons">
                            <a href="../Assignment/saving-goals" class="cancel-btn">Cancel</a>
                            <button type="submit" class="submit-btn">Create Goal</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <c:if test="${param.action == 'edit' && param.goalId != null}">
                <div class="form-container">
                    <h3>Edit Saving Goal</h3>
                    <form action="../Assignment/saving-goals" method="post">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="goalId" value="${param.goalId}">
                        <div class="form-group">
                            <label for="goalName">Goal Name</label>
                            <input type="text" id="goalName" name="goalName" value="${goalToEdit.goalName}" required>
                        </div>
                        <div class="form-group">
                            <label for="targetAmount">Target Amount (VND)</label>
                            <input type="number" id="targetAmount" name="targetAmount" value="${goalToEdit.targetAmount}" min="0" required>
                        </div>
                        <div class="form-group">
                            <label for="deadline">Deadline</label>
                            <input type="date" id="deadline" name="deadline" value="${goalToEdit.deadline}" required>
                        </div>
                        <div class="form-buttons">
                            <a href="../Assignment/saving-goals" class="cancel-btn">Cancel</a>
                            <button type="submit" class="submit-btn">Save Changes</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <c:if test="${param.action == 'contribute' && param.goalId != null}">
                <div class="form-container">
                    <h3>Add Contribution</h3>
                    <form action="../Assignment/saving-goals" method="post">
                        <input type="hidden" name="action" value="saveContribution">
                        <input type="hidden" name="goalId" value="${param.goalId}">
                        <div class="form-group">
                            <label for="amount">Amount (VND)</label>
                            <input type="number" id="amount" name="amount" min="1000" required>
                        </div>
                        <div class="form-group">
                            <label for="note">Note</label>
                            <textarea id="note" name="note"></textarea>
                        </div>
                        <div class="form-buttons">
                            <a href="../Assignment/saving-goals" class="cancel-btn">Cancel</a>
                            <button type="submit" class="submit-btn">Add Contribution</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <c:if test="${param.action == 'withdraw' && param.goalId != null}">
                <div class="form-container">
                    <h3>Withdraw Contribution</h3>
                    <form action="../Assignment/saving-goals" method="post">
                        <input type="hidden" name="action" value="deleteContribution">
                        <input type="hidden" name="goalId" value="${param.goalId}">
                        <div class="form-group">
                            <label for="amount">Amount (VND)</label>
                            <input type="number" id="amount" name="amount" min="1000" required>
                        </div>
                        <div class="form-group">
                            <label for="note">Note</label>
                            <textarea id="note" name="note"></textarea>
                        </div>
                        <div class="form-buttons">
                            <a href="../Assignment/saving-goals" class="cancel-btn">Cancel</a>
                            <button type="submit" class="submit-btn">Withdraw Contribution</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <c:if test="${param.action == 'delete' && param.goalId != null}">
                <div class="form-container">
                    <h3>Confirm Delete</h3>
                    <p>Are you sure you want to delete this saving goal? This action cannot be undone.</p>
                    <form action="../Assignment/saving-goals" method="post">
                        <input type="hidden" name="action" value="confirmDelete">
                        <input type="hidden" name="goalId" value="${param.goalId}">
                        <div class="form-buttons">
                            <a href="../Assignment/saving-goals" class="cancel-btn">Cancel</a>
                            <button type="submit" class="delete-btn">Delete</button>
                        </div>
                    </form>
                </div>
            </c:if>

            <!-- Goals Grid (only show when not in form view) -->
            <c:if test="${empty param.action || (param.action != 'showAddForm' && param.action != 'edit' && param.action != 'contribute' && param.action != 'delete') && param.action != 'withdraw'}">
                <div class="goals-grid">
                    <c:forEach items="${savingGoals}" var="goal">
                        <input type="hidden" name="goalId" value="${goal.goalId}}">
                        <div class="goal-card">
                            <div class="goal-header">
                                <h3>${goal.goalName}</h3>
                            </div>
                            <div class="goal-info">
                                <div class="goal-amount">
                                    <span class="label">Target:</span>
                                    <span class="amount"><fmt:formatNumber value="${goal.targetAmount}" type="currency" currencyCode="VND" maxFractionDigits="0"/></span>
                                </div>
                                <div class="goal-amount">
                                    <span class="label">Current:</span>
                                    <span class="amount"><fmt:formatNumber value="${goal.currentAmount}" type="currency" currencyCode="VND" maxFractionDigits="0"/></span>
                                </div>
                                <div class="goal-deadline">
                                    <span class="label">Deadline:</span>
                                    <span class="date"><fmt:formatDate value="${goal.deadline}" pattern="dd/MM/yyyy"/></span>
                                </div>
                                <div class="goal-status">
                                    <span class="label">Status:</span>
                                    <span class="status ${goal.status.replace(' ', '-')}">${goal.status}</span>
                                </div>

                            </div>
                            <div class="goal-progress">
                                <div class="progress-bar">
                                    <div class="progress" style="width: ${(goal.currentAmount / goal.targetAmount) * 100}%"></div>
                                </div>
                                <div class="progress-text">
                                    <fmt:formatNumber value="${(goal.currentAmount / goal.targetAmount) * 100}" maxFractionDigits="1"/>% Complete
                                </div>
                            </div>
                            <div class="goal-actions">
                                <c:if test="${goal.status != 'Completed' and goal.status != 'Not Completed'}">
                                    <a href="?action=contribute&goalId=${goal.goalId}" class="btn contribute-btn">Add Contribution</a>                                    
                                    <a href="?action=withdraw&goalId=${goal.goalId}" class="btn withdraw-btn">Withdraw Contribution</a>
                                    <a href="?action=edit&goalId=${goal.goalId}" class="btn edit-btn">Edit</a>
                                </c:if>
                                <a href="?action=delete&goalId=${goal.goalId}" class="btn delete-btn">Delete</a>
                            </div>

                        </div>
                    </c:forEach>

                    <!-- Empty state when no goals exist -->
                    <c:if test="${empty savingGoals}">
                        <div class="empty-state">
                            <div class="empty-icon">💰</div>
                            <h3>No saving goals yet</h3>
                            <p>Create your first saving goal to start tracking your progress!</p>
                            <a href="?action=showAddForm" class="add-goal-btn">+ Create Goal</a>
                        </div>
                    </c:if>
                </div>
            </c:if>

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
    </body>
</html>