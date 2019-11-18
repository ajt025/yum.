package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yum.fragments.ExploreFragment;
import com.example.yum.models.Food_Review_Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;


public class ComposeActivity extends AppCompatActivity {

    private Button btnInsertImage;
    private Button btnSubmit;
    private EditText cmpTitle;
    private EditText cmpRating;
    private EditText cmpDescription;
    private EditText cmprestauarant;
    private ImageView viewImage;
    private DatabaseReference myDatabase;
    private Uri targetUri;
    private Bitmap bitmap;
    Food_Review_Database foodObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);



        btnInsertImage = findViewById(R.id.btnImageInsert);
        btnSubmit = findViewById(R.id.btnSubmit);
        cmpTitle = findViewById(R.id.cmpTitle);
        cmpRating = findViewById(R.id.cmpRating);
        cmprestauarant = findViewById(R.id.cmpRestaurant);
        cmpDescription = findViewById(R.id.cmpDescription);
        viewImage = findViewById(R.id.imageView);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");
        //create food object here
        foodObject = new Food_Review_Database();


        // pick image of food
        btnInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                .Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 0);
            }

        });


        // Submitting data to firebase
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = cmpTitle.getText().toString();
                int rating = Integer.parseInt(cmpRating.getText().toString().trim());
                String description = cmpDescription.getText().toString();
                String restaurant = cmprestauarant.getText().toString();
                String id = myDatabase.push().getKey();


                foodObject.setFood_restaurant(restaurant);
                foodObject.setReview_body(description);
                foodObject.setRating(rating);
                foodObject.setReview_id(id);
                foodObject.setUpvote_count(0);
                foodObject.setDownvote_count(0);
                foodObject.setFood(title);

                uploadPicture(id);

                // send it to firebase
                myDatabase.child(id).setValue(foodObject);


                Toast.makeText(ComposeActivity.this, "Review Submitted", Toast.LENGTH_LONG).show();

                final Intent intent = new Intent(ComposeActivity.this,
                        HomeActivity.class);

                startActivity(intent);

                finish();
            }
        });
    }

    // helper function to display food image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            targetUri = data.getData();

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                viewImage.setImageBitmap(bitmap);
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


        final StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child(path);
        riversRef.putFile(targetUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // setting download URL here
                        myDatabase.child(idToFind).child("imageURL").
                                setValue(uri.toString());


                    }
                });
            }
        });






    }
}
