package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassFinishFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_pass_finish , container, false);
        SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);

        view.findViewById(R.id.reset_finish_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(7));

        mAuth.sendPasswordResetEmail(tPref.getString("TimedEmail", "0"))
                .addOnSuccessListener(aVoid -> {
                    view.findViewById(R.id.resetPass_progressbar).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.resetPass_mainRL).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.reset_finish_nextBTN).setVisibility(View.VISIBLE);
                });

        view.findViewById(R.id.reset_finish_nextBTN).setOnClickListener(v ->
            ((MainActivity) getActivity()).LoadFragment(7));

        return view;
    }

}
