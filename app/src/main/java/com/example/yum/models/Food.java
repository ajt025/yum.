package com.example.yum.models;

import org.parceler.Parcel;

@Parcel
public class Food {

    public String imgPath;
    public String name;

    public Food() {

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
