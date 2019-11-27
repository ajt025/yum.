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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private DatabaseReference myDatabase;
    private DatabaseReference myUsers;
    private List<Review> mReviews;
    Context context;

    // pass in reviews into the constructor for RV
    public ReviewAdapter(List<Review> reviews) {
        myDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Review review = mReviews.get(position);

        final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myUsers = FirebaseDatabase.getInstance().getReference().child("User Settings");
        final String reviewId = review.getReviewId();

        holder.title.setText(review.getReviewTitle());
        holder.body.setText(review.getReviewBody());
        holder.rating.setNumStars((int) review.getRating());
        holder.upVoteCount.setText(Integer.toString(review.getUpvoteCount().size() - review.getDownvoteCount().size()));

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!review.getUpvoteCount().contains(currUser)) {
                    review.getUpvoteCount().add(currUser);

                    if (review.getDownvoteCount().contains(currUser)) {
                        review.getDownvoteCount().remove(currUser);
                    }

                    holder.upVoteCount.setText(Integer.toString(review.getUpvoteCount().size() - review.getDownvoteCount().size()));
                    myDatabase.child(reviewId).setValue(review);
                }

            }
        });


        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!review.getDownvoteCount().contains(currUser)) {
                    review.getDownvoteCount().add(currUser);

                    if (review.getUpvoteCount().contains(currUser)) {
                        review.getUpvoteCount().remove(currUser);
                    }

                    holder.upVoteCount.setText(Integer.toString(review.getUpvoteCount().size() - review.getDownvoteCount().size()));
                    myDatabase.child(reviewId).setValue(review);
                }
            }
        });

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
        TextView upVoteCount = itemView.findViewById(R.id.tvUpvoteCounter);
        TextView title = itemView.findViewById(R.id.tvTitle);
        TextView body = itemView.findViewById(R.id.tvReview);
        RatingBar rating = itemView.findViewById(R.id.rbRating);
        ImageView image = itemView.findViewById(R.id.ivProfile);
        ImageView upvote = itemView.findViewById(R.id.imageView4);
        ImageView downvote = itemView.findViewById(R.id.imageView5);

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
