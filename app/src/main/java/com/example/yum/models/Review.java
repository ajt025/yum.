package com.example.yum.models;

import java.util.ArrayList;


/*
* Review object that is created when a user
* submits a review. This object is uploaded
* to the Firebase database and will be used
* to display Food Items and Food Profile
* Pages
*
* */
public class Review {
    public String reviewId;
    public String userId;
    public String restaurant;
    public String imgPath;
    public String reviewTitle;
    public String reviewBody;
    public String food;
    public ArrayList<String> upvoteCount;
    public ArrayList<String> downvoteCount;
    public double rating;



    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurantId) {
        this.restaurant = restaurantId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public ArrayList<String> getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(ArrayList<String> upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public ArrayList<String> getDownvoteCount() {
        return downvoteCount;
    }

    public void setDownvoteCount(ArrayList<String> downvoteCount) {
        this.downvoteCount = downvoteCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public Review() {

    }


}
