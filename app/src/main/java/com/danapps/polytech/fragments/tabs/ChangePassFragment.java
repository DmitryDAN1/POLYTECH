package com.danapps.polytech.fragments.tabs;


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

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassFragment extends Fragment {

    private Button saveBtn;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        saveBtn = view.findViewById(R.id.changePass_saveBtn);
        progressBar = view.findViewById(R.id.changePass_progressBar);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        view.findViewById(R.id.changePass_backBtn).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(4));

        saveBtn.setOnClickListener(v -> {

            if (((EditText) view.findViewById(R.id.changePass_oldPass)).getText().toString().isEmpty())
                Snackbar.make(view, getString(R.string.pass_error), Snackbar.LENGTH_SHORT).show();
            else if (((EditText) view.findViewById(R.id.changePass_newPass)).getText().toString().isEmpty())
                Snackbar.make(view, getString(R.string.pass_error), Snackbar.LENGTH_SHORT).show();
            else if (((EditText) view.findViewById(R.id.changePass_newPass)).getText().toString().length() < 6)
                Snackbar.make(view, getString(R.string.pass_error_length), Snackbar.LENGTH_SHORT).show();
            else {
                saveBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(
                        sPref.getString("UserLogin", "0"),
                        ((EditText) view.findViewById(R.id.changePass_oldPass)).getText().toString());

                mAuth.getCurrentUser().reauthenticate(credential).addOnSuccessListener(aVoid ->
                    mAuth.getCurrentUser().updatePassword(((EditText) view.findViewById(R.id.changePass_newPass)).getText().toString())
                        .addOnSuccessListener(aVoid1 -> {
                            FirebaseDatabase
                                .getInstance()
                                .getReference(mAuth.getCurrentUser().getUid())
                                .child("UserInfo")
                                .child("UserPass")
                                .setValue(((EditText) view.findViewById(R.id.changePass_newPass))
                                        .getText()
                                        .toString())
                                .addOnSuccessListener(aVoid2 -> {
                                    Snackbar.make(view, getString(R.string.change_pass_succesful), Snackbar.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    saveBtn.setClickable(true);
                                })
                                .addOnFailureListener(e -> {
                                    Snackbar.make(view, getString(R.string.change_pass_error), Snackbar.LENGTH_SHORT).show();
                                    Log.e("ChangePassError", e.getMessage());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    saveBtn.setClickable(true);
                                });

                            sPref.edit().putString("UserPass", ((EditText) view.findViewById(R.id.changePass_newPass)).getText().toString()).apply();
                        })
                        .addOnFailureListener(e -> {
                            Snackbar.make(view, getString(R.string.change_pass_error), Snackbar.LENGTH_SHORT).show();
                            Log.e("ChangePassError", e.getMessage());
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
