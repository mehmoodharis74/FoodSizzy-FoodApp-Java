package com.harismehmood.finalproject.activities.Models;

public class cartModel {
    public String getUserEmail() {
        return userEmail;
    }

    private String userEmail,name,price,quantity ;
    public cartModel(String email, String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.userEmail = email;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public String getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
