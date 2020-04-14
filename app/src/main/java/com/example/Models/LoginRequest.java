package com.example.Models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    public void setDel_boy_id(String del_boy_id) {
        this.del_boy_id = del_boy_id;
    }

    public String getDel_boy_id() {
        return del_boy_id;
    }

    public LoginRequest(String del_boy_id) {
        this.del_boy_id = del_boy_id;
    }

    @SerializedName("del_boy_id")
    String del_boy_id;
}
