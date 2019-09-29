package com.danapps.polytech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.danapps.polytech.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationPassActivity extends AppCompatActivity {

    TextInputLayout passTIL;
    EditText passET;

    SharedPreferences sPref;
    TextView showPassBTN;
    boolean isPassShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pass);

        sPref = getPreferences(MODE_PRIVATE);

        showPassBTN = findViewById(R.id.reg_showPassBTN);
        passET = findViewById(R.id.reg_pass_passET);
        passTIL = findViewById(R.id.reg_pass_passTIL);

        findViewById(R.id.reg_pass_backBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationPassActivity.this, AuthActivity.class));
            }
        });

        findViewById(R.id.reg_passBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checker = true;

                if (passET.getText().toString().isEmpty()) {
                    passTIL.setError("Вы забыли ввести пароль :)");
                    checker = false;
                } else if (passET.getText().toString().length() < 6) {
                    passTIL.setError("Уажаюащие себя пароли содержат не менее 6 символов!");
                    checker = false;
                }

                if (checker) {
                    sPref.edit().putString("timePass", passET.getText().toString()).apply();
                    startActivity(new Intent(RegistrationPassActivity.this, RegistrationFinishActivity.class));
                }
            }
        });


        showPassBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassShow) {
                    passET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassBTN.setText("Спрятать пароль");
                } else {
                    passET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassBTN.setText("Показать пароль");
                }
            }
        });

    }
}


