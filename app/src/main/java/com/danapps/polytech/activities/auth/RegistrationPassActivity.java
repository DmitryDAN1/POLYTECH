package com.danapps.polytech.activities.auth;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pass);
        SharedPreferences sPref = getSharedPreferences("TimedInfo", MODE_PRIVATE);

        TextView showPassBTN = findViewById(R.id.reg_pass_showPassBTN);
        EditText passET = findViewById(R.id.reg_pass_passET);
        TextInputLayout passTIL = findViewById(R.id.reg_pass_passTIL);

        findViewById(R.id.reg_pass_backBTN).setOnClickListener(v -> startActivity(new Intent(RegistrationPassActivity.this, RegistrationEmailActivity.class)));

        findViewById(R.id.reg_pass_nextBTN).setOnClickListener(v -> {
            if (passET.getText().toString().isEmpty())
                passTIL.setError(getString(R.string.pass_error));
            else if (passET.getText().toString().length() < 6)
                passTIL.setError("Уажаюащие себя пароли содержат не менее 6 символов!");
            else {
                sPref.edit().putString("TimedPass", passET.getText().toString()).apply();
                startActivity(new Intent(RegistrationPassActivity.this, RegistrationFinishActivity.class));
            }
        });


        boolean isPassShow = false;
        findViewById(R.id.reg_pass_showPassBTN).setOnClickListener(v -> {
            if (isPassShow) {
                passET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPassBTN.setText("Спрятать пароль");
            } else {
                passET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPassBTN.setText("Показать пароль");
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrationPassActivity.this, RegistrationEmailActivity.class));
    }
}