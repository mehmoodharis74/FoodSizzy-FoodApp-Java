package com.harismehmood.finalproject.activities.Models;

public class DynamicRvModel {
    private String name;
    private String id;
    private String parking;
    private String ratings;
    private String no_of_reviews;

    public String getLocation() {
        return location;
    }

    private String location;
    private int image;
    public DynamicRvModel(String name, int image, String id, String parking, String ratings, String no_of_reviews, String location) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.parking = parking;
        this.ratings = ratings;
        this.no_of_reviews = no_of_reviews;
        this.location = location;
    }

    public float getRatings() {
        return Float.parseFloat(ratings);
    }

    public String getNo_of_reviews() {
        return no_of_reviews;
    }

    public String getId() {
        return id;
    }

    public String getParking() {
        return parking;
    }

    public String getName() {
        return name;
    }
    public int getImage() {
        return image;
    }
    public void setName(String name) {
        this.name = name;
    }



}
