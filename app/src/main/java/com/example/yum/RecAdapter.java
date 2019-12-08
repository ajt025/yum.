package com.example.yum;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

/*
* This adapter is used to display food items
* in the recommendation page. This allows for food
* name, restaurant name and rating to be displayed.
* */
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

        View dareView = inflater.inflate(R.layout.item_rec_card, parent, false);
        viewHolder = new ViewHolder(dareView);

        return viewHolder;
    }

    // bind values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mRecs.get(position);

        // Load image of food and various review fields
        Picasso.get()
                .load(review.getImgPath())
                .fit()
                .centerCrop()
                .into(holder.ivFood);

        //set values
        holder.tvFoodName.setText(review.getFood());
        holder.tvRestaurantName.setText(review.getRestaurant());
        holder.rbRating.setRating((float) review.getRating());
        holder.tvTitle.setText(review.getReviewTitle());
        holder.tvDescription.setText(review.getReviewBody());
    }

    @Override
    public int getItemCount() {
        return mRecs.size();
    }

    // ViewHolder class, handles connection of views to vars and listener setup
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFood;
        TextView tvFoodName;
        TextView tvRestaurantName;
        RatingBar rbRating;
        TextView tvTitle;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFood = itemView.findViewById(R.id.ivFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Review review = mRecs.get(getAdapterPosition());

                    // send image url, name, and restaurant name info
                    Intent intent = new Intent(context, FoodProfileActivity.class);
                    intent.putExtra("image_url", review.getImgPath());
                    intent.putExtra("food_name", review.getFood());
                    intent.putExtra("food_restaurant", review.getRestaurant());

                    context.startActivity(intent);
                }
            });
        }

    }

    // RecAdapter - HELPER METHODS //

    // remove all reviews from backing list
    public void clear() {
        mRecs.clear();
        notifyDataSetChanged();
    }

    // add all reviews from a list to the backing list
    public void addAll(ArrayList<Review> list) {
        mRecs.addAll(list);
        notifyDataSetChanged();
    }
}
