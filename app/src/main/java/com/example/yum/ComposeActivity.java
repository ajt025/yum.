package com.example.yum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yum.fragments.ExploreFragment;
import com.example.yum.models.Food_Review_Database;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ComposeActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText cmpTitle;
    private EditText cmpRating;
    private EditText cmpDescription;
    private DatabaseReference myDatabase;
    Food_Review_Database foodObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        btnSubmit = findViewById(R.id.btnSubmit);
        cmpTitle = findViewById(R.id.cmpTitle);
        cmpRating = findViewById(R.id.cmpRating);
        cmpDescription = findViewById(R.id.cmpDescription);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");

        // Submitting data to firebase
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = cmpTitle.getText().toString();
                int rating = Integer.parseInt(cmpRating.getText().toString().trim());
                String description = cmpDescription.getText().toString();
                String id = myDatabase.push().getKey();

                //constructing data object here
                foodObject = new Food_Review_Database();

                foodObject.setReview_title(title);
                foodObject.setReview_body(description);
                foodObject.setRating(rating);
                foodObject.setReview_id(id);
                foodObject.setUpvote_count(0);
                foodObject.setDownvote_count(0);

                // send it to firebase
                myDatabase.push().setValue(foodObject);

                Toast.makeText(ComposeActivity.this, "Review Submitted", Toast.LENGTH_LONG).show();

                final Intent intent = new Intent(ComposeActivity.this,
                        HomeActivity.class);

                startActivity(intent);

                finish();
            }
        });


    }
}
