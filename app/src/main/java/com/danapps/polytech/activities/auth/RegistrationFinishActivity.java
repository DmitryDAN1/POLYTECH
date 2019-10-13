package com.danapps.polytech.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegistrationFinishActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_registration_finish, null, false);
        setContentView(view);
        SharedPreferences sPref = getSharedPreferences("TimedInfo", MODE_PRIVATE);

        findViewById(R.id.reg_finish_backBTN).setOnClickListener(v -> startActivity(new Intent(RegistrationFinishActivity.this, RegistrationPassActivity.class)));

        mAuth.createUserWithEmailAndPassword(sPref.getString("TimedEmail", "0"), sPref.getString("TimedPass", "0"))
                .addOnSuccessListener(authResult -> Objects.requireNonNull(authResult.getUser()).sendEmailVerification()
                    .addOnFailureListener(e -> Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show())
                    .addOnSuccessListener(aVoid -> {
                        FirebaseDatabase.getInstance().getReference(authResult.getUser().getUid()).child("UserInfo").child("UserLogin").setValue(sPref.getString("TimedEmail", "null"));
                        FirebaseDatabase.getInstance().getReference(authResult.getUser().getUid()).child("UserInfo").child("UserPass").setValue(sPref.getString("TimedPass", "null"));
                        findViewById(R.id.reg_finish_nextBTN).setVisibility(View.VISIBLE);
                    }))
                .addOnFailureListener(e -> Snackbar.make(view, getString(R.string.reg_finish_error), Snackbar.LENGTH_SHORT).show());


        findViewById(R.id.reg_finish_nextBTN).setOnClickListener(v -> {
            sPref.edit().remove("TimedEmail").apply();
            sPref.edit().remove("TimedPass").apply();
            startActivity(new Intent(RegistrationFinishActivity.this, AuthActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrationFinishActivity.this, AuthActivity.class));
    }
}