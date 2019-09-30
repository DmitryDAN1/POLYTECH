package com.danapps.polytech.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    TextView showPassBTN;
    boolean isPassShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pass);

        showPassBTN = findViewById(R.id.reg_pass_showPassBTN);
        passET = findViewById(R.id.reg_pass_passET);
        passTIL = findViewById(R.id.reg_pass_passTIL);

        findViewById(R.id.reg_pass_backBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationPassActivity.super.onBackPressed();
            }
        });

        findViewById(R.id.reg_pass_nextBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checker = true;

                if (passET.getText().toString().isEmpty()) {
                    passTIL.setError(getString(R.string.pass_error));
                    checker = false;
                } else if (passET.getText().toString().length() < 6) {
                    passTIL.setError("Уажаюащие себя пароли содержат не менее 6 символов!");
                    checker = false;
                }

                if (checker) {

                    getSharedPreferences("UserTimeInfo", MODE_PRIVATE).edit().putString("timePass", passET.getText().toString()).apply();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


