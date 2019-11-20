package com.example.yum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<String> mList; // holds either fave or wishlist foods, depending on impl
    private ListView lvFood;
    private ArrayAdapter<String> foodAdapter;

    private TextView tvListType;

    private final int FAVORITE = 0, WISHLIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tvListType = findViewById(R.id.tvListType);

        mList = new ArrayList<>();

        final Intent intent = getIntent();

        if (intent.getIntExtra("type", 0) == FAVORITE) {
            tvListType.setText("Favorite Foods");
        } else {
            tvListType.setText("Wishlist Foods");
        }

        lvFood = findViewById(R.id.lvFood);
        foodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mList);

        lvFood.setAdapter(foodAdapter);

        // TODO remove, test code
        for (int i = 0; i < 10; ++i) {
            mList.add("Food name");
            foodAdapter.notifyDataSetChanged();
        }


    }
}
