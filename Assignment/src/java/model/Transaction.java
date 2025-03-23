/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author admin
 */
public class Transaction {

    private int id;
    private int userId;
    private int categoryId;
    private BigDecimal amount;
    private Date date;
    private String note;
    private String transactionType;
    private String categoryName;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, Date date, String note, String transactionType, String categoryName, int categoryId) {
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.transactionType = transactionType;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public Transaction(int id, int userId, int categoryId, BigDecimal amount, Date date, String note) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public Transaction(int id, int userId, int categoryId, BigDecimal amount, Date date, String note, String transactionType, String categoryName) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.transactionType = transactionType;
        this.categoryName = categoryName;
    }

    public Transaction(Integer userId, int categoryId, BigDecimal amount, Date date, String note, String transactionType, String categoryName) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.transactionType = transactionType;
        this.categoryName = categoryName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Transaction{" + "id=" + id + ", userId=" + userId + ", categoryId=" + categoryId + ", amount=" + amount + ", date=" + date + ", note=" + note + ", transactionType=" + transactionType + ", categoryName=" + categoryName + '}';
    }

}
