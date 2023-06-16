package com.harismehmood.finalproject.activities.Models;

public class resStaticModel {
    private String name;
    private String price;
    private int image, pos;

    public resStaticModel(String name, int image, int pos) {
        this.name = name;
        //this.price = price;
        this.image = image;
        this.pos = pos;
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

    public int getPosition() {
        return pos;
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
