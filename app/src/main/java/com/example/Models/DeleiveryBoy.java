package com.example.Models;

import java.io.Serializable;

public class DeleiveryBoy implements Serializable {

    private String order_id;
    private String vendor_phone;
    private String del_boy_name;
    private String del_boy_phone;
    String order_type;
    String PhotoUrl;

    public Boolean getArrived() {
        return isArrived;
    }

    public void setArrived(Boolean arrived) {
        isArrived = arrived;
    }

    Boolean isArrived;

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getVendor_phone() {
        return vendor_phone;
    }

    public void setVendor_phone(String vendor_phone) {
        this.vendor_phone = vendor_phone;
    }

    public String getDel_boy_name() {
        return del_boy_name;
    }

    public void setDel_boy_name(String del_boy_name) {
        this.del_boy_name = del_boy_name;
    }

    public String getDel_boy_phone() {
        return del_boy_phone;
    }

    public void setDel_boy_phone(String del_boy_phone) {
        this.del_boy_phone = del_boy_phone;
    }
}
