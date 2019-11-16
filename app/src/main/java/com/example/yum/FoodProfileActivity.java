package com.example.yum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.yum.models.Review;

import java.util.ArrayList;

public class FoodProfileActivity extends AppCompatActivity {

    RecyclerView rvReviews;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_profile);

        // View setup + RV initialization
        rvReviews = findViewById(R.id.rvReviews);
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);

        populateReviews(); // call to put reviews into RV
    }

    // HELPER METHODS

    private void populateReviews() {
        /*
         * TODO remove this, just test code. Here is where you would make database calls and retrieve reviews
         */
        for (int i = 0; i < 5; ++i) {
            reviews.add(new Review());
            reviewAdapter.notifyItemInserted(reviews.size() - 1); // tells rv to check for updates
        }
    }
}
