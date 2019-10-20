package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFinishFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_finish, container, false);
        SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);

        view.findViewById(R.id.reg_finish_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(7));

        mAuth.createUserWithEmailAndPassword(tPref.getString("TimedEmail", "0"), tPref.getString("TimedPass", "0"))
            .addOnFailureListener( e ->
                Snackbar.make(view, getString(R.string.reg_finish_error), Snackbar.LENGTH_LONG).show()
            ).addOnSuccessListener(authResult -> {
                authResult.getUser().sendEmailVerification();
                view.findViewById(R.id.regFinish_progressbar).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.regFinish_mainRL).setVisibility(View.VISIBLE);
                view.findViewById(R.id.reg_finish_nextBTN).setVisibility(View.VISIBLE);
            });

        view.findViewById(R.id.reg_finish_nextBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(7));

        return view;
    }

}
