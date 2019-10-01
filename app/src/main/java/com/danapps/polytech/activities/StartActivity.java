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


    boolean authChecker = false;
    boolean logChecker = false;
    boolean welcomeChecker = false;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);

        if (sPref.getString("UserEmail", "").isEmpty() || sPref.getString("UserPass", "").isEmpty()) {
            authChecker = true;
        }
        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(sPref.getString("UserEmail", "0"), sPref.getString("UserPass", "0")).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    logChecker = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
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
                        if (welcomeChecker)
                        {
                            startActivity(new Intent(StartActivity.this, WelcomeStartActivity.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(StartActivity.this, AuthActivity.class));
                            finish();
                        }
                    } else if (logChecker) {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                        finish();
                    }

                    startActivity(new Intent(StartActivity.this, WelcomeStartActivity.class));
                }
            }, 3000);
    }
}
