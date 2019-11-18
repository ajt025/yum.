package com.example.yum.models;

import java.util.ArrayList;

public class Food_Review_Database {
    public String review_id;
    public String review_body;
    public String food_restaurant;
    public String food;
    public int upvote_count;
    public Integer downvote_count;
    public Integer rating;
    public String imageURL;

    public Food_Review_Database() {}

    public String getFood_restaurant() {
        return food_restaurant;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setFood_restaurant(String food_restaurant) {
        this.food_restaurant = food_restaurant;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getReview_body() {
        return review_body;
    }

    public void setReview_body(String review_body) {
        this.review_body = review_body;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getUpvote_count() {
        return upvote_count;
    }

    public void setUpvote_count(int upvote_count) {
        this.upvote_count = upvote_count;
    }

    public Integer getDownvote_count() {
        return downvote_count;
    }

    public void setDownvote_count(Integer downvote_count) {
        this.downvote_count = downvote_count;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
