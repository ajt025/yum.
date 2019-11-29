package com.example.yum.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.yum.ListActivity;
import com.example.yum.LoginActivity;
import com.example.yum.R;
import com.example.yum.models.Settings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private Button btnSignOut;
    private Button btnWishlist;
    private Button btnFavorites;
    private Context context;
    private Switch switchVegetarian;
    private Switch switchVegan;
    private Switch switchLocation;
    private ImageView profilePic;


    private Uri targetUri;
    private Bitmap bitmap;
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
        profilePic = view.findViewById(R.id.ivProfilePic);





        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                final Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                        .Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 0);
            }
        });

        switchVegetarian = view.findViewById(R.id.switchVegetarian);
        switchVegan = view.findViewById(R.id.switchVegan);
        switchLocation = view.findViewById(R.id.switchLocation);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("User Info");
        final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        myDatabase.child(currUser).child("profileImgPath").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageURL = dataSnapshot.getValue().toString();
                Picasso.get().load(imageURL).into(profilePic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            targetUri = data.getData();

            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(targetUri));
                profilePic.setImageBitmap(bitmap);
                String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                uploadPicture(currentUserID);

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private void uploadPicture(final String idToFind) {

        // creating unique path for review
        String path = "profileImages/";
        String id = myDatabase.child(idToFind).getKey();
        path += id;
        path += ".jgp";

        // pushing the image to storage and setting URL
        final StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child(path);
        riversRef.putFile(targetUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // setting download URL here
                        myDatabase.child(idToFind).child("profileImgPath").
                                setValue(uri.toString());


                    }
                });
            }
        });






    }



    }
