package com.danapps.polytech.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.danapps.polytech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPassActivity extends AppCompatActivity {

    TextInputLayout emailTIL;
    EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        emailET = findViewById(R.id.reset_pass_emailET);
        emailTIL = findViewById(R.id.reset_pass_emailTIL);

        findViewById(R.id.reset_pass_backBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassActivity.this, AuthActivity.class));
            }
        });

        findViewById(R.id.reset_pass_nextBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (emailET.getText().toString().isEmpty())
                    emailTIL.setError("Вы забыли ввести почту :)");
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailET.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(ResetPassActivity.this, ResetFinishActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(v, Objects.requireNonNull(e.getLocalizedMessage()), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
