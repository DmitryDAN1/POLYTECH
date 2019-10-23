package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danapps.polytech.R;
import com.danapps.polytech.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_menu, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if (mAuth.getCurrentUser() != null) {
            myRef = database.getReference(mAuth.getCurrentUser().getUid());
            view.findViewById(R.id.menu_authBlock).setVisibility(View.VISIBLE);
        } else
            view.findViewById(R.id.menu_authBlock).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.menu_headerBlock).setOnClickListener(v -> {
            if (mAuth.getCurrentUser() == null)
                ((MainActivity) getActivity()).LoadFragment(7);
            else {
                new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.exit_from_app_mainText))
                    .setMessage("Вы уверены, что хотите выйти из своего аккаунта?")
                    .setNegativeButton(getString(R.string.No), (dialog, which) -> Log.e("Menu", "Exit NO"))
                    .setPositiveButton(getString(R.string.Yes), (dialog, which) -> {
                        mAuth.signOut();
                        sPref.edit().remove("UserLogin").remove("UserPass").remove("UserName").remove("UserSurname").apply();
                    })
                    .create()
                    .show();
            }
        });

        view.findViewById(R.id.menu_body_groupContent).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(6));

        ((TextView) view.findViewById(R.id.menu_body_groupCurrent)).setText(sPref.getString("UserGroupName", "Группа не выбрана"));
        ((TextView) view.findViewById(R.id.menu_headerGroup)).setText(sPref.getString("UserGroupName", "Группа не выбрана"));

        if (mAuth.getCurrentUser() != null)
            if (!sPref.getString("UserName", "").equals("") && !sPref.getString("UserSurname", "").equals(""))
                ((TextView) view.findViewById(R.id.menu_headerUserNameTV))
                        .setText(sPref.getString("UserName", "") + " " + sPref.getString("UserSurname", ""));
            else
                ((TextView) view.findViewById(R.id.menu_headerUserNameTV)).setText("Выберети себе имя!");
        else
            ((TextView) view.findViewById(R.id.menu_headerUserNameTV)).setText("Необходимо авторизоваться!");


        view.findViewById(R.id.menu_auth_nameContent).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(13));

        view.findViewById(R.id.menu_aboutBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(14));

        return view;
    }
}
