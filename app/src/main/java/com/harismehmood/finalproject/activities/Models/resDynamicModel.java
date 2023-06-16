package com.harismehmood.finalproject.activities.Models;

public class resDynamicModel {
    private String name,price ;
    public resDynamicModel(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }



}
