package com.danapps.polytech.fragments.tabs;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;


public class CopyrightIconsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_copyright_icons, container, false);

        view.findViewById(R.id.copyright_icon1).setOnClickListener(v ->
                openLink("https://www.flaticon.com/free-icon/moon_1415431"));

        view.findViewById(R.id.copyright_icon2).setOnClickListener(v ->
                openLink("https://www.flaticon.com/free-icon/at_159036"));

        return view;
    }

    private void openLink(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
