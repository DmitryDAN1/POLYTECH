package com.danapps.polytech.fragments.tabs;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;

import java.net.HttpURLConnection;
import java.util.List;

public class AboutFragment extends Fragment {

    private static final String VK_APP_PACKAGE_ID = "com.vkontakte.android";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        view.findViewById(R.id.about_contactContent).setOnClickListener(v -> {
            openLink(getActivity(), "https://vk.com/dmitryidan");
        });

        //TODO: Добавить ссылку
        view.findViewById(R.id.about_rateContent).setOnClickListener(v -> {

        });

        view.findViewById(R.id.about_licenseContent).setOnClickListener(v ->
            ((MainActivity) getActivity()).LoadFragment(15));

        return view;
    }

    private static void openLink(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo.isEmpty())
            return;

        for (ResolveInfo info: resInfo) {
            if (info.activityInfo == null)
                continue;
            if (VK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }

        activity.startActivity(intent);
    }

}
