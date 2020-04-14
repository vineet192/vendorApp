package com.example.Models;

public class MyHistoryItems {
    private String name;
    private String date;
    private String price;
    private String imageUrl;
    private String status;

    public MyHistoryItems(String name, String date, String price, String imageUrl, String status) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
