package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yum.models.Food;
import com.example.yum.models.Restaurant;
import com.example.yum.models.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodProfileActivity extends AppCompatActivity {

    RecyclerView rvReviews;
    //RestaurantAdapter restaurantAdapter;
    ArrayList<Restaurant> restaurants;
    ArrayList<Review> reviews;
    private DatabaseReference databaseRef;
    String imageURL;
    String foodName;
    ReviewAdapter reviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_profile);

        reviews = new ArrayList<>();
        restaurants = new ArrayList<>();


        // View setup + RV initialization

        //rvRestaurants = findViewById(R.id.rvRestaurants);
        rvReviews = findViewById(R.id.rvReviews);

        //restaurantAdapter = new RestaurantAdapter(restaurants);
        reviewAdapter = new ReviewAdapter(reviews);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        //rvRestaurants.setLayoutManager(layoutManager);
        rvReviews.setLayoutManager(layoutManager);
        //rvRestaurants.setAdapter(restaurantAdapter);
        rvReviews.setAdapter(reviewAdapter);

        //rvRestaurants.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        rvReviews.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        getIncomingIntent();

    }

    // HELPER METHODS

    private void populateReviews() {


        // search through firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Reviews");

        // setting restaurant name and populating list of reviews
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    reviews.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        Review currFood = snapshot.getValue(Review.class);


                        // matching food
                        if (currFood.getFood().compareToIgnoreCase(foodName) == 0) {

                            if (currFood != null) {
                                addReview(currFood);
                            }
                            //System.out.println(currFood.getFood());
                            //System.out.println(currFood.getRestaurant());

                            TextView restaurantName = findViewById(R.id.profileRestaurantName);
                            restaurantName.setText(currFood.getRestaurant());

                        }

                        System.out.println(reviews.size());

                    }

                    reviewAdapter = new ReviewAdapter(reviews);
                    rvReviews.setAdapter(reviewAdapter);


                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void getIncomingIntent() {
        if (getIntent().hasExtra("image_url") && getIntent().hasExtra("food_name")) {
            imageURL = getIntent().getStringExtra("image_url");
            foodName = getIntent().getStringExtra("food_name");

            setImage(imageURL, foodName);

            populateReviews(); // call to put reviews into RV


        }
    }

    private void addReview(Review review) {
        reviews.add(review);
    }

    private void setImage(String imageURL, String foodName) {
        TextView name = findViewById(R.id.profileFoodName);
        name.setText(foodName);


        ImageView image = findViewById(R.id.ivPic);

        Picasso.get()
                .load(imageURL)
                .fit()
                .centerCrop()
                .into(image);
    }
}
