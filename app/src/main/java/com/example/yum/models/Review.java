package com.example.yum.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {
    public int reviewId;
    public int userId;
    public int foodId;
    public int restaurantId;
    public String imgPath;
    public String reviewBody;
    public int upvoteCount;
    public int downvoteCount;
    public double rating;

    public Review() {

    }


}
