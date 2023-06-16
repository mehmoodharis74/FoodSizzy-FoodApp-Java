package com.harismehmood.finalproject.activities.common;

import android.graphics.Bitmap;

public class serviceModel {
    private Bitmap image;
    private String name;
    private String description;

    public serviceModel(Bitmap image, String name, String description) {
        this.image = image;
        this.name = name;
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
