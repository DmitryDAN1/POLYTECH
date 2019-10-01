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

public class StartActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
            mAuth.signInWithEmailAndPassword(sPref.getString("UserLogin", ""), sPref.getString("UserPass", "")).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (!mAuth.getCurrentUser().isEmailVerified()) {
                        logChecker = false;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    logChecker = true;
                    Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        if (sPref.getString("UserName", "").isEmpty() || sPref.getString("UserSurname", "").isEmpty() || sPref.getString("UserGroup", "").isEmpty()) {
            welcomeChecker = true;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
                            startActivity(new Intent(StartActivity.this, WelcomeStartActivity.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                        }
                    }
                }

            }
        }, 3000);
    }
}
