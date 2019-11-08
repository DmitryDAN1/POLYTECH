package com.danapps.polytech.fragments.tabs;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;

public class LinksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_links, container, false);

        view.findViewById(R.id.links_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(4));

        view.findViewById(R.id.links_sites_lms).setOnClickListener(v ->
                openLink("https://lms.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dl).setOnClickListener(v ->
                openLink("https://dl.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dlifkst).setOnClickListener(v ->
                openLink("https://dl-ifkst.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dlphnt).setOnClickListener(v ->
                openLink("https://dl-phnt.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dlice).setOnClickListener(v ->
                openLink("https://dl-ice.spbstu.ru/"));

        view.findViewById(R.id.links_sites_immetdist).setOnClickListener(v ->
                openLink("https://immet-dist.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dliamt).setOnClickListener(v ->
                openLink("https://dl-iamt.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dliamm).setOnClickListener(v ->
                openLink("https://dl-iamm.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dleei).setOnClickListener(v ->
                openLink("https://dl.eei.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dlvsbtipt).setOnClickListener(v ->
                openLink("https://dl-vsbtipt.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dlido).setOnClickListener(v ->
                openLink("https://dl-ido.spbstu.ru/"));

        view.findViewById(R.id.links_sites_dliets).setOnClickListener(v ->
                openLink("https://dl-iets.spbstu.ru/"));

        view.findViewById(R.id.links_sites_portasp).setOnClickListener(v ->
                openLink("https://portasp.spbstu.ru/"));

        view.findViewById(R.id.links_sites_fvo).setOnClickListener(v ->
                openLink("https://dl-fvo.spbstu.ru/"));

        return view;
    }

    private void openLink(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
