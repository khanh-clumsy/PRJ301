/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class ShareData {

    private int userId, otherUserId;
    private String username;
    private BigDecimal income, expense;

    public ShareData() {
    }

    public ShareData(int userId, int otherUserId, BigDecimal income, BigDecimal expense) {
        this.userId = userId;
        this.otherUserId = otherUserId;
        this.income = income;
        this.expense = expense;
    }

    public ShareData(int userId, int otherUserId, String username, BigDecimal income, BigDecimal expense) {
        this.userId = userId;
        this.otherUserId = otherUserId;
        this.username = username;
        this.income = income;
        this.expense = expense;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
