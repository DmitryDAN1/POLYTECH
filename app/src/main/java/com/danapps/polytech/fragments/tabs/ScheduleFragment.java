package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.welcome.WelcomeFacultiesActivity;
import com.danapps.polytech.schedule.Faculties;
import com.danapps.polytech.schedule.Groups;
import com.danapps.polytech.schedule.Scheduler;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.model.Faculty;

import java.util.List;

public class ScheduleFragment extends Fragment {
    private Ruz ruz;
    private Faculties faculties;
    private Groups groups;
    private Scheduler scheduler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ruz = new Ruz(getContext());
        faculties = ruz.newFaculties();
        groups = ruz.newGroups();
        scheduler = ruz.newScheduler();

        view.findViewById(R.id.scheduleBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WelcomeFacultiesActivity.class));
//                ruz.newFaculties().queryFaculties(new Faculties.Listener() {
//                    @Override
//                    public void onResponseReady(List<Faculty> faculties) {
//                        for (int i = 0; i < faculties.size(); i++){
//                            Log.e("Faculties", faculties.get(i).getName());
//                        }
//                    }
//
//                    @Override
//                    public void onResponseError(SchedulerError error) {
//
//                    }
//                });

            }
        });


//        groups.queryGroups(101, new Groups.Listener() {
//            @Override
//            public void onResponseReady(List<Group> groups) {
//                groups.size();
//            }
//
//            @Override
//            public void onResponseError(SchedulerError error) {
//                error.getMessage();
//            }
//        });
//
//        scheduler.querySchedule(28971, new ScheduleDate(2019, 10, 1), new Scheduler.Listener() {
//            @Override
//            public void onResponseReady(Schedule schedule) {
//                Week week = schedule.getWeek();
//            }
//
//            @Override
//            public void onResponseError(SchedulerError error) {
//                String message = error.getMessage();
//            }
//        });

        return view;
    }
}