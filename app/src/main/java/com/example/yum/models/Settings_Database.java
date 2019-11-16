package com.example.yum.models;


import com.example.yum.SettingActivity;

public class Settings_Database  {
    public boolean vegetarian;
    public boolean vegan;
    public boolean location;

    public Settings_Database () {
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
