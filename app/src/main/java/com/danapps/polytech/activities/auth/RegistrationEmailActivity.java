package com.danapps.polytech.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.danapps.polytech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationEmailActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_email);
        SharedPreferences sPref = getSharedPreferences("TimedInfo", MODE_PRIVATE);

        TextInputLayout emailTIL = findViewById(R.id.reg_email_emailTIL);
        EditText emailET = findViewById(R.id.reg_email_emailET);

        findViewById(R.id.reg_email_backBTN).setOnClickListener(v -> startActivity(new Intent(RegistrationEmailActivity.this, AuthActivity.class)));

        findViewById(R.id.reg_email_nextBTN).setOnClickListener(v -> {
            if (emailET.getText().toString().isEmpty())
                emailTIL.setError(getString(R.string.email_error));
            else {
                mAuth.signInWithEmailAndPassword(emailET.getText().toString(), "123")
                .addOnFailureListener(e -> {
                    if (e.getMessage().equals("The password is invalid or the user does not have a password."))
                        emailTIL.setError(getString(R.string.email_error_registered));
                    else {
                        sPref.edit().putString("TimedEmail", emailET.getText().toString()).apply();
                        startActivity(new Intent(RegistrationEmailActivity.this, RegistrationPassActivity.class));
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrationEmailActivity.this, AuthActivity.class));
    }
}
