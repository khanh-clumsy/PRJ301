/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Users;

/**
 *
 * @author admin
 */
public class UsersDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public List<Users> getAllUsers() {
        List<Users> userList = new ArrayList<>();
        String sql = "SELECT id, username FROM Users";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("getAllUsers Error: " + e.getMessage());
        }
        return userList;
    }

    public boolean shareData(int ownerId, int sharedWithId, BigDecimal income, BigDecimal expense) {
        String sql = "INSERT INTO SharedData (owner_id, shared_with_id, income, expense) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, ownerId);
            stm.setInt(2, sharedWithId);
            stm.setBigDecimal(3, income);
            stm.setBigDecimal(4, expense);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("shareData Error: " + e.getMessage());
        }
        return false;
    }

    public Users login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ? AND isActive = 1";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            rs = stm.executeQuery();
            while (rs.next()) {
                String username1 = rs.getString(2);
                String password1 = rs.getString(4);
                Users u = new Users(username1, password1);
                return u;
            }
        } catch (SQLException e) {
            System.out.println("login " + e.getMessage());
        }
        return null;
    }

    public void addUser(Users u) {
        String sql = "INSERT INTO Users (username, email, password) VALUES (?,?,?)";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, u.getUsername());
            stm.setString(2, u.getEmail());
            stm.setString(3, u.getPassword());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("addUser " + e.getMessage());
        }
    }

    public boolean getUser(String username) {
        String query = "select * from Users where username = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("getUser " + e.getMessage());
        }
        return false;
    }

    public int getUserIdByUsername(String username) {
        int userId = -1;
        String sql = "SELECT id FROM Users WHERE username = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                userId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("getUserIdByUsername: " + e.getMessage());
        }
        return userId;
    }

    public Users getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        Users user = null;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                user = new Users();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setBalance(rs.getBigDecimal(5));
                user.setCreatedTime(rs.getTimestamp(6));
                user.setActive(rs.getBoolean(7));
            }
        } catch (Exception e) {
            System.out.println("getUserById Error: " + e.getMessage());
        }
        return user;
    }

    public Users getUsersByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Users u = new Users(rs.getInt(1),
                        username, rs.getString(3), rs.getString(4), rs.getBigDecimal(5),
                        rs.getTimestamp(6), rs.getBoolean(7));
                return u;
            }
        } catch (SQLException e) {
            System.out.println("getUsersByUsername: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUsersAllInfo(Users newUser) {
        String sql = "UPDATE Users SET email = ?, password = ? WHERE id = ?;";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newUser.getEmail());
            stm.setString(2, newUser.getPassword());
            stm.setInt(3, newUser.getId());

            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.err.println("updateUsersAllInfo Error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUsers(int id) {
        String sql = "UPDATE users SET isActive = 0 WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);

            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.err.println("deleteUsers Error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUserBalance(int userId, BigDecimal balance) {
        String sql = "UPDATE Users SET balance = ? WHERE id = ?;";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBigDecimal(1, balance);
            stm.setInt(2, userId);

            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.err.println("updateUserBalance Error: " + e.getMessage());
        }
        return false;
    }
}
