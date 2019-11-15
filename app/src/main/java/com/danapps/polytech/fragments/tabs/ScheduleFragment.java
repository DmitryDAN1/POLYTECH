package com.danapps.polytech.fragments.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.danapps.polytech.adapters.ScheduleAdapter;
import com.danapps.polytech.calendar.ScheduleCalendarView;
import com.danapps.polytech.presenters.SchedulePresenter;
import com.danapps.polytech.views.ScheduleView;
import com.google.android.material.snackbar.Snackbar;
import com.venvw.spbstu.ruz.models.Schedule;

import org.joda.time.LocalDate;

public class ScheduleFragment extends Fragment implements ScheduleView {

    private SchedulePresenter presenter;

    private RelativeLayout scheduleProgressBarLayout;

    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;

    private ScheduleCalendarView scheduleCalendarView;

    @Override
    public void setSchedule(Schedule schedule) {
        scheduleAdapter.setSchedule(schedule);
    }

    @Override
    public void smoothScrollToWeekday(int weekday) {
        scheduleRecyclerView.smoothScrollToPosition(weekday);
    }

    @Override
    public void setDate(LocalDate date) {
        scheduleCalendarView.setDay(date);
        scheduleRecyclerView.scrollToPosition(date.getDayOfWeek() - 1);
    }

    @Override
    public void showSnackbar(int resourceId, int length) {
        Snackbar.make(getView(), resourceId, length).show();
    }

    @Override
    public void showProgressBar() {
        scheduleProgressBarLayout.post(() -> scheduleProgressBarLayout.setVisibility(View.VISIBLE));
    }

    @Override
    public void hideProgressBar() {
        scheduleProgressBarLayout.post(() -> scheduleProgressBarLayout.setVisibility(View.GONE));
    }

    private void initScheduleProgressBarLayout(RelativeLayout view) {
        scheduleProgressBarLayout = view;
    }

    private void initScheduleRecyclerView(RecyclerView view) {
        scheduleRecyclerView = view;
        scheduleRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager.setInitialPrefetchItemCount(7);
        scheduleRecyclerView.setLayoutManager(linearLayoutManager);

        scheduleAdapter = new ScheduleAdapter(getContext());
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        new PagerSnapHelper().attachToRecyclerView(scheduleRecyclerView);
    }

    private void initCalendarButton(ImageButton view) {
        view.setOnClickListener(v -> scheduleCalendarView.showDatePickerDialog());
    }

    private void initScheduleCalendar(ScheduleCalendarView view) {
        scheduleCalendarView = view;
        view.attachToRecyclerView(scheduleRecyclerView);
        view.setOnDateSelected(date -> presenter.presentForSpecificDate(date));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        SharedPreferences gPref = getActivity().getSharedPreferences("GroupsInfo", Context.MODE_PRIVATE);

        presenter = new SchedulePresenter(() ->
                gPref.getInt("GroupId" + gPref.getInt("CurrentGroup", 1), 0));

//        presenter = new SchedulePresenter(() -> getActivity().getSharedPreferences("UserInfo",
//                Context.MODE_PRIVATE).getInt("UserGroupId", 0));

        presenter.attachToView(this);

        initScheduleRecyclerView(view.findViewById(R.id.schedule_recycler_view));
        initScheduleCalendar(view.findViewById(R.id.calendar_view));
        initCalendarButton(view.findViewById(R.id.schedule_show_calendar));
        initScheduleProgressBarLayout(view.findViewById(R.id.schedule_progress_bar_layout));

        presenter.presentDefault();

        view.findViewById(R.id.schedule_show_groups).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(20));
        ((TextView) view.findViewById(R.id.schedule_currentGroup))
                .setText(gPref.getString("GroupName" + gPref.getInt("CurrentGroup", 0), ""));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachFromView();
    }

}