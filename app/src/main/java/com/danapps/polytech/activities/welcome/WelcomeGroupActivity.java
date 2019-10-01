package com.danapps.polytech.activities.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class WelcomeGroupActivity extends AppCompatActivity {

    TextInputLayout groupTIL;
    EditText groupET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_group);

        groupET = findViewById(R.id.welcome_group_groupET);
        groupTIL = findViewById(R.id.welcome_group_groupTIL);

        findViewById(R.id.welcome_group_nextBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupET.getText().toString().isEmpty())
                    groupTIL.setError(getString(R.string.welcome_group_error));
                else
                    getSharedPreferences("UserInfo", MODE_PRIVATE).edit().putString("UserGroup", groupET.getText().toString()).apply();
                    startActivity(new Intent(WelcomeGroupActivity.this, MainActivity.class));
            }
        });

    }
}
