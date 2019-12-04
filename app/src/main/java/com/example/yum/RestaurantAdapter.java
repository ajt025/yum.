package com.example.yum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.models.Restaurant;
import com.example.yum.models.Review;

import org.parceler.Parcels;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> mRestaurants;
    Context context;

    // pass in reviews into the constructor for RV
    public RestaurantAdapter(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reviewView = inflater.inflate(R.layout.item_restaurant, parent, false);
        ViewHolder viewHolder = new ViewHolder(reviewView);
        return viewHolder;
    }

    // bind values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    // ViewHolder class, handles connection of views to vars and listener setup
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(context, RestaurantActivity.class);
                    intent.putExtra("restaurant", Parcels.wrap(mRestaurants.get(getAdapterPosition())));
                    context.startActivity(intent);
                }
            });
        }

    }
}
