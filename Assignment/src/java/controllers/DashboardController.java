/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import com.google.gson.Gson;
import dal.CategoryDAO;
import dal.TransactionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import model.Category;
import model.Transaction;

/**
 *
 * @author admin
 */
public class DashboardController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

            default:
                response.sendRedirect("login");
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
            default:
                response.sendRedirect("login");
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void showTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        Integer userId = (Integer) s.getAttribute("id");
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }
        //Code
        CategoryDAO cda = new CategoryDAO();
        List<Category> incomeList = cda.getCategoryByType("income");
        List<Category> expenseList = cda.getCategoryByType("expense");

        TransactionDAO tda = new TransactionDAO();

        String modeStr = request.getParameter("mode");
        int mode = (modeStr != null) ? Integer.parseInt(modeStr) : 0;

        if (mode == 1) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            Transaction t = tda.getTransactionById(userId, transactionId);
            request.setAttribute("trans", t);
        }

        if (mode == 2) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            tda.deleteTransaction(userId, transactionId);
        }

        List<Transaction> transactionList = tda.getTransactionByUser(userId);
        request.setAttribute("incomeCategories", incomeList);
        request.setAttribute("expenseCategories", expenseList);
        request.setAttribute("transactions", transactionList);
        //Tra ve transaction.jsp
        request.getRequestDispatcher("transaction.jsp").forward(request, response);
    }

    private void handleTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        CategoryDAO cda = new CategoryDAO();
        TransactionDAO tda = new TransactionDAO();

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
        Date transactionDate = null;
        Category c = cda.getCategoryById(categoryId);

        try {
            if (selectedDate != null && !selectedDate.isEmpty()) {
                transactionDate = java.sql.Date.valueOf(selectedDate);
            }
        } catch (Exception e) {
            System.out.println("Date format error! " + e.getMessage());
        }

        Transaction t = new Transaction(userId, categoryId, amount, transactionDate, description, selectedType, c.getName());

        if (request.getParameter("add") != null) {
            boolean added = tda.addTransaction(userId, t);
            if (added) {
                request.setAttribute("msg", "Added successfully!");
            }
            request.setAttribute("trans", t);
        }

        if (request.getParameter("update") != null) {
            int transactionId = Integer.parseInt(request.getParameter("code"));
            t.setId(transactionId);
            boolean updated = tda.updateTransaction(t);
            if (updated) {
                request.setAttribute("msg", "Updated successfully!");
            } else {
                request.setAttribute("msg", "Update error!");
            }
            request.setAttribute("trans", t);
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
            tda.deleteTransaction(userId, transactionId);
        }

        List<Transaction> transactionList = tda.getTransactionByUser(userId);
        request.setAttribute("transactions", transactionList);
        request.getRequestDispatcher("transaction.jsp").forward(request, response);
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

    private void handleReport(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("report.jsp").forward(request, response);

    }

    private void showReport(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("report.jsp").forward(request, response);
    }
}
