package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.yum.fragments.ExploreFragment;
import com.example.yum.models.Food_Review_Database;
import com.example.yum.models.Settings_Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {

    private Switch vegetarian;
    private Switch vegan;
    private Switch location;
    private boolean userExists;

    private DatabaseReference myDatabase;
    private Settings_Database settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        vegetarian = findViewById(R.id.switchVegetarian);
        vegan = findViewById(R.id.switchVegan);
        location = findViewById(R.id.switchLocation);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("User Settings");
        final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        settings = new Settings_Database();

        vegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setVegetarian(true);
                    myDatabase.child(currUser).setValue(settings);

                }

                else if(!isChecked) {
                    settings.setVegetarian(false);
                    myDatabase.child(currUser).setValue(settings);
                }
            }
        });

        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setVegan(true);
                    myDatabase.child(currUser).setValue(settings);

                } else if (!isChecked) {
                    settings.setVegan(false);
                    myDatabase.child(currUser).setValue(settings);

                }
            }
        });

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setLocation(true);
                    myDatabase.child(currUser).setValue(settings);

                } else if (!isChecked){
                    settings.setLocation(false);
                    myDatabase.child(currUser).setValue(settings);

                }
            }
        });




    }
}
