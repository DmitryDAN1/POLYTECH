package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.auth.AuthActivity;
import com.danapps.polytech.activities.welcome.WelcomeStartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);

        new Handler().postDelayed(() -> {

            if (sPref.getString("UserLogin", "").equals("") && sPref.getString("UserPass", "").equals("")) {
                startActivity(new Intent(StartActivity.this, AuthActivity.class));
            } else {
                mAuth.signInWithEmailAndPassword(sPref.getString("UserLogin", ""), sPref.getString("UserPass", ""))
                        .addOnFailureListener(e -> startActivity(new Intent(StartActivity.this, AuthActivity.class)))
                        .addOnSuccessListener(authResult -> {
                           if (!authResult.getUser().isEmailVerified())
                               startActivity(new Intent(StartActivity.this, AuthActivity.class));
                           else {
                               DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(authResult.getUser().getUid()).child("UserInfo");
                               myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       if (dataSnapshot.child("UserName").getValue() == null &&
                                            dataSnapshot.child("UserSurname").getValue() == null &&
                                            dataSnapshot.child("UserGroupName").getValue() == null &&
                                            dataSnapshot.child("UserGroupId").getValue() == null) {
                                            startActivity(new Intent(StartActivity.this, WelcomeStartActivity.class));
                                       } else {
                                           sPref.edit().putString("UserName", Objects.requireNonNull(dataSnapshot.child("UserName").getValue()).toString())
                                                   .putString("UserSurname", Objects.requireNonNull(dataSnapshot.child("UserSurname").getValue()).toString())
                                                   .putString("UserGroupName", Objects.requireNonNull(dataSnapshot.child("UserGroupName").getValue()).toString())
                                                   .putInt("UserGroupId", dataSnapshot.child("UserGroupId").getValue(int.class)).apply();
                                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });
                           }
                        });
            }

        }, 250);
    }
}
