package com.example.vendor.MyIncome.Models_Income;

public class TotalIncomeModel {

    String month;
    String amount;

    public TotalIncomeModel(){

    }

    public TotalIncomeModel(String month, String amount) {
        this.month = month;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
