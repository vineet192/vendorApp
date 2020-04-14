package com.example.Models;

import java.io.Serializable;
import java.util.List;

public class NewOrders implements Serializable {

    private String oreder_Id;
    private int order_Total;
    private List<MyItem> items;
    private List<MyHistoryItems> historyItems;
    private String stat;

    public String getOreder_Id() {
        return oreder_Id;
    }

    public void setOreder_Id(String oreder_Id) {
        this.oreder_Id = oreder_Id;
    }

    public int getOrder_Total() {
        return order_Total;
    }

    public void setOrder_Total(int order_Total) {
        this.order_Total = order_Total;
    }

    public List<MyItem> getItems() {
        return items;
    }

    public void setItems(List<MyItem> items) {
        this.items = items;
    }

    public List<MyHistoryItems> getHistoryItems() {
        return historyItems;
    }

    public void setHistoryItems(List<MyHistoryItems> historyItems) {
        this.historyItems = historyItems;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public NewOrders(String oreder_Id, int order_Total, List<MyItem> items) {
        this.oreder_Id = oreder_Id;
        this.order_Total = order_Total;
        this.items = items;
    }

    public NewOrders(String oreder_Id, int order_Total, List<MyHistoryItems> historyItems, String stat) {
        this.oreder_Id = oreder_Id;
        this.order_Total = order_Total;
        this.historyItems = historyItems;
        this.stat = stat;
    }
}
