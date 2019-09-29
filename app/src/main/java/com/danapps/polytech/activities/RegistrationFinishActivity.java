package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationFinishActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_finish);

        sPref = getPreferences(MODE_PRIVATE);

        Log.e("TEST", sPref.getString("timeEmail", ""));
        Log.e("TEST", sPref.getString("timePass", ""));

        mAuth.createUserWithEmailAndPassword(sPref.getString("timeEmail", ""), sPref.getString("timePass", "")).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Objects.requireNonNull(authResult.getUser()).sendEmailVerification();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "К сожалению, что-то пошло не так и мы не смогли вас зарегестрировать :(\n Попробуйте позже...", Toast.LENGTH_LONG).show();
            }
        });


        findViewById(R.id.reg_finishBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sPref.edit().remove("timeEmail").apply();
                // sPref.edit().remove("timePass").apply();
                startActivity(new Intent(RegistrationFinishActivity.this, AuthActivity.class));
            }
        });

    }
}
