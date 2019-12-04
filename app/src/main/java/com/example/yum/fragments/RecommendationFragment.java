package com.example.yum.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.R;
import com.example.yum.RecAdapter;
import com.example.yum.models.Review;
import com.google.android.material.tabs.TabLayout;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;

/*
* This fragment will recommend users new foods and display
* them based off the wishlist and favorite list of
* the user.
* */
public class RecommendationFragment extends Fragment {

    Context context;

    TabLayout tabRec;
    CardStackView csvShareDare;
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

        csvShareDare.setLayoutManager(new CardStackLayoutManager(context, this));
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

        populateReviews(); // call to put reviews into RV
    }

    // Swipe Listener Callbacks

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

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

    // HELPER METHODS

    private void populateReviews() {
        /*
         * TODO remove this, just test code. Here is where you would make database calls and retrieve reviews
         */
        for (int i = 0; i < 3; ++i) {
            shareList.add(new Review()); // alternate b/t dare and share
            shareAdapter.notifyItemInserted(shareList.size() - 1); // tells rv to check for updates

            dareList.add(new Review());
            dareAdapter.notifyItemInserted(dareList.size() - 1);
        }
    }
}
