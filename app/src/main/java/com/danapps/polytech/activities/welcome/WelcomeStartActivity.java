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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeStartActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInfo");

    TextInputLayout nameTIL, surnameTIL;
    EditText nameET, surnameET;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);

        sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);

        nameET = findViewById(R.id.welcome_start_nameET);
        surnameET = findViewById(R.id.welcome_start_surnameET);
        nameTIL = findViewById(R.id.welcome_start_nameTIL);
        surnameTIL = findViewById(R.id.welcome_start_surnameTIL);

        findViewById(R.id.welcome_start_nextBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameET.getText().toString().isEmpty())
                    nameTIL.setError(getString(R.string.welcome_start_name_error));
                else if (surnameET.getText().toString().isEmpty())
                    surnameTIL.setError(getString(R.string.welcome_start_surname_error));
                else {
                    sPref.edit().putString("UserName", nameET.getText().toString()).apply();
                    myRef.setValue(sPref.getString("Name", ""));
                    sPref.edit().putString("UserSurname", surnameET.getText().toString()).apply();
                    myRef.setValue(sPref.getString("Surname", ""));
                    startActivity(new Intent(WelcomeStartActivity.this, WelcomeGroupActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeStartActivity.this);
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
