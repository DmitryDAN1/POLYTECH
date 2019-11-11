package com.danapps.polytech.fragments.tabs;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;

public class LicenseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_license, container, false);

        view.findViewById(R.id.copyright_iconsCardView).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(19));

        return view;
    }

}
