package com.example.vendor;

import com.google.gson.annotations.SerializedName;

public class LoginRequestVendor
{
    public String getVendorID()
    {
        return vendorID;
    }

    public void setVendorID(String vendorID)
    {
        this.vendorID = vendorID;
    }

    @SerializedName("vendor_id")
    private String vendorID;
}
