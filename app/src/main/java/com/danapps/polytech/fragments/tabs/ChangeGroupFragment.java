package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.danapps.polytech.R;
import com.danapps.polytech.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.venvw.spbstu.ruz.RuzService;
import com.venvw.spbstu.ruz.api.FacultiesApi;
import com.venvw.spbstu.ruz.models.Group;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeGroupFragment extends Fragment {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private AutoCompleteTextView groupACTV;
    private RelativeLayout mainRelativeLayout;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_group, container, false);
        SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences gPref = getActivity().getSharedPreferences("GroupsInfo", Context.MODE_PRIVATE);
        FacultiesApi facultiesApi = RuzService.getInstance().getFacultiesApi();
        List<String> list = new ArrayList<>();
        List<Integer> listId = new ArrayList<>();
        TextInputLayout groupTIL = view.findViewById(R.id.changeGroup_groupTIL);
        groupACTV = view.findViewById(R.id.changeGroup_groupET);
        mainRelativeLayout = view.findViewById(R.id.changeGroup_mainRL);
        nextButton = view.findViewById(R.id.changeGroup_nextBTN);

        facultiesApi.getGroups(tPref.getInt("FacultiesId", 0)).enqueue(new Callback<FacultiesApi.GetGroupsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FacultiesApi.GetGroupsResponse> call, @NonNull Response<FacultiesApi.GetGroupsResponse> response) {
                view.findViewById(R.id.changeGroup_progressBar).setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                mainRelativeLayout.setVisibility(View.VISIBLE);

                List<Group> groups = response.body().getGroups();
                for (int i = 0; i < groups.size(); i++) {
                    list.add(groups.get(i).getName());
                    listId.add(groups.get(i).getId());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FacultiesApi.GetGroupsResponse> call, @NonNull Throwable t) {

            }
        });

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        groupACTV.setAdapter(stringArrayAdapter);

        view.findViewById(R.id.changeGroup_nextBTN).setOnClickListener(v -> {
            if (groupACTV.getText().toString().isEmpty())
                groupTIL.setError("Вы не ввели номер группы");
            else {
                view.findViewById(R.id.changeGroup_nextBTN).setClickable(false);
                view.findViewById(R.id.changeGroup_progressBar).setVisibility(View.VISIBLE);

                int groupId = 0;
                boolean checker = false;

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(groupACTV.getText().toString())) {
                        checker = true;
                        groupId = listId.get(i);
                    }
                }

                if (!checker) {
                    view.findViewById(R.id.changeGroup_progressBar).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.changeGroup_nextBTN).setClickable(true);
                    groupTIL.setError("Вы ввели не корректный номер группы");
                } else {
                    gPref.edit().putInt("GroupsCount", gPref.getInt("GroupsCount", 0) + 1).apply();

                    gPref.edit().putString("GroupName" + gPref.getInt("GroupsCount", 0), groupACTV.getText().toString()).apply();
                    gPref.edit().putInt("GroupId" + gPref.getInt("GroupsCount", 0), groupId).apply();

                    if (gPref.getInt("GroupsCount", 0) <= 1)
                        gPref.edit().putInt("CurrentGroup", 1).apply();

                    ((MainActivity) getActivity()).loadFragment(2);
                }
            }

        });

        view.findViewById(R.id.changeGroup_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(6)
        );

        groupACTV.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.findViewById(R.id.changeGroup_nextBTN).performClick();

            return false;
        });

        return view;
    }
}
