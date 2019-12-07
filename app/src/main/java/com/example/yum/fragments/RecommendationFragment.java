package com.example.yum.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yum.R;
import com.example.yum.RecAdapter;
import com.example.yum.models.Review;
import com.google.firebase.auth.FirebaseAuth;
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

    CardStackView csvRecs;
    CardStackLayoutManager layoutManager;
    TextView tvHeader;

    RecAdapter recAdapter;
    ArrayList<Review> recList;

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
        csvRecs = view.findViewById(R.id.csvRec);
        tvHeader = view.findViewById(R.id.tvHeader);

        recList = new ArrayList<>();
        recAdapter = new RecAdapter(recList);

        layoutManager = new CardStackLayoutManager(context, this);
        layoutManager.setStackFrom(StackFrom.Top);

        csvRecs.setLayoutManager(layoutManager);
        csvRecs.setAdapter(recAdapter); // default card list

        databaseRef = FirebaseDatabase.getInstance();

        populateRecommendations();
    }

    // Swipe Listener Callbacks

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        // reset recommendations when end of card stack reached
        if (layoutManager.getTopPosition() == recAdapter.getItemCount()) {
            populateRecommendations();
        }
    }

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

    //**    HELPER METHODS    **//

    /* Populates the CardStackView and generates a list of recommendations for the user
     * based on their "favorites" list.
     */
    private void populateRecommendations() {
        final HashSet<String> userFoodTags = new HashSet<>();
        final HashSet<String> userVisitedRestaurants = new HashSet<>();

        // get user's favorites
        DatabaseReference favRef = databaseRef.getReference().child("Favorites").child(currUser);
        favRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();

                // Iterate each favorite entry and pull food name keywords as well as restaurant names
                while (iter.hasNext()) {
                    String foodRestaurantKey = iter.next().getKey();

                    // parse user faves and extract tags from food names + extract restaurants reviewed
                    Collections.addAll(userFoodTags, parseFoodTags(foodRestaurantKey));
                    Collections.addAll(userVisitedRestaurants, parseRestaurantName(foodRestaurantKey));
                }

                // re-query all reviews for food with those tags + user has not favorited/reviewed before
                getSimilarFoods(userFoodTags, userVisitedRestaurants);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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

    /* Keys for favorites are saved in Database as such "food name_restaurant"
     * This method parses the key string for the substring after the '_' and returns that string.
     */
    private String parseRestaurantName(String favoriteKey) {
        // Get all text after _ (exclusive), representing the restaurant's name
        int underscorePos = favoriteKey.indexOf('_') + 1;
        String restaurantName = favoriteKey.substring(underscorePos);

        return restaurantName;
    }

    /* Generates a list of recommendations for the user based on their favorite history.
     * Populates their CardStackView directly. No return.
     */
    private void getSimilarFoods(final HashSet<String> userFoodTags,
                                              final HashSet<String> userVisitedRestaurants) {

        // Grab reference to the reviews database
        DatabaseReference reviewRef = databaseRef.getReference().child("Reviews");
        reviewRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                ArrayList<Review> recReviews = new ArrayList<>();

                // Iterate reviews in search of reccs
                while (iter.hasNext()) {
                    DataSnapshot curr = iter.next();

                    String foodName = (String) curr.child("food").getValue();
                    String restaurantName = (String) curr.child("restaurant").getValue();

                    // get food tags from the review's food name
                    HashSet<String> reviewFoodTags = new HashSet<>();
                    Collections.addAll(reviewFoodTags, foodName.split(" "));

                    // check if there are any matching food tags
                    if (!Collections.disjoint(userFoodTags, reviewFoodTags)) {
                        // check if the user has been to this restaurant before
                        if (!userVisitedRestaurants.contains(restaurantName)) {

                            // matching food tag + user has not visited restaurant -> recommend
                            Review review = curr.getValue(Review.class);
                            recReviews.add(review);
                        }
                    }
                }

                // update cardstack with reviews
                recAdapter.clear();
                recAdapter.addAll(recReviews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
