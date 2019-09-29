package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EditText emailET, passET;

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        sPref = getPreferences(MODE_PRIVATE);

        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);


        Toast.makeText(getBaseContext(),sPref.getString("timeEmail", "") + sPref.getString("timeEmail", ""), Toast.LENGTH_LONG).show();


        findViewById(R.id.LogBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passET.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (Objects.requireNonNull(authResult.getUser()).isEmailVerified()) {
                            sPref.edit().putString("userLogin", emailET.getText().toString()).apply();
                            sPref.edit().putString("userPass", passET.getText().toString()).apply();
                            startActivity(new Intent(AuthActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        }
                        else {
                            Snackbar.make(v,"Ваша учётная запись не подтверждена, проверьте свою электронную почту!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.RegBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, RegistrationEmailActivity.class));
            }
        });

    }
}
