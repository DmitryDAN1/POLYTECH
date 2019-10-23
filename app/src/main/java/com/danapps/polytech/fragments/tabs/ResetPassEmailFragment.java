package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassEmailFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_pass_email, container, false);

        view.findViewById(R.id.reset_pass_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(7));

        view.findViewById(R.id.reset_pass_nextBTN).setOnClickListener(v -> {
            EditText editText = view.findViewById(R.id.reset_pass_emailET);

            if (editText.getText().toString().isEmpty())
                ((TextInputLayout) view.findViewById(R.id.reset_pass_emailTIL)).setError(getString(R.string.email_error));
            else {
                mAuth.fetchSignInMethodsForEmail(editText.getText().toString())
                        .addOnSuccessListener(command -> {
                            SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);
                            tPref.edit().putString("TimedEmail", editText.getText().toString()).apply();
                            ((MainActivity) getActivity()).LoadFragment(12);
                        })
                        .addOnFailureListener(e ->
                            ((TextInputLayout) view.findViewById(R.id.reset_pass_emailTIL))
                                .setError("Мы не нашли эту почту!"));

            }
        });

        return view;
    }

}
