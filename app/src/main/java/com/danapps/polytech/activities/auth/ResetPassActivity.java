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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPassActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        EditText emailET = findViewById(R.id.reset_pass_emailET);
        TextInputLayout emailTIL = findViewById(R.id.reset_pass_emailTIL);

        findViewById(R.id.reset_pass_backBTN).setOnClickListener(v -> startActivity(new Intent(ResetPassActivity.this, AuthActivity.class)));

        findViewById(R.id.reset_pass_nextBTN).setOnClickListener(v -> {
            if (emailET.getText().toString().isEmpty())
                emailTIL.setError(getString(R.string.email_error));
            else {
                getSharedPreferences("TimedInfo", MODE_PRIVATE).edit().putString("TimedEmail", emailET.getText().toString()).apply();
                startActivity(new Intent(ResetPassActivity.this, ResetFinishActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetPassActivity.this, AuthActivity.class));
    }
}