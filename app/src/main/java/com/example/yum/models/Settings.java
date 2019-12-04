package com.example.yum.models;


/*
* Settings of User that will currently be updated in the
* Firebase for unique users. It will keep track and help filter
* out foods in the explore page if the food is not vegan,
* vegetarian, or the location is too far away.
*
*
* */
public class Settings {
    public boolean vegetarian;
    public boolean vegan;
    public boolean location;


    public Settings() {
        vegan = false;
        vegetarian = false;
        location = false;
    }


    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }

}
