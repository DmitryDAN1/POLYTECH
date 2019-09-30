package com.danapps.polytech.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TextInputLayout emailTIL, passTIL;
    EditText emailET, passET;

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);

        emailET = findViewById(R.id.auth_emailET);
        emailTIL = findViewById(R.id.auth_emailTIL);
        passET = findViewById(R.id.auth_passET);
        passTIL = findViewById(R.id.auth_passTIL);

        findViewById(R.id.auth_logBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (emailET.getText().toString().isEmpty())
                    emailTIL.setError(getString(R.string.email_error));
                else if (passET.getText().toString().isEmpty())
                    passTIL.setError(getString(R.string.pass_error));
                else if (passET.getText().toString().length() < 6)
                    passTIL.setError(getString(R.string.pass_error_length));
                else {
                    mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passET.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @SuppressLint("CommitPrefEdits")
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (Objects.requireNonNull(authResult.getUser()).isEmailVerified()) {
                                sPref.edit().putString("UserLogin", emailET.getText().toString()).apply();
                                sPref.edit().putString("UserPass", passET.getText().toString()).apply();
                                startActivity(new Intent(AuthActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                            } else {
                                Snackbar.make(v, getString(R.string.auth_main_error), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(v, Objects.requireNonNull(e.getLocalizedMessage()), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        findViewById(R.id.auth_registerBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, RegistrationEmailActivity.class));
            }
        });

        findViewById(R.id.auth_resetPassBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, ResetPassActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
        builder.setTitle(getString(R.string.exit_from_app_mainText)).setMessage(getString(R.string.exit_from_app_subText))
                .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), R.string.good_choice, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
