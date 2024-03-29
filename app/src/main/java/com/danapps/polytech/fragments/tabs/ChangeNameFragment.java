package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeNameFragment extends Fragment {

    private EditText nameET;
    private EditText surnameET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_name, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        nameET = view.findViewById(R.id.changeName_nameET);
        surnameET = view.findViewById(R.id.changeName_surnameET);
        view.findViewById(R.id.changeName_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(4));

        view.findViewById(R.id.changeName_nextBTN).setOnClickListener(v -> {
            TextView nameET = view.findViewById(R.id.changeName_nameET);
            TextView surnameET = view.findViewById(R.id.changeName_surnameET);
            if (nameET.getText().toString().isEmpty())
                ((TextInputLayout) view.findViewById(R.id.changeName_nameTIL)).setError("Вы не ввели имя!");
            else if (surnameET.getText().toString().isEmpty())
                ((TextInputLayout) view.findViewById(R.id.changeName_surnameTIL)).setError("Вы не ввели фамилию!");
            else {
                sPref.edit().putString("UserName", nameET.getText().toString())
                            .putString("UserSurname", surnameET.getText().toString())
                            .apply();
                DatabaseReference myRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("UserInfo");
                myRef.child("UserName").setValue(nameET.getText().toString());
                myRef.child("UserSurname").setValue(surnameET.getText().toString());
                ((MainActivity) getActivity()).loadFragment(4);
            }
        });

        nameET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                surnameET.setSelection(surnameET.getText().length());

            return false;
        });

        surnameET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.findViewById(R.id.changeName_nextBTN).performClick();

            return false;
        });

        return view;
    }

}
