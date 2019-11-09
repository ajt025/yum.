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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private Button btnCreate;
    private EditText accEmail;
    private EditText accPassword;
    private EditText accConfirmPassword;
    private FirebaseAuth mAuth;

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
        accEmail = findViewById(R.id.accEmail);
        accPassword = findViewById(R.id.accPassword);
        accConfirmPassword = findViewById(R.id.accConfirmPassword);


        mAuth = FirebaseAuth.getInstance();



        // TODO implement username/pw/email form

        // TODO redirect to login screen
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String accountEmail = accEmail.getText().toString();
                String accountPassword = accPassword.getText().toString();
                String accountConfirmPassword = accConfirmPassword.getText().toString();

                if (accountEmail.isEmpty() || accountPassword.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Username and Password cannot be empty",
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
                    Toast.makeText(CreateAccountActivity.this, "Please Confirm Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccount(accountEmail, accountPassword);


                // redirects user back to login
                final Intent intent = new Intent(CreateAccountActivity.this,
                        LoginActivity.class);

                Toast.makeText(CreateAccountActivity.this, "Account Created Successfully",
                        Toast.LENGTH_SHORT).show();

                startActivity(intent);

                finish();
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(CreateAccountActivity.this, "User Registered Successfully.",
                                    Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        // [END create_user_with_email]
    }
}
