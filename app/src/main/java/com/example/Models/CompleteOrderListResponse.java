package com.example.Models;

import java.util.List;

public class CompleteOrderListResponse {

    private Integer no_prod;
    private List<ProductDescriptionResponse> products = null;

    public Integer getNo_prod() {
        return no_prod;
    }

    public void setNo_prod(Integer no_prod) {
        this.no_prod = no_prod;
    }

    public List<ProductDescriptionResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDescriptionResponse> products) {
        this.products = products;
    }
}
