/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Transaction;

/**
 *
 * @author admin
 */
public class TransactionDAO extends DBContext {

    public boolean addTransaction(int userId, Transaction t) {
        String sql = "INSERT INTO Transactions (user_id, category_id, amount, date, note) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            stm.setInt(2, t.getCategoryId());
            stm.setBigDecimal(3, t.getAmount());
            stm.setDate(4, new java.sql.Date(t.getDate().getTime()));
            stm.setString(5, t.getNote());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("addTransaction Error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTransaction(Transaction t) {
        String sql = "UPDATE Transactions SET date = ?, category_id = ?, amount = ?, note = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setDate(1, new java.sql.Date(t.getDate().getTime()));
            stm.setInt(2, t.getCategoryId());
            stm.setBigDecimal(3, t.getAmount());
            stm.setString(4, t.getNote());
            stm.setInt(5, t.getId());
            stm.setInt(6, t.getUserId());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("updateTransaction Error: " + e.getMessage());
        }
        return false;
    }

    public void deleteTransaction(int userId, int transactionId) {
        String sql = "DELETE FROM Transactions WHERE id = ? AND user_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, transactionId);
            stm.setInt(2, userId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.err.println("deleteTransaction Error: " + e.getMessage());
        }
    }

    public void deleteTransactionsByGoalId(int goalId) {
        String sql = "DELETE FROM Transactions WHERE user_id IN (SELECT user_id FROM SavingsGoals WHERE goal_id = ?)";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, goalId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.err.println("deleteTransactionsByGoalId Error: " + e.getMessage());
        }
    }

    public BigDecimal getTotalIncome(int userId) {
        String sql = """
        SELECT COALESCE(SUM(t.amount), 0) 
        FROM Transactions t
        JOIN Categories c ON t.category_id = c.id
        WHERE t.user_id = ? AND c.type = 'income'
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (Exception e) {
            System.err.println("getTotalIncome Error: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpense(int userId) {
        String sql = """
        SELECT COALESCE(SUM(t.amount), 0) 
        FROM Transactions t
        JOIN Categories c ON t.category_id = c.id
        WHERE t.user_id = ? AND c.type = 'expense'
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (Exception e) {
            System.err.println("getTotalExpense Error: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public List<Transaction> getTransactionByUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT \n"
                + "    t.id,\n"
                + "    t.user_id,\n"
                + "    t.category_id,\n"
                + "    c.name AS category_name,\n"
                + "    c.type AS transaction_type,\n"
                + "    t.amount,\n"
                + "    t.date AS transaction_date,\n"
                + "    t.note AS description\n"
                + "FROM Transactions t\n"
                + "JOIN Categories c ON t.category_id = c.id\n"
                + "WHERE t.user_id = ?;";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getBigDecimal(6),
                            rs.getDate(7),
                            rs.getString(8),
                            rs.getString(5),
                            rs.getString(4)
                    );
                    list.add(t);
                }
            }
        } catch (Exception e) {
            System.err.println("getTransactionByUser Error: " + e.getMessage());
        }
        return list;
    }

    public Transaction getTransactionById(int userId, int transactionId) {
        String sql = "SELECT * FROM Transactions t JOIN Categories c ON t.category_id = c.id WHERE t.id = ? AND user_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, transactionId);
            stm.setInt(2, userId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                            rs.getBigDecimal(4), rs.getDate(5), rs.getString(6), rs.getString(9), rs.getString(8));
                    return t;
                }
            }
        } catch (Exception e) {
            System.err.println("getTransactionById Error: " + e.getMessage());
        }
        return null;
    }

    public List<Integer> getYearByUser(int userId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(date) FROM Transactions WHERE user_id = ? ORDER BY YEAR(date) ASC";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int year = rs.getInt(1);
                    list.add(year);
                }
            }
        } catch (Exception e) {
            System.err.println("getYearByUser Error: " + e.getMessage());
        }

        return list;
    }

    public Map<Integer, Integer> getMonthlyIncomeByYear(int userId, String year) {
        Map<Integer, Integer> map = new HashMap<>();
        String sql = "SELECT \n"
                + "    MONTH(date) AS month, \n"
                + "    SUM(amount) AS total_income\n"
                + "FROM Transactions\n"
                + "WHERE user_id = ? \n"
                + "    AND YEAR(date) = ? \n"
                + "    AND type = 'income' \n"
                + "GROUP BY MONTH(date)\n"
                + "ORDER BY MONTH(date);";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            stm.setString(2, year);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt(1);
                    int totalIncome = rs.getInt(2);
                    map.put(month, totalIncome);
                }
            }
        } catch (Exception e) {
            System.err.println("getMonthlyIncomeByYear Error: " + e.getMessage());
        }
        return map;
    }

    public Map<Integer, Map<String, Integer>> getMonthlyDataByYear(int userId, int year) {
        Map<Integer, Map<String, Integer>> monthlyData = new HashMap<>();

        String sql = "SELECT MONTH(t.date) AS month, "
                + "SUM(CASE WHEN c.type = 'income' THEN t.amount ELSE 0 END) AS total_income, "
                + "SUM(CASE WHEN c.type = 'expense' THEN t.amount ELSE 0 END) AS total_expense "
                + "FROM Transactions t "
                + "JOIN Categories c ON t.category_id = c.id "
                + "WHERE t.user_id = ? AND YEAR(t.date) = ? "
                + "GROUP BY MONTH(t.date) "
                + "ORDER BY MONTH(t.date)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int month = rs.getInt("month");
                int income = rs.getInt("total_income");
                int expense = rs.getInt("total_expense");

                Map<String, Integer> data = new HashMap<>();
                data.put("income", income);
                data.put("expense", expense);
                data.put("balance", income - expense);

                monthlyData.put(month, data);
            }
        } catch (Exception e) {
            System.err.println("getMonthlyDataByYear Error: " + e.getMessage());
        }
        return monthlyData;
    }

    public Map<Integer, Map<String, Integer>> getMonthlyDataAll(int userId) {
        Map<Integer, Map<String, Integer>> monthlyData = new HashMap<>();

        String sql = "SELECT MONTH(t.date) AS month, "
                + "SUM(CASE WHEN c.type = 'income' THEN t.amount ELSE 0 END) AS total_income, "
                + "SUM(CASE WHEN c.type = 'expense' THEN t.amount ELSE 0 END) AS total_expense "
                + "FROM Transactions t "
                + "JOIN Categories c ON t.category_id = c.id "
                + "WHERE t.user_id = ? "
                + "GROUP BY MONTH(t.date) "
                + "ORDER BY MONTH(t.date)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int month = rs.getInt("month");
                int income = rs.getInt("total_income");
                int expense = rs.getInt("total_expense");

                Map<String, Integer> data = new HashMap<>();
                data.put("income", income);
                data.put("expense", expense);
                data.put("balance", income - expense);

                monthlyData.put(month, data);
            }
        } catch (Exception e) {
            System.err.println("getMonthlyDataAll Error: " + e.getMessage());
        }
        return monthlyData;
    }

    public Map<String, Double> getCategoryAndValueByYear(int userId, String type, String year) {
        Map<String, Double> maps = new HashMap<>();
        String sql = "SELECT \n"
                + "    c.name AS category_name,\n"
                + "    c.type,\n"
                + "    SUM(t.amount) AS total_amount\n"
                + "FROM Transactions t\n"
                + "JOIN Categories c ON t.category_id = c.id\n"
                + "WHERE t.user_id = ? AND YEAR(t.date) = ? AND c.type = ?\n"
                + "GROUP BY c.name, c.type\n"
                + "ORDER BY c.type, total_amount DESC;";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            stm.setString(2, year);
            stm.setString(3, type);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                maps.put(rs.getString(1), rs.getDouble(3));
            }
        } catch (Exception e) {
            System.err.println("getCategoryAndValueByYear Error: " + e.getMessage());
        }
        return maps;
    }

    public Map<String, Double> getCategoryAndValueAll(int userId, String type) {
        Map<String, Double> categoryData = new HashMap<>();

        String sql = "SELECT c.name AS category_name, SUM(t.amount) AS total_amount "
                + "FROM Transactions t "
                + "JOIN Categories c ON t.category_id = c.id "
                + "WHERE t.user_id = ? AND c.type = ? "
                + "GROUP BY c.name "
                + "ORDER BY total_amount DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categoryData.put(rs.getString("category_name"), rs.getDouble("total_amount"));
            }
        } catch (Exception e) {
            System.err.println("getCategoryAndValueAll Error: " + e.getMessage());
        }

        return categoryData;
    }

}
