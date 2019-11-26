package com.example.yum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.ComposeActivity;
import com.example.yum.FoodAdapter;
import com.example.yum.R;
import com.example.yum.models.Food;
import com.example.yum.models.Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExploreFragment extends Fragment {

    private Context context;
    private FloatingActionButton btnCompose;
    private TextView tvFilters;
    private ConstraintLayout clOptions;
    private EditText searchBar;

    private RecyclerView rvFoods;
    private ArrayList<Food> foods;
    private FoodAdapter foodAdapter;

    private DatabaseReference databaseRef;


    // The onCreateView method is called when Fragment should create its View object hierarchy.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        context = parent.getContext();
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_explore, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnCompose = view.findViewById(R.id.fabCompose);
//        btnFoodProfile = view.findViewById(R.id.btnFoodProfile);
        tvFilters = view.findViewById(R.id.tvFilters);
        clOptions = view.findViewById(R.id.clOptions);
        rvFoods = view.findViewById(R.id.rvFood);
        searchBar = view.findViewById(R.id.searchBar);

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, ComposeActivity.class);
                context.startActivity(intent);
            }
        });

        // expandable search
        tvFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clOptions.getVisibility() == View.VISIBLE) {
                    clOptions.setVisibility(View.GONE);
                } else {
                    clOptions.setVisibility(View.VISIBLE);
                }

            }
        });

        // View setup + RV initialization
        foods = new ArrayList<>();
        foodAdapter = new FoodAdapter(context, foods);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        rvFoods.setLayoutManager(layoutManager);
        rvFoods.setAdapter(foodAdapter);
        rvFoods.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));

        populateFoods(); // call to put reviews into RV

        // this to be used for onTextListener and call function when enter is pressed
        searchBar.setOnKeyListener((new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    foods.clear();
                    final String searchedWord = searchBar.getText().toString();


                    final ArrayList<String> URLs = new ArrayList<String>();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    databaseRef = database.getReference().child("Reviews");

                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Review currFood = snapshot.getValue(Review.class);
                                if (currFood.getFood().contains(searchedWord)) {

                                    Food myFood = new Food();
                                    myFood.setName(currFood.getFood());
                                    myFood.setImgPath(currFood.getImgPath());

                                    foods.add(myFood);

                                }

                            }


                            foodAdapter = new FoodAdapter(context, foods);
                            rvFoods.setAdapter(foodAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    return true;
                }
                return false;


            }

        }));
    }

    // HELPER METHODS
    private void populateFoods() {
        /*
         * TODO remove this, just test code. Here is where you would make database calls and retrieve reviews
         */
        /*for (int i = 0; i < 10; ++i) {
            foods.add(new Food()); // alternate b/t dare and share
            foodAdapter.notifyItemInserted(foods.size() - 1); // tells rv to check for updates
        }*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Reviews");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int total = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    total++;
                    Review currFood = snapshot.getValue(Review.class);
                    Food myFood = new Food();
                    myFood.setName(currFood.getFood());
                    myFood.setImgPath(currFood.getImgPath());

                    if (total < 10) {
                        foods.add(myFood);
                    }

                }

                foodAdapter = new FoodAdapter(context, foods);
                rvFoods.setAdapter(foodAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}