package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.yum.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
* This class is used to display the specific
* food items and reviews from the database.
* It acts like a generic template that will be filled out
* by data from the database.
*
* */
public class FoodProfileActivity extends AppCompatActivity {

    // Fields
    RecyclerView rvReviews;
    ArrayList<Review> reviews;
    protected DatabaseReference databaseRef;
    private TextView tvFood;
    private TextView tvRestaurant;
    String imageURL;
    String foodName;
    String foodRestaurant;
    ReviewAdapter reviewAdapter;
    Button wishlistBtn;
    Button favoriteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_profile);

        // Wishlist button
        wishlistBtn = findViewById(R.id.btnWishlist);
        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the id of the user
                final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // for some reason, we have to define this again or it reverts back to "Review"
                tvFood = findViewById(R.id.profileFoodName);
                tvRestaurant = findViewById(R.id.profileRestaurantName);
                final String wishListID = tvFood.getText().toString() + "_" + tvRestaurant.getText().toString();
                databaseRef = FirebaseDatabase.getInstance().getReference("Wishlist").child(currUser);
                databaseRef.child(wishListID).setValue(1); // the 1 value is a dummy value
                Toast.makeText(FoodProfileActivity.this, "Added to Wishlist",
                        Toast.LENGTH_LONG).show();

            }


        });

        // Favorite button
        favoriteBtn = findViewById(R.id.btnFavorite);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the id of the user
                final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // for some reason, we have to define this again or it reverts back to "Review"
                tvFood = findViewById(R.id.profileFoodName);
                tvRestaurant = findViewById(R.id.profileRestaurantName);
                final String favoriteID = tvFood.getText().toString() + "_" + tvRestaurant.getText().toString();
                databaseRef = FirebaseDatabase.getInstance().getReference("Favorites").child(currUser);
                databaseRef.child(favoriteID).setValue(1); // the 1 value is a dummy value
                Toast.makeText(FoodProfileActivity.this, "Added to Favorite",
                        Toast.LENGTH_LONG).show();

            }


        });

        reviews = new ArrayList<>();

        rvReviews = findViewById(R.id.rvReviews);
        reviewAdapter = new ReviewAdapter(reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setAdapter(reviewAdapter);

        //rvRestaurants.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        rvReviews.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        getIncomingIntent();
    }

    /////////// HELPER METHODS ////////////

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

                        String currFoodName = currFood.getFood();
                        String currFoodRestaurant = currFood.getRestaurant();
                        currFoodName += currFoodRestaurant;

                        // matching food
                        if (currFoodName.compareToIgnoreCase(foodName+foodRestaurant) == 0) {
                            if (currFood != null) {
                                addReview(currFood);
                            }
                            TextView restaurantName = findViewById(R.id.profileRestaurantName);
                            restaurantName.setText(currFood.getRestaurant());
                        }
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
            foodRestaurant = getIntent().getStringExtra("food_restaurant");

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
