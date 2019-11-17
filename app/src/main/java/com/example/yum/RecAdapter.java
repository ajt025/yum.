package com.example.yum;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.models.Review;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private List<Pair<Review, Integer>> mRecs;
    Context context;

    private final int SHARE = 0, DARE = 1;

    // pass in reviews into the constructor for RV
    public RecAdapter(List<Pair<Review, Integer>> recs) {
        mRecs = recs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case DARE:
                View dareView = inflater.inflate(R.layout.item_dare, parent, false);
                viewHolder = new ViewHolder(dareView);
                break;

            default:
            case SHARE:
                View shareView = inflater.inflate(R.layout.item_share, parent, false);
                viewHolder = new ViewHolder(shareView);
                break;
        }

        return viewHolder;
    }

    // bind values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<Review, Integer> review = mRecs.get(position);

        // TODO this is where you will load images/text/etc into the review RecyclerView
    }

    @Override
    public int getItemViewType(int position) {
        return mRecs.get(position).second; // Returns either SHARE(0) or DARE(1) card type
    }

    @Override
    public int getItemCount() {
        return mRecs.size();
    }

    // ViewHolder class, handles connection of views to vars and listener setup
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
