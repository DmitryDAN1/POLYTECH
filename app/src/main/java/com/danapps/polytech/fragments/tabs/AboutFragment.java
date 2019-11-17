package com.danapps.polytech.fragments.tabs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;

public class AboutFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        view.findViewById(R.id.about_contactContent).setOnClickListener(v ->
                ((MainActivity) getActivity()).openLink(getActivity(), "https://vk.com/dmitryidan"));

        view.findViewById(R.id.about_rateContent).setOnClickListener(v ->
                ((MainActivity) getActivity()).openLink(getActivity(), "https://play.google.com/store/apps/details?id=com.danapps.polytech"));

        view.findViewById(R.id.about_licenseContent).setOnClickListener(v ->
            ((MainActivity) getActivity()).loadFragment(15));

        view.findViewById(R.id.about_backBTN).setOnClickListener(v ->
            ((MainActivity) getActivity()).loadFragment(4));

        return view;
    }



}
