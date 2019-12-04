package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yum.models.Food;
import com.example.yum.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

/*
* This activity is used to list the favorite
* and wishlist items when viewing them on the profile
* page. This fetches data from Firebase and displays it
* in a list format
* */
public class ListActivity extends AppCompatActivity {

    private ArrayList<String> mList; // holds either fave or wishlist foods, depending on impl
    private ListView lvFood;



    private TextView tvListType;

    private final int FAVORITE = 0, WISHLIST = 1;

    // Charles' implemented fields:
    private RecyclerView rvFoods;
    private ArrayList<Food> foods;
    private HashSet<String> check_for_foods;
    private FoodAdapter foodAdapter;
    private DatabaseReference dbReference;
    private String path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tvListType = findViewById(R.id.tvListType);
        // RV initializer
        rvFoods = findViewById(R.id.rvFood);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFoods.setLayoutManager(layoutManager);
        rvFoods.setAdapter(foodAdapter);
        rvFoods.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        mList = new ArrayList<>();

        final Intent intent = getIntent();

        if (intent.getIntExtra("type", 0) == FAVORITE) {
            tvListType.setText("Favorite Foods");
            path = "Favorites";
        } else {
            tvListType.setText("Wishlist Foods");
            path = "Wishlist";
        }

        // Get food items in wishlist/favorite list
        check_for_foods = new HashSet<>();
        final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbReference = FirebaseDatabase.getInstance().getReference(path).child(currUser);

        // Read data from firebase
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        check_for_foods.add(snapshot.getKey().toLowerCase());
                        System.out.println(snapshot.getKey().toLowerCase());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Compare with Reviews
        foods = new ArrayList<Food>();
        // get reference to child in database
        dbReference = FirebaseDatabase.getInstance().getReference("Reviews");
        final HashSet<String> duplicates = new HashSet<>();
        duplicates.clear();

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Review currFood = snapshot.getValue(Review.class);
                        Food myFood = new Food();
                        myFood.setName(currFood.getFood());
                        myFood.setImgPath(currFood.getImgPath());

                        String foodName = myFood.getName().toLowerCase();
                        foodName += "_" + currFood.getRestaurant().toLowerCase();

                        // check we are not adding multiple same food
                        if (duplicates.contains(foodName)) {
                            continue;
                        }

                        else if (check_for_foods.contains(foodName) == true) { // change to true
                            check_for_foods.add(foodName);
                            duplicates.add(foodName);
                            foods.add(myFood);
                        }
                    }
                    System.out.println(foods);
                    foodAdapter = new FoodAdapter(foods);
                    rvFoods.setAdapter(foodAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
