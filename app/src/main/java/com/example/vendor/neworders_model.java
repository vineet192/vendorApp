package com.example.vendor;

public class neworders_model {

    String prod_name,prod_quan;

    public neworders_model(String prod_name, String prod_quan) {
        this.prod_name = prod_name;
        this.prod_quan = prod_quan;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_quan() {
        return prod_quan;
    }

    public void setProd_quan(String prod_quan) {
        this.prod_quan = prod_quan;
    }
}
