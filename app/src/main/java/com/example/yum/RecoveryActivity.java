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
import com.google.firebase.auth.FirebaseAuth;

/*
* This activity handles recovery functionality and
* communicates with Firebase to send a email recovery
* to the designated user email
* */
public class RecoveryActivity extends AppCompatActivity {

    private Button btnRecover;
    private EditText recEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        btnRecover = findViewById(R.id.btnRequestRecover);
        recEmail = findViewById(R.id.recEmail);

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recEmail.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // here sends Firebase to recover password
                FirebaseAuth.getInstance().sendPasswordResetEmail(recEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(getApplicationContext(), "Sending Email...",
                                        Toast.LENGTH_SHORT).show();

                                if (task.isSuccessful()) {

                                    Log.d("Email", "Email sent.");
                                    Toast.makeText(getApplicationContext(), "Recovery email sent.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Email is not registered.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                final Intent intent = new Intent(RecoveryActivity.this,
                        LoginActivity.class);

                // edge case where we sign in after sending email
                intent.putExtra("boolean_check", false);

                startActivity(intent);
                finish();

            }
        });
    }

}
