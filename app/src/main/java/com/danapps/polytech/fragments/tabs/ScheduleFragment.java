package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.Scheduler;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.model.Week;

public class ScheduleFragment extends Fragment {
    private Ruz ruz;
    private Scheduler scheduler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_schedule, null);

        ruz = new Ruz(getContext());
        scheduler = ruz.newScheduler();

        scheduler.querySchedule(28971, new ScheduleDate(2019, 10, 1), new Scheduler.Listener() {
            @Override
            public void onResponseReady(Schedule schedule) {
                Week week = schedule.getWeek();
            }

            @Override
            public void onResponseError(SchedulerError error) {
                String message = error.getMessage();
            }
        });

        return v;
    }
}