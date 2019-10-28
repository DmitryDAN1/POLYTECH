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

import com.danapps.polytech.R;
import com.danapps.polytech.adapters.ScheduleAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.annotations.NotNull;
import com.venvw.spbstu.ruz.RuzService;
import com.venvw.spbstu.ruz.api.SchedulerApi;
import com.venvw.spbstu.ruz.models.Schedule;

import org.joda.time.LocalDate;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class ScheduleFragment extends Fragment {
    private static final String TAG = "ScheduleFragment";

    private SharedPreferences userInfo;
    private SchedulerApi scheduler;
    private Call<SchedulerApi.GetScheduleResponse> scheduleRequest;

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

    private void querySchedule(@NonNull LocalDate date) {
        int weekday = date.getDayOfWeek() - 1;

        Schedule current = scheduleAdapter.getSchedule();
        if(current != null) {
            if(date.equals(current.getWeek().getDateStart()) || date.equals(current.getWeek().getDateEnd()) ||
                    date.isAfter(current.getWeek().getDateStart()) && date.isBefore(current.getWeek().getDateEnd())) {
                scheduleRecyclerView.smoothScrollToPosition(weekday);
                return;
            }
        }

        scheduleProgressBarLayout.setVisibility(View.VISIBLE);

        int groupId = userInfo.getInt("UserGroupId", 0);
        scheduleRequest = scheduler.getSchedule(groupId, date);
        scheduleRequest.enqueue(new retrofit2.Callback<SchedulerApi.GetScheduleResponse>() {
            @Override
            public void onResponse(@NonNull Call<SchedulerApi.GetScheduleResponse> call, @NonNull Response<SchedulerApi.GetScheduleResponse> response) {
                if(response.body().isError()) {
                    Snackbar.make(getView(), getString(R.string.schedule_parsing_failed), Snackbar.LENGTH_SHORT);
                } else {
                    scheduleAdapter.setSchedule(response.body());
                    scheduleRecyclerView.scrollToPosition(weekday);
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<SchedulerApi.GetScheduleResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private void querySchedule(@NotNull Calendar calendar) {
        querySchedule(LocalDate.fromCalendarFields(calendar));
    }

    private void querySchedule() {
        querySchedule(Calendar.getInstance());
    }

    private void initScheduleProgressBarLayout(RelativeLayout view) {
        scheduleProgressBarLayout = view;
    }

    private void initScheduleRecyclerView(RecyclerView view) {
        scheduleRecyclerView = view;
        scheduleRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        scheduleRecyclerView.setLayoutManager(linearLayoutManager);

        scheduleRecyclerView.setAdapter(scheduleAdapter);

        scheduleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Schedule schedule = scheduleAdapter.getSchedule();
                if(schedule != null) {
                    int index = linearLayoutManager.findFirstVisibleItemPosition();
                    if(index > -1) {
                        LocalDate date = scheduleAdapter.getSchedule().getWeek().getDateStart().plusDays(index);
                        datePickerDialog.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
                    }
                }
            }
        });

        new PagerSnapHelper().attachToRecyclerView(scheduleRecyclerView);
    }

    private void initCalendarButton(ImageButton view) {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDate date = new LocalDate(year, month + 1, dayOfMonth);
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
        scheduler = RuzService.getInstance().getSchedulerApi();

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