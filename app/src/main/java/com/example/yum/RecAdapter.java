package com.example.yum;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.models.Review;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private ArrayList<Review> mRecs;
    Context context;

    // pass in reviews into the constructor for RV
    public RecAdapter(ArrayList<Review> recs) {
        mRecs = recs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View dareView = inflater.inflate(R.layout.item_share_or_dare, parent, false);
        viewHolder = new ViewHolder(dareView);

        return viewHolder;
    }

    // bind values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mRecs.get(position);

        // TODO this is where you will load images/text/etc into the review RecyclerView
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
