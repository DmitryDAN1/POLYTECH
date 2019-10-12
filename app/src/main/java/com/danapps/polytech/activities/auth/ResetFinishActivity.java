package com.danapps.polytech.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ResetFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_reset_finish, null);
        setContentView(view);

        findViewById(R.id.reset_finish_backBTN).setOnClickListener(v -> startActivity(new Intent(ResetFinishActivity.this, ResetPassActivity.class)));

        FirebaseAuth.getInstance().sendPasswordResetEmail(getSharedPreferences("TimedInfo", MODE_PRIVATE).getString("TimedEmail", "null"))
        .addOnFailureListener(e -> Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show())
        .addOnSuccessListener(aVoid -> findViewById(R.id.reset_finish_nextBTN).setVisibility(View.VISIBLE));

        findViewById(R.id.reset_finish_nextBTN).setOnClickListener(v -> startActivity(new Intent(ResetFinishActivity.this, AuthActivity.class)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetFinishActivity.this, ResetPassActivity.class));
    }
}
