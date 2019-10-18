package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.danapps.polytech.Auth;
import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.danapps.polytech.activities.auth.AuthActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_menu, container, false);

        view.findViewById(R.id.menu_headerBlock).setOnClickListener(v -> {
            ((MainActivity) getActivity()).LoadFragment(5);
        });

        return view;
    }
}
