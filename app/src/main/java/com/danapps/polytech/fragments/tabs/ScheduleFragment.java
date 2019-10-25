package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.danapps.polytech.R;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.ScheduleAdapter;
import com.danapps.polytech.schedule.Scheduler;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.utils.ScheduleUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.annotations.NotNull;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    private static final String TAG = "ScheduleFragment";

    private SharedPreferences userInfo;
    private Scheduler scheduler;
    private Request scheduleRequest;

    private RelativeLayout scheduleProgressBarLayout;

    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;

    private ImageButton showCalendarButton;
    private DatePickerDialog datePickerDialog;

    private void hideProgressBar() {
        scheduleProgressBarLayout.post(new Runnable() {
            @Override
            public void run() {
                scheduleProgressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    private void querySchedule(@NonNull ScheduleDate date) {
        if(scheduleRequest != null) {
            return;
        }

        Schedule current = scheduleAdapter.getSchedule();
        if(current != null) {
            int weekday = ScheduleUtils.getWeekday(current, date);
            if(weekday > -1) {
                scheduleRecyclerView.smoothScrollToPosition(weekday);
                return;
            }
        }

        scheduleProgressBarLayout.setVisibility(View.VISIBLE);

        int groupId = userInfo.getInt("UserGroupId", 0);
        scheduleRequest = scheduler.querySchedule(groupId, date, new Scheduler.Listener() {
            @Override
            public void onResponseReady(Schedule schedule) {
                scheduleAdapter.setSchedule(schedule);
                scheduleRecyclerView.scrollToPosition(ScheduleUtils.getWeekday(schedule, date));

                scheduleRequest = null;
                hideProgressBar();
            }

            @Override
            public void onResponseError(SchedulerError error) {
                Log.e(TAG, error.toString());
                switch (error.getType()) {
                    case NetworkFailed:
                        Snackbar.make(getView(), R.string.schedule_network_failed,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case ParsingFailed:
                        Snackbar.make(getView(), R.string.schedule_parsing_failed,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                }

                scheduleRequest = null;
                hideProgressBar();
            }
        });
    }

    private void querySchedule(@NotNull Calendar calendar) {
        querySchedule(ScheduleDate.fromCalendar(calendar));
    }

    private void querySchedule() {
        querySchedule(Calendar.getInstance());
    }

    private void initScheduleProgressBarLayout(RelativeLayout view) {
        scheduleProgressBarLayout = view;
    }

    private void initScheduleRecyclerView(RecyclerView view) {
        scheduleRecyclerView = view;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        scheduleRecyclerView.setLayoutManager(linearLayoutManager);

        scheduleRecyclerView.setAdapter(scheduleAdapter);

        new PagerSnapHelper().attachToRecyclerView(scheduleRecyclerView);
    }

    private void initCalendarButton(ImageButton view) {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ScheduleDate date = new ScheduleDate(year, month + 1, dayOfMonth);
                querySchedule(date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        showCalendarButton = view;
        showCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        scheduleAdapter = new ScheduleAdapter(getContext());
        scheduler = new Ruz(getContext()).newScheduler();

        initScheduleRecyclerView(view.findViewById(R.id.schedule_recycler_view));
        initCalendarButton(view.findViewById(R.id.schedule_show_calendar));
        initScheduleProgressBarLayout(view.findViewById(R.id.schedule_progress_bar_layout));

        querySchedule();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(scheduleRequest != null) {
            scheduleRequest.cancel();
            scheduleRequest = null;
        }
    }
}