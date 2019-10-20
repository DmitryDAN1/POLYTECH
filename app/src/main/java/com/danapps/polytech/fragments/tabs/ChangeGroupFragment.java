package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.danapps.polytech.schedule.Groups;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.model.Group;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ChangeGroupFragment extends Fragment {

    private AutoCompleteTextView groupACTV;
    private ArrayAdapter<String> stringArrayAdapter;
    private RelativeLayout mainRL;
    private Button nextBTN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_group, container, false);
        SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Ruz ruz = new Ruz(getContext());
        Groups groups = ruz.newGroups();
        List<String> list = new ArrayList<>();
        List<Integer> listId = new ArrayList<>();
        TextInputLayout groupTIL = view.findViewById(R.id.changeGroup_groupTIL);
        groupACTV = view.findViewById(R.id.changeGroup_groupET);
        mainRL = view.findViewById(R.id.changeGroup_mainRL);
        nextBTN = view.findViewById(R.id.changeGroup_nextBTN);

        groups.queryGroups(tPref.getInt("FacultiesId", 0), new Groups.Listener() {
            @Override
            public void onResponseReady(List<Group> groups) {
                view.findViewById(R.id.changeGroup_progressBar).setVisibility(View.GONE);
                nextBTN.setVisibility(View.VISIBLE);
                mainRL.setVisibility(View.VISIBLE);
                for (int i = 0; i < groups.size(); i++) {
                    list.add(groups.get(i).getName());
                    listId.add(groups.get(i).getId());
                }
            }

            @Override
            public void onResponseError(SchedulerError error) {

            }
        });

        stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        groupACTV.setAdapter(stringArrayAdapter);

        view.findViewById(R.id.changeGroup_nextBTN).setOnClickListener(v -> {
            if (groupACTV.getText().toString().isEmpty())
                groupTIL.setError("Вы не ввели номер группы");
            else {
                int groupId = 0;
                boolean checker = true;

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(groupACTV.getText().toString()))
                        checker = true;
                        groupId = listId.get(i);
                }

                if (!checker)
                    groupTIL.setError("Вы ввели не корректный номер группы");
                else {
                    sPref.edit().putString("UserGroupName", groupACTV.getText().toString()).apply();
                    sPref.edit().putInt("UserGroupId", groupId).apply();
                    Log.e("ChangeGroupFragment", "sPref<UserGroupName>: " + groupACTV.getText().toString());
                    Log.e("ChangeGroupFragment", "sPref<UserGroupId>: " + groupId);
                    int id = ((BottomNavigationView) ((MainActivity) getActivity()).findViewById(R.id.bottom_navigation_view)).getSelectedItemId();

                    if (id == R.id.schedule_item)
                        ((MainActivity) getActivity()).LoadFragment(2);
                    else if (id == R.id.menu_item)
                        ((MainActivity) getActivity()).LoadFragment(4);
                }
            }

        });

        view.findViewById(R.id.changeGroup_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).LoadFragment(6)
        );

        return view;
    }
}
