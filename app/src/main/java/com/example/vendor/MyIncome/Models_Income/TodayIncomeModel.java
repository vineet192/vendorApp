package com.example.vendor.MyIncome.Models_Income;

public class TodayIncomeModel {

    String orderId;
    String amount;

    public TodayIncomeModel(){

    }

    public TodayIncomeModel(String orderId, String amount) {
        this.orderId = orderId;
        this.amount = amount;
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
}
