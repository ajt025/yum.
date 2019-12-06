package com.example.yum.fragments;

import android.content.Context;
import android.content.DialogInterface;
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

import android.widget.EditText;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.yum.ListActivity;
import com.example.yum.LoginActivity;
import com.example.yum.R;
import com.example.yum.models.Review;
import com.example.yum.models.Settings;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;

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

/*
* Users can customize their profile image
* as well as their settings here. They will find
* their wishlist and favorite list here as well.
*
* */
public class ProfileFragment extends Fragment {

    private Button btnSignOut;
    private Button btnWishlist;
    private Button btnFavorites;
    private Button btnDeleteAcc;
    private Context context;
    private Switch switchVegetarian;
    private Switch switchVegan;
    private Switch switchLocation;
    private ImageView profilePic;
    private TextView tvName;


    private Uri targetUri;
    private Bitmap bitmap;

    final FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    private DatabaseReference myDatabase;
    private Settings settings;
    private final String dataPath = "User Settings";

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

        // Get buttons
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnWishlist = view.findViewById(R.id.btnWishlist);
        btnFavorites = view.findViewById(R.id.btnFavorites);

        // Define functionality for sign out button
        profilePic = view.findViewById(R.id.ivProfilePic);
        btnDeleteAcc = view.findViewById(R.id.btnDeleteAccount);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
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

        // Get settings switches
        switchVegetarian = view.findViewById(R.id.switchVegetarian);
        switchVegan = view.findViewById(R.id.switchVegan);
        switchLocation = view.findViewById(R.id.switchLocation);


        // Get database reference to send
        myDatabase = FirebaseDatabase.getInstance().getReference().child(dataPath);

        //myDatabase = FirebaseDatabase.getInstance().getReference().child("User Info");

        final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference childImg = myDatabase.child(currUser).child("profileImgPath");

        childImg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageURL = dataSnapshot.getValue(String.class);
                // load picture into Profile Image
                if (imageURL != null) {
                    Picasso.get().load(imageURL).into(profilePic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        settings = new Settings();
        myDatabase = FirebaseDatabase.getInstance().getReference().child("User Settings");
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

        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                promptReauth(); // leads into promptConfirmation
            }
        });

        // Update database to reflect change of vegetarian setting
        final String currUserId = currUser;
        switchVegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setVegetarian(true);
                    myDatabase.child(currUserId).setValue(settings);

                }

                else if(!isChecked) {
                    settings.setVegetarian(false);
                    myDatabase.child(currUserId).setValue(settings);
                }
            }
        });

        // Update database to reflect change of vegan setting
        switchVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setVegan(true);
                    myDatabase.child(currUserId).setValue(settings);

                } else if (!isChecked) {
                    settings.setVegan(false);
                    myDatabase.child(currUserId).setValue(settings);

                }
            }
        });

        // Update database to reflect change of Location setting
        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setLocation(true);
                    myDatabase.child(currUserId).setValue(settings);

                } else if (!isChecked){
                    settings.setLocation(false);
                    myDatabase.child(currUserId).setValue(settings);

                }
            }
        });


        //The profile name Textview
        tvName = view.findViewById(R.id.tvName);

        String displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        //display name if it's not null
        if(displayName!=null && !displayName.equals("")){
            tvName.setText(displayName);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        tvName.setText(email);



    }

    // HELPER METHODS //

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        final Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void promptReauth() {

        // Reauthentication Dialog --> building
        final AlertDialog.Builder builderReauth = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(builderReauth.getContext());

        // keep dialogview for grabbing password from user
        final View dialogView = inflater.inflate(R.layout.dialog_reauth, null);
        builderReauth.setView(dialogView);

        builderReauth.setTitle("Account Re-authentication");

        builderReauth.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText etPassword = dialogView.findViewById(R.id.etPassword);
                String password = etPassword.getText().toString();

                if (!password.isEmpty()) {
                    AuthCredential credential = EmailAuthProvider.getCredential(
                            currUser.getEmail(),
                            password);

                    currUser.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    promptConfirmation();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context,
                                            "Failed to reauthenticate",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                }

            }
        });

        builderReauth.show();
    }

    private void promptConfirmation() {
                // Confirmation Dialog --> building
                AlertDialog.Builder builderConfirmation = new AlertDialog.Builder(context);

                builderConfirmation.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserReviews();
                        deleteUserSettings();

                        currUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            signOut();
                                        }
                                    }
                                });
                    }
                });

                builderConfirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no action, cancels dialog
                    }
                });

                // text setting
                builderConfirmation.setMessage("This action is permanent and all user data will be lost.\n\n" +
                        "Are you sure you want to delete your account?")
                        .setTitle("Account Deletion");

                // completion of dialog
                AlertDialog dialog = builderConfirmation.create();
                dialog.show();
    }

    private void deleteUserReviews() {
        final DatabaseReference reviewDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");
        final String currUserId = currUser.getUid();

        reviewDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Review review = dataSnapshot.getValue(Review.class);

                if (review.getUserId().equals(currUserId)) {
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Delete User Helper Function
    private void deleteUserSettings() {
        final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("User Settings");
        final String currUserId = currUser.getUid();

        userDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(currUserId)) {
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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



