package com.danapps.polytech.activities.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.AuthActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationFinishActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_finish);

        mAuth.createUserWithEmailAndPassword(getSharedPreferences("timeEmail", MODE_PRIVATE).getString("timeEmail", "0"), getSharedPreferences("timePass", MODE_PRIVATE).getString("timePass", "0")).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
                getSharedPreferences("timeEmail", MODE_PRIVATE).edit().remove("timeEmail").apply();
                getSharedPreferences("timePass", MODE_PRIVATE).edit().remove("timePass").apply();
                startActivity(new Intent(RegistrationFinishActivity.this, AuthActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrationFinishActivity.this, AuthActivity.class));
    }
}
