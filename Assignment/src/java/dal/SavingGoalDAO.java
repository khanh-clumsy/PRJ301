package dal;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.SavingGoal;

public class SavingGoalDAO extends DBContext {

    public BigDecimal getCurrentAmount(int goalId) {
        String query = "SELECT current_amount FROM SavingsGoals WHERE goal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, goalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("current_amount");
            }
        } catch (SQLException e) {
            System.err.println("getCurrentAmount Error: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTargetAmount(int goalId) {
        String query = "SELECT target_amount FROM SavingsGoals WHERE goal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, goalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("target_amount");
            }
        } catch (SQLException e) {
            System.err.println("getTargetAmount Error: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public void updateExpiredGoals(LocalDate today) {
        String query = "UPDATE SavingsGoals SET status = 'Not Completed' WHERE deadline < ? AND status = 'In Progress'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(today));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateExpiredGoals Error: " + e.getMessage());
        }
    }

    public SavingGoal getGoalById(int userId, int goalId) {
        String sql = "SELECT * FROM SavingsGoals WHERE user_id = ? AND goal_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            stm.setInt(2, goalId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return new SavingGoal(
                            goalId,
                            userId,
                            rs.getString("goal_name"),
                            rs.getBigDecimal("target_amount"),
                            rs.getBigDecimal("current_amount"),
                            rs.getDate("deadline"),
                            rs.getString("status") // Lấy trạng thái
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("getGoalById Error: " + e.getMessage());
        }
        return null;
    }

    public List<SavingGoal> getAllGoalsByUserId(int userId) {
        String sql = "SELECT * FROM SavingsGoals WHERE user_id = ?";
        List<SavingGoal> list = new ArrayList<>();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    SavingGoal s = new SavingGoal(
                            rs.getInt("goal_id"),
                            userId,
                            rs.getString("goal_name"),
                            rs.getBigDecimal("target_amount"),
                            rs.getBigDecimal("current_amount"),
                            rs.getDate("deadline"),
                            rs.getString("status") // Lấy trạng thái
                    );
                    list.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("getAllGoalsByUserId Error: " + e.getMessage());
        }
        return list;
    }

    public boolean addGoal(int userId, SavingGoal s) {
        String sql = "INSERT INTO SavingsGoals (user_id, goal_name, target_amount, current_amount, deadline, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            stm.setString(2, s.getGoalName());
            stm.setBigDecimal(3, s.getTargetAmount());
            stm.setBigDecimal(4, s.getCurrentAmount());
            stm.setDate(5, new java.sql.Date(s.getDeadline().getTime()));
            stm.setString(6, s.getStatus());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("addGoal Error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateGoal(SavingGoal s) {
        String sql = "UPDATE SavingsGoals SET goal_name = ?, target_amount = ?, current_amount = ?, deadline = ?, status = ? WHERE goal_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, s.getGoalName());
            stm.setBigDecimal(2, s.getTargetAmount());
            stm.setBigDecimal(3, s.getCurrentAmount());
            stm.setDate(4, new java.sql.Date(s.getDeadline().getTime()));
            stm.setString(5, s.getStatus());
            stm.setInt(6, s.getGoalId());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("updateGoal Error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteGoal(int goalId) {
        String sql = "DELETE FROM SavingsGoals WHERE goal_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, goalId);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("deleteGoal Error: " + e.getMessage());
        }
        return false;
    }

    public boolean addContribution(int userId, int goalId, BigDecimal amount, Date date, String note) {
        String updateGoalSql = "UPDATE SavingsGoals SET current_amount = current_amount + ? WHERE goal_id = ?";
        String insertTransactionSql = "INSERT INTO Transactions (user_id, category_id, amount, date, note) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement updateStm = connection.prepareStatement(updateGoalSql); PreparedStatement insertStm = connection.prepareStatement(insertTransactionSql)) {

            updateStm.setBigDecimal(1, amount);
            updateStm.setInt(2, goalId);
            boolean success = updateStm.executeUpdate() > 0;

            if (success) {
                updateGoalStatus(goalId);
                insertStm.setInt(1, userId);
                insertStm.setInt(2, 11);
                insertStm.setBigDecimal(3, amount);
                insertStm.setDate(4, date);
                insertStm.setString(5, note);
                insertStm.executeUpdate();
            }
            return success;
        } catch (Exception e) {
            System.err.println("addContribution Error: " + e.getMessage());
        }
        return false;
    }

    public boolean withdrawContribution(int userId, int goalId, BigDecimal amount, Date date, String note) {
        String updateGoalSql = "UPDATE SavingsGoals SET current_amount = current_amount - ? WHERE goal_id = ? AND current_amount >= ?";
        String insertTransactionSql = "INSERT INTO Transactions (user_id, category_id, amount, date, note) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement updateStm = connection.prepareStatement(updateGoalSql); PreparedStatement insertStm = connection.prepareStatement(insertTransactionSql)) {

            updateStm.setBigDecimal(1, amount);
            updateStm.setInt(2, goalId);
            updateStm.setBigDecimal(3, amount);
            boolean success = updateStm.executeUpdate() > 0;

            if (success) {
                updateGoalStatus(goalId);

                insertStm.setInt(1, userId);
                insertStm.setInt(2, 10);
                insertStm.setBigDecimal(3, amount);
                insertStm.setDate(4, date);
                insertStm.setString(5, note);
                insertStm.executeUpdate();
            }
            return success;
        } catch (Exception e) {
            System.err.println("withdrawContribution Error: " + e.getMessage());
        }
        return false;
    }

    public void updateGoalStatus(int goalId) {
        String sql = "UPDATE SavingsGoals "
                + "SET status = CASE "
                + "WHEN current_amount >= target_amount THEN 'Completed' "
                + "WHEN deadline <= GETDATE() THEN 'Not Completed' "
                + "ELSE 'In Progress' "
                + "END "
                + "WHERE goal_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, goalId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.err.println("updateGoalStatus Error: " + e.getMessage());
        }
    }
}
