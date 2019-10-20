package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterEmailFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, container, false);

        view.findViewById(R.id.reg_email_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(7));

        view.findViewById(R.id.reg_email_nextBTN).setOnClickListener(v -> {
            EditText emailET = view.findViewById(R.id.reg_email_emailET);
            mAuth.signInWithEmailAndPassword(emailET.getText().toString(), "1").addOnFailureListener(e -> {
                //TODO: Написать текст ошибки при уже зарегестрированном email
                if (e.getMessage().equals(""))
                    ((TextInputLayout) view.findViewById(R.id.reg_email_emailTIL)).setError(getString(R.string.email_error_registered));
                else {
                    SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);
                    tPref.edit().putString("TimedEmail", emailET.getText().toString()).apply();
                    ((MainActivity) getActivity()).LoadFragment(9);
                }
            });

        });
        return view;
    }

}
