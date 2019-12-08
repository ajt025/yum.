package com.example.yum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yum.models.Food;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/*
* This adapter is used to display the food items as a
* list in the Explore Page. This allows for
* food name and the food picture to be displayed
* */
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

    // binds values based on the position of each element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Food food1 = mFoods.get(position);

        // this is where we will load images/text/etc into the review RecyclerView
        holder.textViewName.setText(food1.getName());
        holder.textViewRestaurant.setText(food1.getRestaurant());
        Picasso.get()
                .load(food1.getImgPath())
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodProfileActivity.class);
                intent.putExtra("image_url", mFoods.get(position).getImgPath());
                intent.putExtra("food_name", mFoods.get(position).getName());
                intent.putExtra("food_restaurant",mFoods.get(position).getRestaurant());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    // ViewHolder class, handles connection of views to vars and listener setup
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView imageView;
        ConstraintLayout parentLayout;
        TextView textViewRestaurant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.tvName);
            textViewRestaurant = itemView.findViewById(R.id.tvFoodRestaurant);
            imageView = itemView.findViewById(R.id.ivFoodPic);
            parentLayout = itemView.findViewById(R.id.parentLayout);

        }

    }

    public void updateData(ArrayList<Food> viewModels) {
        mFoods.clear();
        mFoods.addAll(viewModels);
        notifyDataSetChanged();
    }
}
