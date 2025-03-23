/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import com.google.gson.Gson;
import dal.CategoryDAO;
import dal.SavingGoalDAO;
import dal.ShareDataDAO;
import dal.TransactionDAO;
import dal.UsersDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import model.Category;
import model.SavingGoal;
import model.Transaction;
import model.Users;
import java.sql.Date;
import model.ShareData;

/**
 *
 * @author admin
 */
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/transaction":
                showTransaction(request, response);
                break;
            case "/dashboard":
                showDashboard(request, response);
                break;
            case "/report":
                showReport(request, response);
                break;
            case "/saving-goals":
                showSavingGoals(request, response);
                break;
            case "/sharing-data":
                showSharingData(request, response);
                break;
            default:
                response.sendRedirect("login");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/transaction":
                handleTransaction(request, response);
                break;
            case "/dashboard":
                handleDashboard(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/report":
                handleReport(request, response);
                break;
            case "/saving-goals":
                handleSavingGoals(request, response);
                break;
            case "/sharing-data":
                handleSharingdata(request, response);
                break;
            default:
                response.sendRedirect("login");
                break;
        }
    }

    private void showTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");
        String username = (String) session.getAttribute("username");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        LocalDate today = LocalDate.now();
        CategoryDAO cda = new CategoryDAO();
        TransactionDAO tda = new TransactionDAO();
        UsersDAO uda = new UsersDAO();

        List<Category> incomeList = cda.getCategoryByType("income");
        List<Category> expenseList = cda.getCategoryByType("expense");
        request.setAttribute("incomeCategories", incomeList);
        request.setAttribute("expenseCategories", expenseList);

        Users user = uda.getUsersByUsername(username);
        BigDecimal userBalance = user.getBalance();

        String modeStr = request.getParameter("mode");
        int mode = (modeStr != null) ? Integer.parseInt(modeStr) : 0;

        if (mode == 1) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            Transaction transaction = tda.getTransactionById(userId, transactionId);
            request.setAttribute("trans", transaction);
        }

        if (mode == 2) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            Transaction oldTransaction = tda.getTransactionById(userId, transactionId);

            if (oldTransaction != null) {
                userBalance = updateBalance(userBalance, oldTransaction, false);
                uda.updateUserBalance(userId, userBalance);
            }

            tda.deleteTransaction(userId, transactionId);
        }
        request.setAttribute("selectedDate", today);
        List<Transaction> transactionList = tda.getTransactionByUser(userId);
        request.setAttribute("transactions", transactionList);
        request.getRequestDispatcher("transaction.jsp").forward(request, response);
    }

    private void handleTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");
        String username = (String) session.getAttribute("username");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        UsersDAO uda = new UsersDAO();
        CategoryDAO cda = new CategoryDAO();
        TransactionDAO tda = new TransactionDAO();

        Users u = uda.getUsersByUsername(username);
        BigDecimal userBalance = u.getBalance();

        List<Category> incomeList = cda.getCategoryByType("income");
        List<Category> expenseList = cda.getCategoryByType("expense");
        request.setAttribute("incomeCategories", incomeList);
        request.setAttribute("expenseCategories", expenseList);

        String selectedDate = request.getParameter("date");
        String selectedType = request.getParameter("type");
        String selectedCategory = request.getParameter("category");
        String amountStr = request.getParameter("amount");
        String description = request.getParameter("description");

        int categoryId = Integer.parseInt(selectedCategory);
        BigDecimal amount = new BigDecimal(amountStr);
        String msg = "";
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            msg = "Amount can not <=0!\n";
        }
        Date transactionDate = null;
        Category c = cda.getCategoryById(categoryId);

        boolean isValidDate = true;

        try {
            if (selectedDate != null && !selectedDate.isEmpty()) {
                LocalDate selectedLocalDate = LocalDate.parse(selectedDate);
                LocalDate now = LocalDate.now();

                if (selectedLocalDate.isBefore(LocalDate.of(2020, 1, 1))) {
                    msg += "Date must not be before 2020!";
                    isValidDate = false;
                } else if (selectedLocalDate.isAfter(now)) {
                    msg += "Date cannot be in the future!";
                    isValidDate = false;
                } else {
                    transactionDate = java.sql.Date.valueOf(selectedLocalDate);
                }
            }
        } catch (Exception e) {
            msg += "Invalid date format!";
            isValidDate = false;
        }

        if (!isValidDate) {
            request.setAttribute("selectedDate", selectedDate);
        }

        Transaction t = new Transaction(userId, categoryId, amount, transactionDate, description,
                selectedType, c.getName());
        request.setAttribute("trans", t);

        if (request.getParameter("add") != null) {
            if (!msg.isEmpty()) {
                List<Transaction> transactionList = tda.getTransactionByUser(userId);
                request.setAttribute("transactions", transactionList);
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("transaction.jsp").forward(request, response);
                return;
            }
            if (u.getBalance().equals(BigDecimal.ZERO)) {
                List<Transaction> transactionList = tda.getTransactionByUser(userId);
                request.setAttribute("transactions", transactionList);
                request.setAttribute("msg", "K du so du");
                request.getRequestDispatcher("transaction.jsp").forward(request, response);
                return;
            }
            if (tda.addTransaction(userId, t)) {
                request.setAttribute("msg", "Added successfully!");
                userBalance = updateBalance(userBalance, t, true);
                uda.updateUserBalance(userId, userBalance);
            }
        }

        if (request.getParameter("update") != null) {
            if (!msg.isEmpty()) {
                List<Transaction> transactionList = tda.getTransactionByUser(userId);
                request.setAttribute("transactions", transactionList);
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("transaction.jsp").forward(request, response);
                return;
            }

            int transactionId = Integer.parseInt(request.getParameter("code"));
            Transaction oldTransaction = tda.getTransactionById(userId, transactionId);
            t.setId(transactionId);

            if (tda.updateTransaction(t)) {
                request.setAttribute("msg", "Updated successfully!");
                if (oldTransaction != null) {
                    userBalance = updateBalance(userBalance, oldTransaction, false);
                }
                userBalance = updateBalance(userBalance, t, true);
                uda.updateUserBalance(userId, userBalance);
            } else {
                request.setAttribute("msg", "Update error!");
            }
        }

        String modeStr = request.getParameter("mode");
        int mode = (modeStr != null) ? Integer.parseInt(modeStr) : 0;

        if (mode == 1) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            t = tda.getTransactionById(userId, transactionId);
            request.setAttribute("trans", t);
        }

        if (mode == 2) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            Transaction oldTransaction = tda.getTransactionById(userId, transactionId);

            if (oldTransaction != null) {
                userBalance = updateBalance(userBalance, oldTransaction, false);
                uda.updateUserBalance(userId, userBalance);
            }

            tda.deleteTransaction(userId, transactionId);
        }

        List<Transaction> transactionList = tda.getTransactionByUser(userId);
        request.setAttribute("transactions", transactionList);
        request.getRequestDispatcher("transaction.jsp").forward(request, response);
    }

    private BigDecimal updateBalance(BigDecimal balance, Transaction transaction, boolean isAdd) {
        if (transaction.getTransactionType().equals("income")) {
            return isAdd ? balance.add(transaction.getAmount()) : balance.subtract(transaction.getAmount());
        } else {
            return isAdd ? balance.subtract(transaction.getAmount()) : balance.add(transaction.getAmount());
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        Integer userId = (Integer) s.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String selectedYear = request.getParameter("year");
        String selectedType = request.getParameter("type");

        if (selectedType == null || (!selectedType.equals("income") && !selectedType.equals("expense"))) {
            selectedType = "expense";
        }

        TransactionDAO tda = new TransactionDAO();
        List<Integer> years = tda.getYearByUser(userId);
        Map<Integer, Map<String, Integer>> monthlyData;
        monthlyData = tda.getMonthlyDataAll(userId);

        List<Integer> incomeData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> expenseData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> balanceData = new ArrayList<>(Collections.nCopies(12, 0));

        int totalIncome = 0, totalExpense = 0, totalBalance = 0;

        for (int month = 1; month <= 12; month++) {
            if (monthlyData.containsKey(month)) {
                int income = monthlyData.get(month).get("income");
                int expense = monthlyData.get(month).get("expense");
                int balance = monthlyData.get(month).get("balance");

                incomeData.set(month - 1, income);
                expenseData.set(month - 1, expense);
                balanceData.set(month - 1, balance);

                totalIncome += income;
                totalExpense += expense;
                totalBalance += balance;
            }
        }

        List<String> categoryNames = new ArrayList<>();
        List<Double> categoryValues = new ArrayList<>();
        Map<String, Double> maps;

        maps = tda.getCategoryAndValueAll(userId, selectedType);

        for (Map.Entry<String, Double> entry : maps.entrySet()) {
            categoryNames.add(entry.getKey());
            categoryValues.add(entry.getValue());
        }

        Gson gson = new Gson();
        String categoryNamesJson = gson.toJson(categoryNames);
        String categoryValuesJson = gson.toJson(categoryValues);

        request.setAttribute("categoryNames", categoryNamesJson);
        request.setAttribute("categoryValues", categoryValuesJson);
        request.setAttribute("incomeData", incomeData);
        request.setAttribute("expenseData", expenseData);
        request.setAttribute("balanceData", balanceData);
        request.setAttribute("totalIncome", totalIncome);
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("totalBalance", totalBalance);
        request.setAttribute("selectedYear", selectedYear);
        request.setAttribute("selectedType", selectedType);
        request.setAttribute("years", years);

        // Chuyển hướng về dashboard.jsp
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        Integer userId = (Integer) s.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String selectedYear = request.getParameter("year");
        String selectedType = request.getParameter("type");

        if (selectedType == null || (!selectedType.equals("income") && !selectedType.equals("expense"))) {
            selectedType = "expense";
        }

        TransactionDAO tda = new TransactionDAO();
        List<Integer> years = tda.getYearByUser(userId);
        Map<Integer, Map<String, Integer>> monthlyData;

        if (selectedYear == null || selectedYear.isEmpty() || selectedYear.equals("all")) {
            selectedYear = "all";
            monthlyData = tda.getMonthlyDataAll(userId);
        } else {
            try {
                int intYear = Integer.parseInt(selectedYear);
                monthlyData = tda.getMonthlyDataByYear(userId, intYear);
            } catch (NumberFormatException e) {
                response.sendRedirect("dashboard?error=invalid_year");
                return;
            }
        }

        List<Integer> incomeData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> expenseData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> balanceData = new ArrayList<>(Collections.nCopies(12, 0));

        int totalIncome = 0, totalExpense = 0, totalBalance = 0;

        for (int month = 1; month <= 12; month++) {
            if (monthlyData.containsKey(month)) {
                int income = monthlyData.get(month).get("income");
                int expense = monthlyData.get(month).get("expense");
                int balance = monthlyData.get(month).get("balance");

                incomeData.set(month - 1, income);
                expenseData.set(month - 1, expense);
                balanceData.set(month - 1, balance);

                totalIncome += income;
                totalExpense += expense;
                totalBalance += balance;
            }
        }

        List<String> categoryNames = new ArrayList<>();
        List<Double> categoryValues = new ArrayList<>();
        Map<String, Double> maps;

        if ("all".equals(selectedYear)) {
            maps = tda.getCategoryAndValueAll(userId, selectedType);
        } else {
            maps = tda.getCategoryAndValueByYear(userId, selectedType, selectedYear);
        }

        for (Map.Entry<String, Double> entry : maps.entrySet()) {
            categoryNames.add(entry.getKey());
            categoryValues.add(entry.getValue());
        }

        Gson gson = new Gson();
        String categoryNamesJson = gson.toJson(categoryNames);
        String categoryValuesJson = gson.toJson(categoryValues);

        request.setAttribute("categoryNames", categoryNamesJson);
        request.setAttribute("categoryValues", categoryValuesJson);
        request.setAttribute("incomeData", incomeData);
        request.setAttribute("expenseData", expenseData);
        request.setAttribute("balanceData", balanceData);
        request.setAttribute("totalIncome", totalIncome);
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("totalBalance", totalBalance);
        request.setAttribute("selectedYear", selectedYear);
        request.setAttribute("selectedType", selectedType);
        request.setAttribute("years", years);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login");
    }

    private void showReport(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession s = request.getSession();
        Integer userId = (Integer) s.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String selectedYear = request.getParameter("year");

        TransactionDAO tda = new TransactionDAO();
        List<Integer> years = tda.getYearByUser(userId);
        Map<Integer, Map<String, Integer>> monthlyData = tda.getMonthlyDataAll(userId);

        List<Integer> incomeData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> expenseData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> balanceData = new ArrayList<>(Collections.nCopies(12, 0));

        int totalIncome = 0, totalExpense = 0, totalBalance = 0;

        for (int month = 1; month <= 12; month++) {
            if (monthlyData.containsKey(month)) {
                int income = monthlyData.get(month).getOrDefault("income", 0);
                int expense = monthlyData.get(month).getOrDefault("expense", 0);
                int balance = monthlyData.get(month).getOrDefault("balance", 0);

                incomeData.set(month - 1, income);
                expenseData.set(month - 1, expense);
                balanceData.set(month - 1, balance);

                totalIncome += income;
                totalExpense += expense;
                totalBalance += balance;
            }
        }

        request.setAttribute("incomeData", incomeData);
        request.setAttribute("expenseData", expenseData);
        request.setAttribute("balanceData", balanceData);
        request.setAttribute("totalIncome", totalIncome);
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("totalBalance", totalBalance);
        request.setAttribute("selectedYear", selectedYear);
        request.setAttribute("years", years);

        request.getRequestDispatcher("report.jsp").forward(request, response);
    }

    private void handleReport(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession s = request.getSession();
        Integer userId = (Integer) s.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String selectedYear = request.getParameter("year");

        TransactionDAO tda = new TransactionDAO();
        List<Integer> years = tda.getYearByUser(userId);
        Map<Integer, Map<String, Integer>> monthlyData;

        if (selectedYear == null || selectedYear.isEmpty() || selectedYear.equals("all")) {
            selectedYear = "all";
            monthlyData = tda.getMonthlyDataAll(userId);
        } else {
            try {
                int intYear = Integer.parseInt(selectedYear);
                monthlyData = tda.getMonthlyDataByYear(userId, intYear);
            } catch (NumberFormatException e) {
                response.sendRedirect("report?error=invalid_year");
                return;
            }
        }

        List<Integer> incomeData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> expenseData = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> balanceData = new ArrayList<>(Collections.nCopies(12, 0));

        int totalIncome = 0, totalExpense = 0, totalBalance = 0;

        for (int month = 1; month <= 12; month++) {
            if (monthlyData.containsKey(month)) {
                int income = monthlyData.get(month).getOrDefault("income", 0);
                int expense = monthlyData.get(month).getOrDefault("expense", 0);
                int balance = monthlyData.get(month).getOrDefault("balance", 0);

                incomeData.set(month - 1, income);
                expenseData.set(month - 1, expense);
                balanceData.set(month - 1, balance);

                totalIncome += income;
                totalExpense += expense;
                totalBalance += balance;
            }
        }

        request.setAttribute("incomeData", incomeData);
        request.setAttribute("expenseData", expenseData);
        request.setAttribute("balanceData", balanceData);
        request.setAttribute("totalIncome", totalIncome);
        request.setAttribute("totalExpense", totalExpense);
        request.setAttribute("totalBalance", totalBalance);
        request.setAttribute("selectedYear", selectedYear);
        request.setAttribute("years", years);

        request.getRequestDispatcher("report.jsp").forward(request, response);
    }

    private void showSavingGoals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        Integer userId = (Integer) s.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        SavingGoalDAO sda = new SavingGoalDAO();
        LocalDate today = LocalDate.now();

        // Auto update status for expired goals
        sda.updateExpiredGoals(today);
        switch (action) {
            case "showAddForm":
                request.getRequestDispatcher("saving-goals.jsp").forward(request, response);
                break;
            case "edit":
                int goalId = Integer.parseInt(request.getParameter("goalId"));
                SavingGoal goalToEdit = sda.getGoalById(userId, goalId);
                request.setAttribute("goalToEdit", goalToEdit);
                request.getRequestDispatcher("saving-goals.jsp").forward(request, response);
                break;
            case "delete":
                request.getRequestDispatcher("saving-goals.jsp").forward(request, response);
                break;
            default:
                List<SavingGoal> savingGoals = sda.getAllGoalsByUserId(userId);
                request.setAttribute("savingGoals", savingGoals);
                request.getRequestDispatcher("saving-goals.jsp").forward(request, response);
                break;
        }
    }

    private void handleSavingGoals(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        SavingGoalDAO sda = new SavingGoalDAO();
        LocalDate today = LocalDate.now();
        String status = "In Progress";
        sda.updateExpiredGoals(today);
        UsersDAO uda = new UsersDAO();
        Users u = uda.getUserById(userId);
        BigDecimal userBalance = u.getBalance();
        TransactionDAO tda = new TransactionDAO();
        switch (action) {
            case "add":
                String goalName = request.getParameter("goalName");
                BigDecimal targetAmount = new BigDecimal(request.getParameter("targetAmount"));
                BigDecimal initialAmount = new BigDecimal(request.getParameter("initialAmount"));
                LocalDate deadline = LocalDate.parse(request.getParameter("deadline"));

                if (deadline.isBefore(today)) {
                    session.setAttribute("error", "Deadline cannot be in the past.");
                    response.sendRedirect("saving-goals");
                    return;
                }
                if (targetAmount.compareTo(initialAmount) < 0) {
                    session.setAttribute("error", "Target amount cannot be less than initial amount.");
                    response.sendRedirect("saving-goals");
                    return;
                }

                boolean added = sda.addGoal(userId, new SavingGoal(userId, goalName, targetAmount,
                        initialAmount,
                        java.sql.Date.valueOf(deadline), status));
                if (added) {

                }
                break;
            case "update":
                int goalId = Integer.parseInt(request.getParameter("goalId"));
                String updatedName = request.getParameter("goalName");
                BigDecimal updatedTargetAmount = new BigDecimal(request.getParameter("targetAmount"));
                BigDecimal updatedCurrentAmount = sda.getCurrentAmount(goalId);
                LocalDate updatedDeadline = LocalDate.parse(request.getParameter("deadline"));
                if (updatedDeadline.isBefore(today)) {
                    session.setAttribute("error", "Deadline cannot be in the past.");
                    response.sendRedirect("saving-goals");
                    return;
                }
                if (updatedTargetAmount.compareTo(updatedCurrentAmount) < 0) {
                    session.setAttribute("error", "Target amount cannot be less than current amount.");
                    response.sendRedirect("saving-goals");
                    return;
                }

                sda.updateGoal(new SavingGoal(goalId, updatedName, updatedTargetAmount, updatedCurrentAmount, java.sql.Date.valueOf(updatedDeadline), "In Progress"));
                break;

            case "saveContribution":
                int goalIdForContribution = Integer.parseInt(request.getParameter("goalId"));
                BigDecimal amount = new BigDecimal(request.getParameter("amount"));
                BigDecimal currentTotal = sda.getCurrentAmount(goalIdForContribution);
                BigDecimal target = sda.getTargetAmount(goalIdForContribution);
                String note = request.getParameter("note");

                if (currentTotal.add(amount).compareTo(target) > 0) {
                    session.setAttribute("error", "Contribution exceeds target amount.");
                    response.sendRedirect("saving-goals");
                    return;
                }

                if (userBalance.compareTo(amount) < 0) {
                    session.setAttribute("error", "Insufficient balance to contribute.");
                    response.sendRedirect("saving-goals");
                    return;
                }

                Date currentDate = Date.valueOf(LocalDate.now());
                sda.addContribution(userId, goalIdForContribution, amount, currentDate, note);

                Transaction t = new Transaction(userId, 11, amount, currentDate, note, "expense", "Saving Goal");
                userBalance = updateBalance(userBalance, t, true);
                uda.updateUserBalance(userId, userBalance);

                List<SavingGoal> savingGoals = sda.getAllGoalsByUserId(userId);
                request.setAttribute("savingGoals", savingGoals);
                break;

            case "deleteContribution":
                goalIdForContribution = Integer.parseInt(request.getParameter("goalId"));
                amount = new BigDecimal(request.getParameter("amount"));
                currentTotal = sda.getCurrentAmount(goalIdForContribution);
                target = sda.getTargetAmount(goalIdForContribution);
                note = request.getParameter("note");

                if (currentTotal.compareTo(amount) < 0) {
                    session.setAttribute("error", "Not enough balance in Saving Goal to withdraw.");
                    response.sendRedirect("saving-goals");
                    return;
                }

                currentDate = Date.valueOf(LocalDate.now());
                sda.withdrawContribution(userId, goalIdForContribution, amount, currentDate, note);

                t = new Transaction(userId, 11, amount, currentDate, note, "expense", "Saving Goal");
                userBalance = updateBalance(userBalance, t, false);
                uda.updateUserBalance(userId, userBalance);

                savingGoals = sda.getAllGoalsByUserId(userId);
                request.setAttribute("savingGoals", savingGoals);
                break;

            case "confirmDelete":
                int goalIdToDelete = Integer.parseInt(request.getParameter("goalId"));
                SavingGoal s = sda.getGoalById(userId, goalIdToDelete);
                if (s.getCurrentAmount().compareTo(BigDecimal.ZERO) > 0) {
                    tda.deleteTransactionsByGoalId(goalIdToDelete);
                }
                sda.deleteGoal(goalIdToDelete);
                break;
        }
        List<SavingGoal> savingGoals = sda.getAllGoalsByUserId(userId);
        request.setAttribute("savingGoals", savingGoals);
        response.sendRedirect("saving-goals");
    }

    private void showSharingData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsersDAO uda = new UsersDAO();
        List<Users> list = uda.getAllUsers();
        ShareDataDAO sda = new ShareDataDAO();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }
        list.removeIf(user -> user.getId() == userId);
        List<ShareData> sharedData = sda.getSharedDataByUserId(userId);
        request.setAttribute("sharedData", sharedData);
        request.setAttribute("userList", list);
        request.getRequestDispatcher("sharingdata.jsp").forward(request, response);
    }

    private void handleSharingdata(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsersDAO uda = new UsersDAO();
        List<Users> list = uda.getAllUsers();

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }
        ShareDataDAO sda = new ShareDataDAO();
        TransactionDAO tda = new TransactionDAO();

        BigDecimal totalIncome = tda.getTotalIncome(userId);
        BigDecimal totalExpense = tda.getTotalExpense(userId);

        int shareWithId = Integer.parseInt(request.getParameter("userId"));
        boolean shared = uda.shareData(userId, shareWithId, totalIncome, totalExpense);
        if (!shared) {
            request.setAttribute("err", "Failed to share data. Please try again.");
            request.getRequestDispatcher("sharingdata.jsp").forward(request, response);
            return;
        }
        Users u = uda.getUserById(userId);
        list.removeIf(user -> user.getId() == userId);
        request.setAttribute("userId", request.getParameter("userId"));
        request.setAttribute("err", "Shared successfully!");
        List<ShareData> sharedData = sda.getSharedDataByUserId(userId);
        request.setAttribute("sharedData", sharedData);
        request.setAttribute("userList", list);
        request.getRequestDispatcher("sharingdata.jsp").forward(request, response);
    }

}
