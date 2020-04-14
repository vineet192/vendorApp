package com.example.Models;

import java.io.Serializable;
import java.util.List;

public class OrderDescriptionResponse implements Serializable {

    private String order_id;
    private List<ProductDescriptionResponse> items;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<ProductDescriptionResponse> getItems() {
        return items;
    }

    public void setItems(List<ProductDescriptionResponse> items) {
        this.items = items;
    }
}
