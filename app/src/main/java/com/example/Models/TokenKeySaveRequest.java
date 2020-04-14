package com.example.Models;

public class TokenKeySaveRequest {
    public String getVendor_phone() {
        return vendor_phone;
    }

    public void setVendor_phone(String vendor_phone) {
        this.vendor_phone = vendor_phone;
    }

    public String getVendor_fcm_token() {
        return vendor_fcm_token;
    }

    public TokenKeySaveRequest(String vendor_phone, String vendor_fcm_token) {
        this.vendor_phone = vendor_phone;
        this.vendor_fcm_token = vendor_fcm_token;
    }

    public void setVendor_fcm_token(String vendor_fcm_token) {
        this.vendor_fcm_token = vendor_fcm_token;
    }

    String vendor_phone;
    String vendor_fcm_token;
}
