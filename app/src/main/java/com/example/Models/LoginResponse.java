package com.example.Models;

public class LoginResponse
{
    private String vendor_name;

    private String vendor_id;

    private String found;

    private String vendor_phone;

    private String vendor_lat;

    private String vendor_long;

    private String imagePath;

    private String vendor_city;

    private String vendor_address;

    public String getVendor_id()
    {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id)
    {
        this.vendor_id = vendor_id;
    }

    public String getFound()
    {
        return found;
    }

    public void setFound(String found)
    {
        this.found = found;
    }

    public String getVendor_phone()
    {
        return vendor_phone;
    }

    public void setVendor_phone(String vendor_phone)
    {
        this.vendor_phone = vendor_phone;
    }

    public String getVendor_name()
    {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name)
    {
        this.vendor_name = vendor_name;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getVendor_city()
    {
        return vendor_city;
    }

    public void setVendor_city(String vendor_city)
    {
        this.vendor_city = vendor_city;
    }

    public String getVendor_address()
    {
        return vendor_address;
    }

    public void setVendor_address(String vendor_address)
    {
        this.vendor_address = vendor_address;
    }

    public String getVendor_lat() {
        return vendor_lat;
    }

    public void setVendor_lat(String vendor_lat) {
        this.vendor_lat = vendor_lat;
    }

    public String getVendor_long() {
        return vendor_long;
    }

    public void setVendor_long(String vendor_long) {
        this.vendor_long = vendor_long;
    }
}
