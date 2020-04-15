package com.example.vendor;

public class neworders_model {

    String prod_name,prod_quan;
    String day,time,temp;


    public neworders_model(String temp, String day, String time) {
        this.temp = temp;
        this.day = day;
        this.time = time;
    }

    public neworders_model(String prod_name, String prod_quan) {
        this.prod_name = prod_name;
        this.prod_quan = prod_quan;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_quan() {
        return prod_quan;
    }

    public void setProd_quan(String prod_quan) {
        this.prod_quan = prod_quan;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
