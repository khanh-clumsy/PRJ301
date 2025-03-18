/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Users;

/**
 *
 * @author admin
 */
public class UsersDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public Users login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
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
        String sql = "UPDATE Users SET email = ?, password = ?, balance = ? WHERE id = ?;";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newUser.getEmail());
            stm.setString(2, newUser.getPassword());
            stm.setBigDecimal(3, newUser.getBalance());
            stm.setInt(4, newUser.getId());

            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.err.println("updateUsersAllInfo Error: " + e.getMessage());
        }
        return false;
    }

    public void deleteUsers(int id) {

    }
}
