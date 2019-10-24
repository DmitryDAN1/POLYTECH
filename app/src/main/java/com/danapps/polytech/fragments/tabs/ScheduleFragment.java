package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.ScheduleAdapter;
import com.danapps.polytech.schedule.Scheduler;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Schedule;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    private ScheduleAdapter scheduleAdapter;
    private RecyclerView scheduleRecyclerView;

    private SharedPreferences userInfo;
    private Scheduler scheduler;

    private void querySchedule(Calendar calendar) {
        int groupId = userInfo.getInt("UserGroupId", 0);
        ScheduleDate scheduleDate = new ScheduleDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        scheduler.querySchedule(groupId, scheduleDate, new Scheduler.Listener() {
            @Override
            public void onResponseReady(Schedule schedule) {
                scheduleAdapter.setSchedule(schedule);
                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResponseError(SchedulerError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        scheduleAdapter = new ScheduleAdapter();

        scheduleRecyclerView = view.findViewById(R.id.schedule_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        scheduleRecyclerView.setLayoutManager(linearLayoutManager);
        scheduleRecyclerView.setAdapter(scheduleAdapter);
        new PagerSnapHelper().attachToRecyclerView(scheduleRecyclerView);

        scheduler = new Ruz(getContext()).newScheduler();
        querySchedule(Calendar.getInstance());

        return view;
    }
}