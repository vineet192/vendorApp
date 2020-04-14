package com.example.vendor;

public class order_dataholder {

    public order_dataholder(){}

    String orderID,date,time,total_price,timer,deliveryboy_arivingtime,
            deliveryboy_phone,deliveryboy_otp,product_name,product_price,prod_quantity,prod_total_price,package_status;

    public order_dataholder(String product_name, String prod_quantity, String prod_total_price) {
        this.product_name = product_name;
        this.prod_quantity = prod_quantity;
        this.prod_total_price = prod_total_price;
    }

    public order_dataholder(String orderID, String date, String time, String total_price) {
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.total_price = total_price;
    }

    public order_dataholder(String orderID, String date, String time, String total_price,String package_status) {
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.total_price = total_price;
        this.package_status = package_status;
    }

    public order_dataholder(String product_name, String product_price) {
        this.product_name = product_name;
        this.product_price = product_price;
    }

    public order_dataholder(String orderID, String date, String time, String total_price, String deliveryboy_arivingtime, String deliveryboy_phone, String deliveryboy_otp) {
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.total_price = total_price;
//        this.deliveryboy_name = deliveryboy_name;
        this.deliveryboy_arivingtime = deliveryboy_arivingtime;
        this.deliveryboy_phone = deliveryboy_phone;
        this.deliveryboy_otp = deliveryboy_otp;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getTimer() {
        return timer;
    }

//    public String getDeliveryboy_name() {
//        return deliveryboy_name;
//    }

    public String getDeliveryboy_arivingtime() {
        return deliveryboy_arivingtime;
    }

    public String getDeliveryboy_phone() {
        return deliveryboy_phone;
    }

    public String getDeliveryboy_otp() {
        return deliveryboy_otp;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProd_quantity() {
        return prod_quantity;
    }

    public String getProd_total_price() {
        return prod_total_price;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public void setDeliveryboy_arivingtime(String deliveryboy_arivingtime) {
        this.deliveryboy_arivingtime = deliveryboy_arivingtime;
    }

    public void setDeliveryboy_phone(String deliveryboy_phone) {
        this.deliveryboy_phone = deliveryboy_phone;
    }

    public void setDeliveryboy_otp(String deliveryboy_otp) {
        this.deliveryboy_otp = deliveryboy_otp;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public void setProd_quantity(String prod_quantity) {
        this.prod_quantity = prod_quantity;
    }

    public void setProd_total_price(String prod_total_price) {
        this.prod_total_price = prod_total_price;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }
}
