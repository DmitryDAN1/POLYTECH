package com.danapps.polytech.fragments.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAuthFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button logBTN, regBtn;
    private ImageView backBtn;
    private TextView resetPassBtn;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_auth, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        logBTN = view.findViewById(R.id.auth_logBTN);
        regBtn = view.findViewById(R.id.auth_registerBTN);
        backBtn = view.findViewById(R.id.auth_backBTN);
        resetPassBtn = view.findViewById(R.id.auth_resetPassBTN);
        progressBar = view.findViewById(R.id.auth_progressBar);

        view.findViewById(R.id.auth_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(4));

        view.findViewById(R.id.auth_resetPassBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(11));

        view.findViewById(R.id.auth_logBTN).setOnClickListener(v -> {
            EditText emailET = view.findViewById(R.id.auth_emailET);
            EditText passET = view.findViewById(R.id.auth_passET);

            if (emailET.getText().toString().isEmpty())
                ((TextInputLayout) view.findViewById(R.id.auth_emailTIL)).setError(getString(R.string.email_error));
            else if (passET.getText().toString().isEmpty())
                ((TextInputLayout) view.findViewById(R.id.auth_passTIL)).setError(getString(R.string.pass_error));
            else if (passET.getText().toString().length() < 6)
                ((TextInputLayout) view.findViewById(R.id.auth_passTIL)).setError(getString(R.string.pass_error_length));
            else {
                logBTN.setClickable(false);
                backBtn.setClickable(false);
                regBtn.setClickable(false);
                resetPassBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(
                        ((EditText) view.findViewById(R.id.auth_emailET)).getText().toString(),
                        ((EditText) view.findViewById(R.id.auth_passET)).getText().toString()
                ).addOnFailureListener(e -> {
                    Snackbar.make(view, getString(R.string.auth_main_error), Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    logBTN.setClickable(true);
                    backBtn.setClickable(true);
                    regBtn.setClickable(true);
                    resetPassBtn.setClickable(true);
                }).addOnSuccessListener(authResult -> {
                    DatabaseReference myRef = FirebaseDatabase
                            .getInstance()
                            .getReference("Users")
                            .child(authResult.getUser().getUid())
                            .child("UserInfo");
                    myRef.child("UserLogin").setValue(emailET.getText().toString());
                    myRef.child("UserPass").setValue(passET.getText().toString());
                    sPref.edit()
                            .putString("UserLogin", emailET.getText().toString())
                            .putString("UserPass", passET.getText().toString()).apply();


                    if (sPref.getString("UserName", "").equals("")) {
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("UserName").getValue() != null)
                                    sPref.edit().putString("UserName", dataSnapshot.child("UserName").getValue().toString()).apply();
                                if (dataSnapshot.child("UserSurname").getValue() != null)
                                    sPref.edit().putString("UserSurname", dataSnapshot.child("UserSurname").getValue().toString()).apply();

                                progressBar.setVisibility(View.INVISIBLE);
                                logBTN.setClickable(true);
                                backBtn.setClickable(true);
                                regBtn.setClickable(true);
                                resetPassBtn.setClickable(true);
                                ((MainActivity) getActivity()).loadFragment(4);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        logBTN.setClickable(true);
                        backBtn.setClickable(true);
                        regBtn.setClickable(true);
                        resetPassBtn.setClickable(true);
                        ((MainActivity) getActivity()).loadFragment(4);
                    }
                });
            }

        });

        view.findViewById(R.id.auth_registerBTN).setOnClickListener(v ->
            ((MainActivity) getActivity()).loadFragment(8));


        ((EditText) view.findViewById(R.id.auth_emailET)).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                ((EditText) view.findViewById(R.id.auth_passET))
                        .setSelection(((EditText) view.findViewById(R.id.auth_passET))
                                .getText()
                                .length());

            return false;
        });

        ((EditText) view.findViewById(R.id.auth_passET)).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                logBTN.performClick();

            return false;
        });

        return view;
    }
}
