package com.example.yum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.models.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> mReviews;
    Context context;

    // pass in reviews into the constructor for RV
    public ReviewAdapter(List<Review> reviews) {

        mReviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reviewView = inflater.inflate(R.layout.item_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(reviewView);
        return viewHolder;
    }

    // bind values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        // TODO this is where you will load images/text/etc into the review RecyclerView
        holder.title.setText(review.getReviewTitle());
        holder.body.setText(review.getReviewBody());
        holder.rating.setNumStars((int) review.getRating());

        Picasso.get()
                .load(review.getImgPath())
                .fit()
                .centerCrop()
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    // ViewHolder class, handles connection of views to vars and listener setup
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title = itemView.findViewById(R.id.tvTitle);
        TextView body = itemView.findViewById(R.id.tvReview);
        RatingBar rating = itemView.findViewById(R.id.rbRating);
        ImageView image = itemView.findViewById(R.id.ivProfile);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    public void clear() {
        int size = mReviews.size();
        mReviews.clear();
        notifyItemRangeRemoved(0, size);
    }
}
