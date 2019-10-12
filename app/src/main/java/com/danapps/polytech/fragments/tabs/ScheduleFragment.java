package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.welcome.WelcomeGroupActivity;
import com.danapps.polytech.schedule.Faculties;
import com.danapps.polytech.schedule.Groups;
import com.danapps.polytech.schedule.Scheduler;
import com.danapps.polytech.schedule.Ruz;

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