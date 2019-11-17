package com.example.yum.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.R;
import com.example.yum.RecAdapter;
import com.example.yum.models.Review;

import java.util.ArrayList;

public class RecommendationFragment extends Fragment {

    Context context;

    RecyclerView rvRecs;
    RecAdapter recAdapter;
    ArrayList<Pair<Review, Integer>> recs;

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
        // View setup + RV initialization
        rvRecs = view.findViewById(R.id.rvRecs);
        recs = new ArrayList<>();
        recAdapter = new RecAdapter(recs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        rvRecs.setLayoutManager(layoutManager);
        rvRecs.setAdapter(recAdapter);

        populateReviews(); // call to put reviews into RV
    }

    // HELPER METHODS

    private void populateReviews() {
        /*
         * TODO remove this, just test code. Here is where you would make database calls and retrieve reviews
         */
        for (int i = 0; i < 10; ++i) {
            recs.add(new Pair<Review, Integer>(new Review(), i % 2)); // alternate b/t dare and share
            recAdapter.notifyItemInserted(recs.size() - 1); // tells rv to check for updates
        }
    }
}
