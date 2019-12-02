package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yum.models.Food;
import com.example.yum.models.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class ListActivity extends AppCompatActivity {

    private ArrayList<String> mList; // holds either fave or wishlist foods, depending on impl
    private ListView lvFood;
    //private ArrayAdapter<String> foodAdapter;


    private TextView tvListType;

    private final int FAVORITE = 0, WISHLIST = 1;

    // Charles' fields:
    private RecyclerView rvFoods;
    private ArrayList<Food> foods;
    private HashSet<String> check_for_foods;
    private FoodAdapter foodAdapter;
    private DatabaseReference dbReference;
    private String path;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        tvListType = findViewById(R.id.tvListType);

        mList = new ArrayList<>();

        final Intent intent = getIntent();

        if (intent.getIntExtra("type", 0) == FAVORITE) {
            tvListType.setText("Favorite Foods");
            path = "Favorites";
        } else {
            tvListType.setText("Wishlist Foods");
            path = "Wishlist";
        }

        // TODO load items like in ExploreFragment
/*
        check_for_foods = new HashSet<>();
        foods = new ArrayList<Food>();
        dbReference = FirebaseDatabase.getInstance().getReference(path); // get reference to child in database

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
                        foodName += currFood.getRestaurant().toLowerCase();


                        if (check_for_foods.contains(foodName) == false) {
                            check_for_foods.add(foodName);
                            foods.add(myFood);
                        }
                    }
                    // TODO is this context right?
                    foodAdapter = new FoodAdapter(foods);
                    rvFoods.setAdapter(foodAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


*/



        /*
        lvFood = findViewById(R.id.lvFood);
        foodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mList);

        lvFood.setAdapter(foodAdapter);

        // TODO load items from either wishlist or favorite in DB
        for (int i = 0; i < 10; ++i) {
            mList.add("Food name");
            foodAdapter.notifyDataSetChanged();
        }
    */

    }
}
