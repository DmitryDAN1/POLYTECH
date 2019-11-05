package com.danapps.polytech;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEmailFragment extends Fragment {

    private Button saveBtn;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        saveBtn = view.findViewById(R.id.changeEmail_saveBtn);
        progressBar = view.findViewById(R.id.changeEmail_progressBar);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        view.findViewById(R.id.changeEmail_backBtn).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(4));


        saveBtn.setOnClickListener(v -> {

            if (((EditText) view.findViewById(R.id.changeEmail_oldPass)).getText().toString().isEmpty())
                Snackbar.make(view, getString(R.string.pass_error), Snackbar.LENGTH_SHORT).show();
            else if (((EditText) view.findViewById(R.id.changeEmail_newEmail)).getText().toString().isEmpty())
                Snackbar.make(view, getString(R.string.email_error), Snackbar.LENGTH_SHORT).show();
            else {
                saveBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(
                        sPref.getString("UserLogin", "0"),
                        ((EditText) view.findViewById(R.id.changeEmail_oldPass)).getText().toString());

                mAuth.getCurrentUser().reauthenticate(credential)
                        .addOnSuccessListener(aVoid ->
                        mAuth.getCurrentUser().updateEmail(((EditText) view.findViewById(R.id.changeEmail_newEmail)).getText().toString())
                                .addOnSuccessListener(aVoid1 -> {
                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference(mAuth.getCurrentUser().getUid())
                                            .child("UserInfo")
                                            .child("UserLogin")
                                            .setValue(((EditText) view.findViewById(R.id.changeEmail_newEmail))
                                                    .getText()
                                                    .toString())
                                            .addOnSuccessListener(aVoid2 -> {
                                                Snackbar.make(view, getString(R.string.change_email_succesfull), Snackbar.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                saveBtn.setClickable(true);
                                            })
                                            .addOnFailureListener(e -> {
                                                Snackbar.make(view, getString(R.string.change_email_error), Snackbar.LENGTH_SHORT).show();
                                                Log.e("ChangeEmailError", e.getMessage());
                                                progressBar.setVisibility(View.INVISIBLE);
                                                saveBtn.setClickable(true);
                                            });

                                    sPref.edit().putString("UserLogin", ((EditText) view.findViewById(R.id.changeEmail_newEmail)).getText().toString()).apply();
                                })
                                .addOnFailureListener(e -> {
                                    Snackbar.make(view, getString(R.string.change_email_error), Snackbar.LENGTH_SHORT).show();
                                    Log.e("ChangeEmailError", e.getMessage());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    saveBtn.setClickable(true);
                                }))
                        .addOnFailureListener(e -> {
                            Snackbar.make(view, getString(R.string.change_pass_error_oldPass), Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            saveBtn.setClickable(true);
                        });
            }

        });

        return view;
    }

}
