package com.example.yum.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.yum.ListActivity;
import com.example.yum.LoginActivity;
import com.example.yum.R;
import com.example.yum.models.Settings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private Button btnSignOut;
    private Button btnWishlist;
    private Button btnFavorites;
    private Context context;
    private Switch switchVegetarian;
    private Switch switchVegan;
    private Switch switchLocation;

    private DatabaseReference myDatabase;
    private Settings settings;

    private final int FAVORITE = 0, WISHLIST = 1;

    // The onCreateView method is called when Fragment should create its View object hierarchy.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        context = parent.getContext();

        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnWishlist = view.findViewById(R.id.btnWishlist);
        btnFavorites = view.findViewById(R.id.btnFavorites);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                final Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        switchVegetarian = view.findViewById(R.id.switchVegetarian);
        switchVegan = view.findViewById(R.id.switchVegan);
        switchLocation = view.findViewById(R.id.switchLocation);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("User Settings");
        final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        settings = new Settings();

        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("type", WISHLIST);
                startActivity(intent);
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("type", FAVORITE);
                startActivity(intent);
            }
        });

        // TODO implement state persistence --> upon profile screen load, check if switches are
        // already set and reflect accordingly

        switchVegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        switchVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
