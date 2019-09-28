package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danapps.polytech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EditText emailET, passET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);

        findViewById(R.id.LogBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passET.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(AuthActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, "Неудачный вход!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.RegBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
