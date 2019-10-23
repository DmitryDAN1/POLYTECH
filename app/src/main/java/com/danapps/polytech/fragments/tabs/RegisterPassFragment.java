package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.danapps.polytech.R;
import com.danapps.polytech.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPassFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_pass, container, false);

        view.findViewById(R.id.reg_pass_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(8));

        view.findViewById(R.id.reg_pass_showPassBTN).setOnClickListener(v -> {
            TextView textView = view.findViewById(R.id.reg_pass_showPassBTN);

            if (textView.getText().toString().equals(getString(R.string.showPass))) {
                textView.setText(getString(R.string.hidePass));
                ((EditText) view.findViewById(R.id.reg_pass_passET)).setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                textView.setText(getString(R.string.showPass));
                ((EditText) view.findViewById(R.id.reg_pass_passET)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        view.findViewById(R.id.reg_pass_nextBTN).setOnClickListener(v -> {
            EditText passET = view.findViewById(R.id.reg_pass_passET);

            if (passET.getText().toString().isEmpty())
                ((TextInputLayout) view.findViewById(R.id.reg_pass_passTIL)).setError(getString(R.string.pass_error));
            else if (passET.getText().toString().length() < 6)
                ((TextInputLayout) view.findViewById(R.id.reg_pass_passTIL)).setError(getString(R.string.pass_error_length));
            else {
                SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);
                tPref.edit().putString("TimedPass", passET.getText().toString()).apply();
                ((MainActivity) getActivity()).LoadFragment(10);
            }
        });

        return view;
    }

}
