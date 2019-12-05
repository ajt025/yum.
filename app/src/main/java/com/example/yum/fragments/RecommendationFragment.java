package com.example.yum.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yum.R;
import com.example.yum.RecAdapter;
import com.example.yum.models.Review;
import com.google.android.material.tabs.TabLayout;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;

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

        shareAdapter.addAll(populateReviews());
        dareAdapter.addAll(populateReviews());
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

    private ArrayList<Review> populateReviews() {
        // Share population
        ArrayList<Review> newReviews = new ArrayList<>();
        /*
         * TODO remove this, just test code. Here is where you would make database calls and retrieve reviews
         */
        for (int i = 0; i < 3; ++i) {
            newReviews.add(new Review());
        }

        // TODO get user's favorites

        // TODO parse user faves and extract tags from food names + extract restaurants reviewed

        // TODO re-query all reviews for food with those tags + user has not favorited/reviewed before

        // TODO populate those reviews into ArrayList to send back

        return newReviews;
    }

}
