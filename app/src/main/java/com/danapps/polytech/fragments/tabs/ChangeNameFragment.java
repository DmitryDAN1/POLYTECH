package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeNameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_name, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


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
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInfo");
                myRef.child("UserName").setValue(nameET.getText().toString());
                myRef.child("UserSurname").setValue(surnameET.getText().toString());
                ((MainActivity) getActivity()).LoadFragment(4);
            }
        });

        return view;
    }

}
