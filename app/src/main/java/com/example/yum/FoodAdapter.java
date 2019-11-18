package com.example.yum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.models.Food;

import org.parceler.Parcels;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private List<Food> mFoods;
    Context context;

    // pass in reviews into the constructor for RV
    public FoodAdapter(List<Food> foods) {
        mFoods = foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View foodView = inflater.inflate(R.layout.item_food, parent, false);
        ViewHolder viewHolder = new ViewHolder(foodView);
        return viewHolder;
    }

    // bind values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = mFoods.get(position);

        // TODO this is where you will load images/text/etc into the review RecyclerView
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    // ViewHolder class, handles connection of views to vars and listener setup
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(context, FoodProfileActivity.class);
                    intent.putExtra("food", Parcels.wrap(mFoods.get(getAdapterPosition())));
                    context.startActivity(intent);
                }
            });
        }

    }
}
