package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.auth.AuthActivity;
import com.danapps.polytech.activities.welcome.WelcomeStartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    boolean authChecker = false;
    boolean logChecker = false;
    boolean welcomeChecker = false;
    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);

        if (sPref.getString("UserLogin", "").isEmpty() || sPref.getString("UserPass", "").isEmpty()) {
            authChecker = true;
        }
        else {
            mAuth.signInWithEmailAndPassword(sPref.getString("UserLogin", ""), sPref.getString("UserPass", "")).addOnSuccessListener(authResult -> {
                if (!Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                    logChecker = false;
                    myRef = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
                }
            }).addOnFailureListener(e -> {
                logChecker = true;
                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            });
        }

        if (sPref.getString("UserName", "").isEmpty() || sPref.getString("UserSurname", "").isEmpty() || sPref.getString("UserGroup", "").isEmpty()) {
            welcomeChecker = true;
        }

        new Handler().postDelayed(() -> {

            if (authChecker) {
                startActivity(new Intent(StartActivity.this, AuthActivity.class));
                finish();
            }
            else {
                if (logChecker) {
                    startActivity(new Intent(StartActivity.this, AuthActivity.class));
                }
                else {
                    if (welcomeChecker) {
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("UserInfo").child("UserName").getValue() == null || dataSnapshot.child("UserInfo").child("UserSurname").getValue() == null || dataSnapshot.child("UserInfo").child("UserGroup").getValue() == null) {
                                    startActivity(new Intent(StartActivity.this, WelcomeStartActivity.class));
                                    finish();
                                } else {
                                    sPref.edit()
                                            .putString("UserName", Objects.requireNonNull(dataSnapshot.child("UserInfo").child("UserName").getValue()).toString())
                                            .putString("UserSurname", Objects.requireNonNull(dataSnapshot.child("UserInfo").child("UserSurname").getValue()).toString())
                                            .putString("UserGroup", Objects.requireNonNull(dataSnapshot.child("UserInfo").child("UserGroup").getValue()).toString()).apply();
                                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        startActivity(new Intent(StartActivity.this, WelcomeStartActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                    }
                }
            }

        }, 3000);
    }
}
