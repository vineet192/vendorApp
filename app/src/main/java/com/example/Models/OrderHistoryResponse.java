package com.example.Models;

import java.util.List;

public class OrderHistoryResponse {

    private String no_order;
    private List<OrderDescriptionResponse> orders;

    public String getNo_order() {
        return no_order;
    }

    public void setNo_order(String no_order) {
        this.no_order = no_order;
    }

    public List<OrderDescriptionResponse> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDescriptionResponse> orders) {
        this.orders = orders;
    }
}

