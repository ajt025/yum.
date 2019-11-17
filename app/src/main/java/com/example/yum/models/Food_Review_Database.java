package com.example.yum.models;

public class Food_Review_Database {
    public String review_id;
    public String review_body;
    public int upvote_count;
    public int downvote_count;
    public double rating;

    public Food_Review_Database() {}

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

    public int getUpvote_count() {
        return upvote_count;
    }

    public void setUpvote_count(int upvote_count) {
        this.upvote_count = upvote_count;
    }

    public int getDownvote_count() {
        return downvote_count;
    }

    public void setDownvote_count(int downvote_count) {
        this.downvote_count = downvote_count;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
