package com.example.yum;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodProfileActivity extends AppCompatActivity {

    RecyclerView rvRestaurants;
    RestaurantAdapter restaurantAdapter;
    ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_profile);

        // View setup + RV initialization
        rvRestaurants = findViewById(R.id.rvRestaurants);
        restaurants = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(restaurants);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvRestaurants.setLayoutManager(layoutManager);
        rvRestaurants.setAdapter(restaurantAdapter);
        rvRestaurants.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        populateRestaurants(); // call to put reviews into RV

        getIncomingIntent();
    }

    // HELPER METHODS

    private void populateRestaurants() {
        /*
         * TODO remove this, just test code. Here is where you would make database calls and retrieve reviews
         */
        for (int i = 0; i < 10; ++i) {
            restaurants.add(new Restaurant());
            restaurantAdapter.notifyItemInserted(restaurants.size() - 1); // tells rv to check for updates
        }
    }


    private void getIncomingIntent() {
        if (getIntent().hasExtra("image_url") && getIntent().hasExtra("food_name")) {
            String imageURL = getIntent().getStringExtra("image_url");
            String foodName = getIntent().getStringExtra("food_name");

            setImage(imageURL, foodName);


        }
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
