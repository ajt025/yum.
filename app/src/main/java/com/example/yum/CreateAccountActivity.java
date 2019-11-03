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

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private Button btnCreate;
    private EditText accEmail;
    private EditText accPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btnCreate = findViewById(R.id.btnCreate);
        accEmail = findViewById(R.id.accEmail);
        accPassword = findViewById(R.id.accPassword);

        mAuth = FirebaseAuth.getInstance();



        // TODO implement username/pw/email form
        final String accountEmail = accEmail.getText().toString();
        final String accountPassword = accPassword.getText().toString();

        // TODO redirect to login screen
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accountEmail.isEmpty() || accountPassword.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Username and Password cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccount(accountEmail, accountPassword);

                finish();
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            final Intent intent = new Intent(CreateAccountActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();

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
