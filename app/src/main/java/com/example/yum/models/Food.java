package com.example.yum.models;

import org.parceler.Parcel;

@Parcel

/*
* Food object that will be used to populate the
* recycler view in Explore Page using
* the reviews in database
*
* */
public class Food {

    public String imgPath;
    public String name;
    public String restaurant;

    public Food() {

    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
