package com.example.yum.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yum.R;
import com.example.yum.RecAdapter;
import com.example.yum.models.Review;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/*
* This fragment will recommend users new foods and display
* them based off the wishlist and favorite list of
* the user.
* */
public class RecommendationFragment extends Fragment implements CardStackListener {

    Context context;

    TabLayout tabRec;
    CardStackView csvShareDare;
    CardStackLayoutManager layoutManager;
    TextView tvHeader;

    RecAdapter shareAdapter;
    RecAdapter dareAdapter;
    ArrayList<Review> shareList;
    ArrayList<Review> dareList;

    FirebaseDatabase databaseRef;
    final String currUser = FirebaseAuth.getInstance().getUid();

    // The onCreateView method is called when Fragment should create its View object hierarchy.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        context = parent.getContext();

        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_recommendations, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // View setup + RV/CSV initialization
        tabRec = view.findViewById(R.id.tabRec);
        csvShareDare = view.findViewById(R.id.csvRec);
        tvHeader = view.findViewById(R.id.tvHeader);

        shareList = new ArrayList<>();
        dareList = new ArrayList<>();
        shareAdapter = new RecAdapter(shareList);
        dareAdapter = new RecAdapter(dareList);

        layoutManager = new CardStackLayoutManager(context, this);
        layoutManager.setStackFrom(StackFrom.Top);

        csvShareDare.setLayoutManager(layoutManager);
        csvShareDare.setAdapter(shareAdapter); // default card list

        // handle tab switching and which list to display in cards
        tabRec.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if (position == 0) { // share
                    csvShareDare.setAdapter(shareAdapter);
                    tvHeader.setText(R.string.share);
                } else { // dare
                    csvShareDare.setAdapter(dareAdapter);
                    tvHeader.setText(R.string.dare);
                }

                csvShareDare.getAdapter().notifyDataSetChanged(); // update card stack
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        databaseRef = FirebaseDatabase.getInstance();

        populateShares();
    }

    // Swipe Listener Callbacks

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (csvShareDare.getAdapter() == shareAdapter) {
            if (layoutManager.getTopPosition() == shareAdapter.getItemCount()) {
                csvShareDare.scrollToPosition(0);
            }
        } else {
            if (layoutManager.getTopPosition() == dareAdapter.getItemCount()) {
                csvShareDare.scrollToPosition(0);
            }
        }    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    // HELPER METHODS

    private void populateShares() {
        // Populate share cardstack
        final HashSet<String> userFoodTags = new HashSet<>();
        final HashSet<String> userVisitedRestaurants = new HashSet<>();

        // TODO get user's favorites
        DatabaseReference favRef = databaseRef.getReference().child("Favorites").child(currUser);
        favRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();

                while (iter.hasNext()) {
                    String foodRestaurantKey = iter.next().getKey();

                    // TODO parse user faves and extract tags from food names + extract restaurants reviewed
                    Collections.addAll(userFoodTags, parseFoodTags(foodRestaurantKey));
                    Collections.addAll(userVisitedRestaurants, parseRestaurantName(foodRestaurantKey));
                }

//                Log.d("RecFrag - FoodTags", foodTags.toString());
//                Log.d("RecFrag - Restaurants", visitedRestaurants.toString());

                // TODO re-query all reviews for food with those tags + user has not favorited/reviewed before
                getSimilarFoods(userFoodTags, userVisitedRestaurants);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // HELPER METHODS //

    /* Keys for favorites are saved in Database as such "food name_restaurant"
     * This method parses the key string for each word before the '_' and returns a list of the
     * tags.
     */
    private String[] parseFoodTags(String favoriteKey) {
        // Get all text before _, representing food name
        int underscorePos = favoriteKey.indexOf('_');
        String foodName = favoriteKey.substring(0, underscorePos);

        // Split text on spaces to generate tags
        return foodName.split(" ");
    }

    private String parseRestaurantName(String favoriteKey) {
        // Get all text after _ (exclusive), representing the restaurant's name
        int underscorePos = favoriteKey.indexOf('_') + 1;
        String restaurantName = favoriteKey.substring(underscorePos);

        return restaurantName;
    }

    private void getSimilarFoods(final HashSet<String> userFoodTags,
                                              final HashSet<String> userVisitedRestaurants) {
        ArrayList<Review> results = new ArrayList<>();

        DatabaseReference reviewRef = databaseRef.getReference().child("Reviews");
        reviewRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                ArrayList<Review> recReviews = new ArrayList<>();

                // TODO populate those reviews into CSV
                while (iter.hasNext()) {
                    DataSnapshot curr = iter.next();

                    String foodName = (String) curr.child("food").getValue();
                    String restaurantName = (String) curr.child("restaurant").getValue();

                    // TODO get food tags from the review
                    HashSet<String> reviewFoodTags = new HashSet<>();
                    Collections.addAll(reviewFoodTags, foodName.split(" "));

                    // check if there are any matching food tags
                    if (!Collections.disjoint(userFoodTags, reviewFoodTags)) {
                        // check if the user has been to this restaurant
                        if (!userVisitedRestaurants.contains(restaurantName)) {

                            // matching food tag + user has not visited restaurant -> recommend
                            Review review = curr.getValue(Review.class);
                            recReviews.add(review);
                        }
                    }
                }

                // update cardstack with reviews
                shareAdapter.clear();
                shareAdapter.addAll(recReviews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
