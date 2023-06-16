package com.harismehmood.finalproject.activities.Models;

public class StaticRvModel {
    private String name;
    private String price;
    private int image;

    public StaticRvModel(String name,int image) {
        this.name = name;
        //this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
