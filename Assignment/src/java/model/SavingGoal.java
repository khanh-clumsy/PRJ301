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
public class SavingGoal {

    private int goalId;
    private int userId;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private Date deadline;
    private String status;

    public SavingGoal() {
    }

    public SavingGoal(int goalId, int userId, String goalName, BigDecimal targetAmount, BigDecimal currentAmount, Date deadline) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
    }

    public SavingGoal(String goalName, BigDecimal targetAmount, Date deadline, BigDecimal initialAmount) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
    }

    public SavingGoal(int userId, String goalName, BigDecimal targetAmount, BigDecimal currentAmount, Date deadline) {
        this.userId = userId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
    }

    public SavingGoal(int goalId, int userId, String goalName, BigDecimal targetAmount, BigDecimal currentAmount, Date deadline, String status) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = status;
    }

    public SavingGoal(int goalId, String goalName, BigDecimal targetAmount, BigDecimal currentAmount, Date deadline, String status) {
        this.goalId = goalId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = status;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SavingGoal{" + "goalId=" + goalId + ", userId=" + userId + ", goalName=" + goalName + ", targetAmount=" + targetAmount + ", currentAmount=" + currentAmount + ", deadline=" + deadline + '}';
    }

}
