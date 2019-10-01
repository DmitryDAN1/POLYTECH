package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.auth.AuthActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MenuFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_menu, null);

        SharedPreferences sPref = Objects.requireNonNull(getActivity()).getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        TextView profileBlockCurrentGroupTV = view.findViewById(R.id.menu_profileBlock_currentGroupTV);
        TextView settingsBlockCurrentGroupTV = view.findViewById(R.id.menu_settingsBlock_currentGroupTV);
        profileBlockCurrentGroupTV.setText(sPref.getString("UserGroup", ""));
        settingsBlockCurrentGroupTV.setText(sPref.getString("UserGroup", ""));

        TextView  profileBlockName = view.findViewById(R.id.profileBlock_name);
        profileBlockName.setText(sPref.getString("UserName", "Name") + " " + sPref.getString("UserSurname", "Surname"));


        view.findViewById(R.id.profileBlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"XYECOC", Snackbar.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.groupNumber_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.exitBlock_exitBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), AuthActivity.class));
            }
        });

        return view;
    }
}
