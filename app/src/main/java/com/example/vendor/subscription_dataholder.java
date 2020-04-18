package com.example.vendor;

import org.json.JSONArray;

public class subscription_dataholder {
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    String orderID,startdate,enddate,days,deliveryboy_arivingtime,time
            ,total_price,timer,deliveryboy_phone,deliveryboy_otp,product_name,product_price,
            prod_quantity,prod_total_price,package_status,subscription_type;

    int duration;

    public String getTimer() {
        return timer;
    }

    public String getDeliveryboy_arivingtime() {
        return deliveryboy_arivingtime;
    }

    public JSONArray getSubsTotalOrders() {
        return SubsTotalOrders;
    }

    public void setSubsTotalOrders(JSONArray subsTotalOrders) {
        SubsTotalOrders = subsTotalOrders;
    }

    public JSONArray getSubsQuantity() {
        return SubsQuantity;
    }

    public void setSubsQuantity(JSONArray subsQuantity) {
        SubsQuantity = subsQuantity;
    }

    JSONArray SubsTotalOrders;
    JSONArray SubsQuantity;

    public  subscription_dataholder(){}
    public subscription_dataholder(String orderID, String startdate, String enddate, String total_price,String subscription_type) {
        this.orderID = orderID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.total_price = total_price;
        this.subscription_type = subscription_type;

    }

    public subscription_dataholder(String product_name, String product_price) {
        this.product_name = product_name;
        this.product_price = product_price;
    }

    public subscription_dataholder(String product_name, String prod_quantity, String prod_total_price) {
        this.product_name = product_name;
        this.prod_quantity = prod_quantity;
        this.prod_total_price = prod_total_price;
    }

    public subscription_dataholder(String orderID, String startdate, String enddate, String total_price, String deliveryboy_arivingtime, String deliveryboy_phone, String deliveryboy_otp) {
        this.orderID = orderID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.total_price = total_price;
//        this.deliveryboy_name = deliveryboy_name;
        this.deliveryboy_arivingtime = deliveryboy_arivingtime;
        this.deliveryboy_phone = deliveryboy_phone;
        this.deliveryboy_otp = deliveryboy_otp;
    }

    public subscription_dataholder(String orderID, String startdate, String enddate, String total_price, String deliveryboy_arivingtime, String deliveryboy_phone, String deliveryboy_otp,String subscription_type) {
        this.orderID = orderID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.total_price = total_price;
        this.subscription_type = subscription_type;
        this.deliveryboy_arivingtime = deliveryboy_arivingtime;
        this.deliveryboy_phone = deliveryboy_phone;
        this.deliveryboy_otp = deliveryboy_otp;
    }

    public subscription_dataholder(String orderID, String startdate, String enddate, String total_price,String package_status,String subscription_type) {
        this.orderID = orderID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.total_price = total_price;
        this.package_status = package_status;
        this.subscription_type = subscription_type;
    }


    public String getOrderID() {
        return orderID;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getSubscription_type() {
        return subscription_type;
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

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
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

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }
}
