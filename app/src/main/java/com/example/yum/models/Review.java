package com.example.yum.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {
    public String reviewId;
    public int userId;
    public int foodId;
    public String restaurant;
    public String imgPath;
    public String reviewTitle;
    public String reviewBody;
    public String food;
    public int upvoteCount;
    public int downvoteCount;
    public double rating;


    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
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

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public int getDownvoteCount() {
        return downvoteCount;
    }

    public void setDownvoteCount(int downvoteCount) {
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
