package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MenuFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_menu, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if (mAuth.getCurrentUser() != null)
            myRef = database.getReference(mAuth.getCurrentUser().getUid());

        view.findViewById(R.id.menu_body_groupContent).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(6));

        ((TextView) view.findViewById(R.id.menu_body_groupCurrent)).setText(sPref.getString("UserGroupName", "Группа не выбрана"));
        ((TextView) view.findViewById(R.id.menu_headerGroup)).setText(sPref.getString("UserGroupName", "Группа не выбрана"));

        if (mAuth.getCurrentUser() != null)
            myRef.child("UserInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ((TextView) view.findViewById(R.id.menu_headerUserNameTV))
                            .setText(dataSnapshot.child("UserName").getValue().toString() + " " +
                                     dataSnapshot.child("UserSurname").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        return view;
    }
}
