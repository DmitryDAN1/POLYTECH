package com.danapps.polytech.fragments.tabs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class NavigationChooseFragment extends Fragment {

    private NavigationFragment navigationFragment = new NavigationFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_navigation_choose, null);
        String[] places = {
            getString(R.string.mainBuilding),
            getString(R.string.corpus1),
            getString(R.string.corpus2),
            getString(R.string.corpus3),
            getString(R.string.corpus4),
            getString(R.string.corpus5),
            getString(R.string.corpus6),
            getString(R.string.corpus9),
            getString(R.string.corpus10),
            getString(R.string.corpus11),
            getString(R.string.corpus15),
            getString(R.string.corpus16)
        };

        ArrayAdapter<String> placesAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, places);
        AutoCompleteTextView fromACTV = view.findViewById(R.id.nav_fromACTV);
        AutoCompleteTextView toACTV = view.findViewById(R.id.nav_toACTV);
        TextInputLayout fromTIL = view.findViewById(R.id.nav_fromTIL);
        TextInputLayout toTIL = view.findViewById(R.id.nav_toTIL);
        fromACTV.setAdapter(placesAdapter);
        toACTV.setAdapter(placesAdapter);

        SharedPreferences sPref = Objects.requireNonNull(getActivity()).getSharedPreferences("PlacesInfo", Context.MODE_PRIVATE);

        view.findViewById(R.id.nav_loadBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromACTV.getText().toString().isEmpty())
                    fromTIL.setError(getString(R.string.navigation_choose_from_error));
                else if (toACTV.getText().toString().isEmpty())
                    toTIL.setError(getString(R.string.navigation_choose_to_error));
                else {

                    boolean checkerFrom = false;
                    boolean checkerTo = false;
                    int fromInt = 0, toInt = 0;

                    for (int i = 0; i < places.length; i++) {
                        if (fromACTV.getText().toString().equals(places[i])) {
                            checkerFrom = true;
                            fromInt = i;
                            break;
                        }
                    }

                    for (int i = 0; i < places.length; i++) {
                        if (toACTV.getText().toString().equals(places[i])) {
                            checkerTo = true;
                            toInt = i;
                            break;
                        }
                    }

                    if (!checkerFrom)
                        fromTIL.setError(getString(R.string.navigation_choose_adress_error));
                    else if (!checkerTo)
                        toTIL.setError(getString(R.string.navigation_choose_adress_error));
                    else {
                        sPref.edit().putInt("Place1", fromInt).putInt("Place2", toInt).putBoolean("isRoute", true).apply();
                        assert getFragmentManager() != null;
                        getFragmentManager().beginTransaction().replace(R.id.frame_layout, navigationFragment).commit();
                    }
                }
            }
        });

        view.findViewById(R.id.nav_openMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.frame_layout, navigationFragment).commit();
            }
        });

        return view;
    }

}
