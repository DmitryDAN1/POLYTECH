package com.danapps.polytech.fragments.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.danapps.polytech.R;
import com.danapps.polytech.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAuthFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_auth, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        view.findViewById(R.id.auth_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(4));

        view.findViewById(R.id.auth_resetPassBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(11));

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
                mAuth.signInWithEmailAndPassword(
                        ((EditText) view.findViewById(R.id.auth_emailET)).getText().toString(),
                        ((EditText) view.findViewById(R.id.auth_passET)).getText().toString()
                ).addOnFailureListener(e -> {
                    Snackbar.make(view, getString(R.string.auth_main_error), Snackbar.LENGTH_SHORT).show();
                }).addOnSuccessListener(authResult -> {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(authResult.getUser().getUid());
                    myRef.child("UserInfo").child("UserLogin").setValue(emailET.getText().toString());
                    myRef.child("UserInfo").child("UserPass").setValue(passET.getText().toString());
                    sPref.edit().putString("UserLogin", emailET.getText().toString())
                                .putString("UserPass", passET.getText().toString()).apply();

                    if (sPref.getInt("UserGroupId", 0) == 0) {
                        myRef.child("UserInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("UserGroupId").getValue() != null)
                                    sPref.edit().putInt("UserGroupId", Integer.valueOf(dataSnapshot.child("UserGroupId").getValue().toString())).apply();
                                if (dataSnapshot.child("UserGroupName").getValue() != null)
                                    sPref.edit().putString("UserGroupName", dataSnapshot.child("UserGroupName").getValue().toString()).apply();
                                if (dataSnapshot.child("UserName").getValue() != null)
                                    sPref.edit().putString("UserName", dataSnapshot.child("UserName").getValue().toString()).apply();
                                if (dataSnapshot.child("UserSurname") != null)
                                    sPref.edit().putString("UserSurname", dataSnapshot.child("UserSurname").getValue().toString()).apply();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        myRef.child("UserInfo").child("UserGroupId").setValue(sPref.getInt("UserGroupId", 0));
                        myRef.child("UserInfo").child("UserGroupName").setValue(sPref.getString("UserGroupName", "0"));
                    }

                    if (sPref.getString("UserName", "").equals("")) {
                        myRef.child("UserInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("UserName").getValue() != null)
                                    sPref.edit().putString("UserName", dataSnapshot.child("UserName").getValue().toString()).apply();
                                if (dataSnapshot.child("UserSurname") != null)
                                    sPref.edit().putString("UserSurname", dataSnapshot.child("UserSurname").getValue().toString()).apply();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    ((MainActivity) getActivity()).LoadFragment(4);
                });
            }

        });

        view.findViewById(R.id.auth_registerBTN).setOnClickListener(v ->
            ((MainActivity) getActivity()).LoadFragment(8));    // MainRegisterFragment


        return view;
    }
}
