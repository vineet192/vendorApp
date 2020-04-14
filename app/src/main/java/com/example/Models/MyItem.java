package com.example.Models;

public class MyItem {

    private String name;
    private String category;
    private String price;
    private String imageUrl;
    private boolean check=false;

    public MyItem(String name, String category, String price, String imageUrl, boolean check) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean checked) {
        this.check = checked;
    }
}
