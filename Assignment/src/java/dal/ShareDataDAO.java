/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ShareData;

/**
 *
 * @author admin
 */
public class ShareDataDAO extends DBContext {

    public List<ShareData> getSharedDataByUserId(int userId) {
        List<ShareData> list = new ArrayList<>();
        String sql = "SELECT u.username, s.income, s.expense "
                + "FROM SharedData s "
                + "JOIN Users u ON s.owner_id = u.id "
                + // Lấy username từ user_id
                "WHERE s.shared_with_id = ?"; // Điều kiện là shared_with_id

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ShareData sharedData = new ShareData();
                sharedData.setUsername(rs.getString("username")); // Lấy username từ Users
                sharedData.setIncome(rs.getBigDecimal("income"));
                sharedData.setExpense(rs.getBigDecimal("expense"));
                list.add(sharedData);
            }
        } catch (Exception e) {
            System.out.println("getSharedDataByUserId Error: " + e.getMessage());
        }
        return list;
    }

}
