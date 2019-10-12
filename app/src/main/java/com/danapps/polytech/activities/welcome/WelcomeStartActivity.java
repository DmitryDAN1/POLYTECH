package com.danapps.polytech.activities.welcome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.auth.AuthActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class WelcomeStartActivity extends AppCompatActivity {

    DatabaseReference myRef;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);
        sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);


        EditText nameET = findViewById(R.id.welcome_start_nameET), surnameET = findViewById(R.id.welcome_start_surnameET);
        TextInputLayout nameTIL = findViewById(R.id.welcome_start_nameTIL), surnameTIL = findViewById(R.id.welcome_start_surnameTIL);

        findViewById(R.id.welcome_start_nextBTN).setOnClickListener(v -> {
            if (nameET.getText().toString().isEmpty())
                nameTIL.setError(getString(R.string.welcome_start_name_error));
            else if (surnameET.getText().toString().isEmpty())
                surnameTIL.setError(getString(R.string.welcome_start_surname_error));
            else {
                myRef = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("UserInfo");
                myRef.child("UserName").setValue(nameET.getText().toString())
                .addOnFailureListener(e -> Snackbar.make(v, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid -> myRef.child("UserSurname").setValue(surnameET.getText().toString())
                .addOnFailureListener(e -> Snackbar.make(v, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid1 -> {
                    sPref.edit().putString("UserName", nameET.getText().toString())
                         .putString("UserSurname", surnameET.getText().toString()).apply();
                    startActivity(new Intent(WelcomeStartActivity.this, WelcomeGroupActivity.class));
                }));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WelcomeStartActivity.this, AuthActivity.class));
    }
}
