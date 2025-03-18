/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Category;

/**
 *
 * @author admin
 */
public class CategoryDAO extends DBContext {

    public List<Category> getCategoryByType(String type) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories WHERE type = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, type);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category(rs.getInt(1), rs.getString(2), rs.getString(3));
                    list.add(c);
                }
            }
        } catch (Exception e) {
            System.err.println("getCategoryByType Error: " + e.getMessage());
        }
        return list;
    }

    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM Categories WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category(rs.getInt(1), rs.getString(2), rs.getString(3));
                    return c;
                }
            }
        } catch (Exception e) {
            System.err.println("getCategoryById Error: " + e.getMessage());
        }
        return null;
    }
}
