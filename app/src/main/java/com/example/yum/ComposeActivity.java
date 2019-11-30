package com.example.yum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yum.models.Review;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;


public class ComposeActivity extends AppCompatActivity {

    private FloatingActionButton fabSubmit; // completes review + submits
    private EditText etDishName; // name of the dish
    private SeekBar sbRating; // rating of the dish
    private EditText etTitle; // title of review
    private EditText etReviewBody; // review description
    private EditText etRestaurantName; // name of the restaurant
    private ImageView ivDisplay; // image of dish + handles intent to gallery
    private DatabaseReference myDatabase;
    private Drawable defaultImage;

    Review review;
    private Uri targetUri;
    private Bitmap bitmap;

    final String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        etTitle = findViewById(R.id.etTitle);
        etDishName = findViewById(R.id.etDish);
        fabSubmit = findViewById(R.id.fabSubmit);
        etDishName = findViewById(R.id.etDish);
        sbRating = findViewById(R.id.sbRating);
        etRestaurantName = findViewById(R.id.etRestaurant);
        etReviewBody = findViewById(R.id.etReviewBody);
        ivDisplay = findViewById(R.id.ivDisplay);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");


        // pick image of food
        ivDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                .Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 0);
                defaultImage = ivDisplay.getDrawable();
            }

        });


        // Submitting data to firebase
        fabSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Includes null argument checks
                if (etDishName.getText().toString().trim().length() <= 0) {
                    Toast.makeText(ComposeActivity.this, "Please include dish name", Toast.LENGTH_LONG).show();
                } else if (etRestaurantName.getText().toString().trim().length() <= 0) {
                    Toast.makeText(ComposeActivity.this, "Please include restaurant name", Toast.LENGTH_LONG).show();
                } else if (etTitle.getText().toString().trim().length() <= 0) {
                    Toast.makeText(ComposeActivity.this, "Please include review title", Toast.LENGTH_LONG).show();
                } else if (etReviewBody.getText().toString().trim().length() <= 0) {
                    Toast.makeText(ComposeActivity.this, "Please include review body", Toast.LENGTH_LONG).show();
                } else if (defaultImage == null || defaultImage == ivDisplay.getDrawable()) {
                    Toast.makeText(ComposeActivity.this, "Please include image", Toast.LENGTH_LONG).show();
                }
                else {

                    String dish = etDishName.getText().toString();
                    String title = etTitle.getText().toString();
                    int rating = sbRating.getProgress() + 1;
                    String description = etReviewBody.getText().toString();
                    String restaurant = etRestaurantName.getText().toString(); // TODO eventually use google places + ID?
                    String id = myDatabase.push().getKey();

                    //constructing data object here
                    review = new Review();

                    review.setFood(dish);
                    review.setReviewTitle(title);
                    review.setReviewBody(description);
                    review.setRating(sbRating.getProgress() + 1);
                    review.setReviewId(id);
                    review.setUpvoteCount(0);
                    review.setDownvoteCount(0);


                    uploadPicture(id);

                    // send it to firebase
                    myDatabase.child(id).setValue(review);

                    Toast.makeText(ComposeActivity.this, "Review Submitted", Toast.LENGTH_LONG).show();

                    final Intent intent = new Intent(ComposeActivity.this,
                            HomeActivity.class);


                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    // HELPER METHODS

    // helper function to display food image in ImageView
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            targetUri = data.getData();

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                ivDisplay.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    // essentially uploads the picture to firebase storage
    private void uploadPicture(final String idToFind) {

        // creating unique path for review
        String path = "reviewImages/";
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
                        myDatabase.child(idToFind).child("imgPath").
                                setValue(uri.toString());


                    }
                });
            }
        });






    }
}
