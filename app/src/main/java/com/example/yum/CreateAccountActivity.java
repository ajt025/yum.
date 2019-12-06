package com.example.yum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* This activity handles creating a new account and sending
* that data to Firebase authentication
* */
public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private Button btnCreate;
    private EditText accEmail;
    private EditText accConfirmEmail;
    private EditText accPassword;
    private EditText accConfirmPassword;
    private FirebaseAuth mAuth;
    private EditText accFirstName;
    private EditText accLastName;


    /*
    * Helper method that checks if a string is a valid
    * email or not (Does it use @ and .com)
    * */
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btnCreate = findViewById(R.id.btnCreate);

        accFirstName = findViewById(R.id.accFirst);
        accLastName = findViewById(R.id.accLast);
        accEmail = findViewById(R.id.accEmail);
        accConfirmEmail = findViewById(R.id.accConfirmEmail);
        accPassword = findViewById(R.id.accPassword);
        accConfirmPassword = findViewById(R.id.accConfirmPassword);

        mAuth = FirebaseAuth.getInstance();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting values from fields
                String accountEmail = accEmail.getText().toString();
                String accountFirstname = accFirstName.getText().toString();
                String accountLastname = accLastName.getText().toString();
                String accountConfirmEmail = accConfirmEmail.getText().toString();
                String accountPassword = accPassword.getText().toString();
                String accountConfirmPassword = accConfirmPassword.getText().toString();

                // verify text input checks
                if (accountEmail.isEmpty() || accountPassword.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Username and Password cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                // verify text input checks
                if (accountFirstname.isEmpty() || accountLastname.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "First or Last name cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                if (accountEmail.compareTo(accountConfirmEmail) != 0) {
                    Toast.makeText(CreateAccountActivity.this, "Emails does not match",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (accountPassword.length() < 6) {
                    Toast.makeText(CreateAccountActivity.this, "Password must be at least 6 characters",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isValid(accountEmail) == false) {
                    Toast.makeText(CreateAccountActivity.this, "Must enter a valid email address",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (accountPassword.compareTo(accountConfirmPassword) != 0) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords does not match",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccount(accountEmail, accountPassword, accountFirstname, accountLastname);

            }
        });
    }

    //helper method that communicates to firebase to create a new account login
    private void createAccount(String email, String password, String firstName, String lastName) {

        Log.d(TAG, "createAccount:" + email);

        Toast.makeText(CreateAccountActivity.this, "Creating Account...",
                Toast.LENGTH_SHORT).show();

        // creates user email and password onto firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreateAccountActivity.this, "Account created successfully",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });




        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + lastName).build();

        user.updateProfile(profileUpdates);*/

        // redirects user back to login
        final Intent intent = new Intent(CreateAccountActivity.this,
                LoginActivity.class);

        startActivity(intent);

        finish();

    }
}
