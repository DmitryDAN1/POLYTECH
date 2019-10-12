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
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);

        EditText emailET = findViewById(R.id.auth_emailET), passET =  findViewById(R.id.auth_passET);
        TextInputLayout emailTIL = findViewById(R.id.auth_emailTIL), passTIL =  findViewById(R.id.auth_passTIL);

        findViewById(R.id.auth_registerBTN).setOnClickListener(v -> startActivity(new Intent(AuthActivity.this, RegistrationEmailActivity.class)));
        findViewById(R.id.auth_resetPassBTN).setOnClickListener(v -> startActivity(new Intent(AuthActivity.this, ResetPassActivity.class)));

        findViewById(R.id.auth_logBTN).setOnClickListener(v -> {
            if (emailET.getText().toString().isEmpty())
                emailTIL.setError(getString(R.string.email_error));
            else if (passET.getText().toString().isEmpty())
                passTIL.setError(getString(R.string.pass_error));
            else if (passET.getText().toString().length() < 6)
                passTIL.setError(getString(R.string.pass_error_length));
            else {
                mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passET.getText().toString())
                .addOnFailureListener(e -> Snackbar.make(v, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(authResult -> {
                    if (!authResult.getUser().isEmailVerified())
                        Snackbar.make(v, getString(R.string.auth_main_error), Snackbar.LENGTH_SHORT).show();
                    else {
                        sPref.edit().putString("UserLogin", emailET.getText().toString())
                        .putString("UserPass", passET.getText().toString()).apply();

                        myRef = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("UserInfo");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("UserName").getValue() == null ||
                                    dataSnapshot.child("UserSurname").getValue() == null ||
                                    dataSnapshot.child("UserGroupName").getValue() == null ||
                                    dataSnapshot.child("UserGroupId").getValue() == null)
                                    startActivity(new Intent(AuthActivity.this, WelcomeStartActivity.class));
                                else {
                                    sPref.edit().putString("UserName", dataSnapshot.child("UserName").getValue().toString())
                                    .putString("UserSurname", dataSnapshot.child("UserSurname").getValue().toString())
                                    .putString("UserGroupName", dataSnapshot.child("UserGroupName").getValue().toString())
                                    .putInt("UserGroupId", dataSnapshot.child("UserGroupId").getValue(int.class)).apply();
                                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this)
            .setTitle(getString(R.string.exit_from_app_mainText)).setMessage(getString(R.string.exit_from_app_subText))
            .setNegativeButton(getString(R.string.No), (dialogInterface, i) -> Toast.makeText(getApplicationContext(), R.string.good_choice, Toast.LENGTH_SHORT).show())
            .setPositiveButton(getString(R.string.Yes), (dialogInterface, i) -> finishAffinity());

        builder.create().show();
    }
}



