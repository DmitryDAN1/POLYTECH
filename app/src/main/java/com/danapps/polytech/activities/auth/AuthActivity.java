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
import com.danapps.polytech.activities.welcome.WelcomeStartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TextInputLayout emailTIL, passTIL;
    EditText emailET, passET;
    SharedPreferences sPref;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
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
                                myRef = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
                                sPref.edit().putString("UserLogin", emailET.getText().toString()).apply();
                                sPref.edit().putString("UserPass", passET.getText().toString()).apply();

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("UserInfo").child("UserName").getValue() == "" || dataSnapshot.child("UserInfo").child("UserSurname").getValue() == "" || dataSnapshot.child("UserInfo").child("UserGroup").getValue() == "") {
                                            startActivity(new Intent(AuthActivity.this, WelcomeStartActivity.class));
                                            finish();
                                        } else {
                                            sPref.edit()
                                                    .putString("UserName", dataSnapshot.child("UserInfo").child("UserName").getValue().toString())
                                                    .putString("UserSurname", dataSnapshot.child("UserInfo").child("UserSurname").getValue().toString())
                                                    .putString("UserGroup", dataSnapshot.child("UserInfo").child("UserGroup").getValue().toString()).apply();
                                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

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
