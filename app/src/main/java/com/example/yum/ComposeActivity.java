package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yum.fragments.ExploreFragment;
import com.example.yum.models.Food_Review_Database;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        cmpDescription = findViewById(R.id.cmpDescription);
        viewImage = findViewById(R.id.imageView);

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");


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
                String id = myDatabase.push().getKey();

                //constructing data object here
                foodObject = new Food_Review_Database();

                foodObject.setReview_body(description);
                foodObject.setRating(rating);
                foodObject.setReview_id(id);
                foodObject.setUpvote_count(0);
                foodObject.setDownvote_count(0);

                // send it to firebase
                myDatabase.child(title).setValue(foodObject);
                uploadPicture();

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
    private void uploadPicture() {

        // progress bar of uploading picture
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        // creating unique path for review
        String path = "reviewImages/";
        String id = myDatabase.push().getKey();
        path += id;
        path += ".jgp";

        StorageReference riversRef = FirebaseStorage.getInstance().getReference()
                .child(path);
        riversRef.putFile(targetUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //if the upload is successful
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying a success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //if the upload is not successful
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying error message
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

    }
}
