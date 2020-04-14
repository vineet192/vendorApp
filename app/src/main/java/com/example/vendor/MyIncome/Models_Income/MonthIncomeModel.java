package com.example.vendor.MyIncome.Models_Income;

public class MonthIncomeModel {

    String orderId;
    String amount;
    String date;

    public MonthIncomeModel(){

    }

    public MonthIncomeModel(String orderId, String amount, String date) {
        this.orderId = orderId;
        this.amount = amount;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
