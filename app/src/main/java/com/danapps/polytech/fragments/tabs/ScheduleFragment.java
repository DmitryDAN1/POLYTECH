package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danapps.polytech.R;
import com.danapps.polytech.notes.NoteAdapter;
import com.danapps.polytech.schedule.ScheduleAdapter;
import com.danapps.polytech.schedule.ScheduleListItem;
import com.danapps.polytech.schedule.Scheduler;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.model.schedule.Auditory;
import com.danapps.polytech.schedule.model.schedule.Day;
import com.danapps.polytech.schedule.model.schedule.Lesson;
import com.danapps.polytech.schedule.model.schedule.Teacher;
import com.danapps.polytech.schedule.time.ScheduleTime;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ScheduleListItem> listItems = new ArrayList<>();


    private List<Day> dayList;
    List<Lesson> lessonList;
    private DateTime dateTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        dateTime = DateTime.now();
        Ruz ruz = new Ruz(getContext());
        recyclerView = view.findViewById(R.id.schedule_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView weekendIV = view.findViewById(R.id.schedule_weekend);
        TextView dayOfWeekTV = view.findViewById(R.id.schedule_weekDay);
        TextView weekLongTV = view.findViewById(R.id.schedule_SE);
        UpdateUI(dayOfWeekTV, weekLongTV, dateTime);
        UpdateRV(recyclerView, sPref, dateTime, weekendIV);

        view.findViewById(R.id.schedule_backBTN).setOnClickListener(v -> {
            dateTime = dateTime.minusDays(1);
            UpdateUI(dayOfWeekTV, weekLongTV, dateTime);
            UpdateRV(recyclerView, sPref, dateTime, weekendIV);
        });

        view.findViewById(R.id.schedule_nextBTN).setOnClickListener(v -> {
            dateTime = dateTime.plusDays(1);
            UpdateUI(dayOfWeekTV, weekLongTV, dateTime);
            UpdateRV(recyclerView, sPref, dateTime, weekendIV);
        });


        return view;
    }

    private String timeStr(ScheduleTime startTime, ScheduleTime endTime) {
        String result = "";

        if (startTime.getHour() < 10)
            result = "0" + String.valueOf(startTime.getHour());
        else
            result = String.valueOf(startTime.getHour());

        result += ":";

        if (startTime.getMinute() < 10)
            result += "0" + startTime.getMinute();
        else
            result = String.valueOf(startTime.getMinute());

        result += " - ";

        if (endTime.getHour() < 10)
            result += "0" + String.valueOf(endTime.getHour());
        else
            result += String.valueOf(endTime.getHour());

        result += ":";


        if (endTime.getMinute() < 10)
            result += "0" + endTime.getMinute();
        else
            result += String.valueOf(endTime.getMinute());

        return result;
    }

    private String teachersStr(List<Teacher> teacherList) {
        String result = "Илон Маск";

        if (teacherList != null) {
            Teacher teacher1 = teacherList.get(0);
            result = teacher1.getFullName();

            for (int j = 1; j < teacherList.size(); j++) {
                Teacher teacher = teacherList.get(j);
                result += "\n" + teacher.getFullName();
            }
        }
        return result;
    }

    private String geoStr(List<Auditory> auditoryList) {
        Auditory auditory = auditoryList.get(0);

        return auditory.getBuilding().getName() + ", ауд. " + auditory.getName();
    }

    private void UpdateRV (RecyclerView recyclerView, SharedPreferences sPref, DateTime dateTimeRV, ImageView weekendIV) {
        listItems = new ArrayList<>();
        if (dateTimeRV.getDayOfWeek() != 7) {
            weekendIV.setVisibility(View.GONE);
            Ruz ruz = new Ruz(getContext());
            ruz.newScheduler().querySchedule(sPref.getInt("UserGroupId", 0),
                    new ScheduleDate(dateTimeRV.getYear(), dateTimeRV.getMonthOfYear(), dateTimeRV.getDayOfMonth()),
                    new Scheduler.Listener() {
                        @Override
                        public void onResponseReady(Schedule schedule) {
                            dayList = schedule.getDays();
                            Day day = dayList.get(dateTimeRV.getDayOfWeek() - 1);
                            lessonList = day.getLessons();

                            for (int i = 0; i < lessonList.size(); i++) {
                                Lesson lesson = lessonList.get(i);
                                ScheduleListItem lessonListItem = new ScheduleListItem(
                                        (i + 1) + ".",
                                        lesson.getTypeObj().getName(),
                                        timeStr(lesson.getTimeStart(), lesson.getTimeEnd()),
                                        lesson.getSubject(),
                                        teachersStr(lesson.getTeachers()),
                                        geoStr(lesson.getAuditories())
                                );
                                listItems.add(lessonListItem);
                                adapter = new ScheduleAdapter(listItems, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onResponseError(SchedulerError error) {

                        }
                    });
        } else {
            adapter = new ScheduleAdapter(listItems, getContext());
            recyclerView.setAdapter(adapter);
            weekendIV.setVisibility(View.VISIBLE);
        }

    }

    private String weekStr(DateTime dateTime) {
        String result = "";
        DateTime weekStartDT = dateTime.minusDays(dateTime.getDayOfWeek() - 1);
        DateTime weekEndDT = dateTime.plusDays(7 - dateTime.getDayOfWeek());

        if (weekStartDT.getDayOfMonth() < 10)
            result += "0";
        result += weekStartDT.getDayOfMonth() + ".";

        if (weekStartDT.getMonthOfYear() < 10)
            result += "0";
        result += weekStartDT.getMonthOfYear() + ".";
        result += weekStartDT.getYear() + " - ";

        if (weekEndDT.getDayOfMonth() < 10)
            result += "0";
        result += weekEndDT.getDayOfMonth() + ".";

        if (weekEndDT.getMonthOfYear() < 10)
            result += "0";
        result += weekEndDT.getMonthOfYear() + ".";
        result += weekEndDT.getYear();

        return result;
    }

    @SuppressLint("SetTextI18n")
    private void UpdateUI(TextView dayOfWeek, TextView weekLong, DateTime dateTime) {
        switch (dateTime.getDayOfWeek()) {
            case (1):
                dayOfWeek.setText(R.string.day_monday);
                break;
            case (2):
                dayOfWeek.setText(R.string.day_tuesday);
                break;
            case (3):
                dayOfWeek.setText(R.string.day_wednesday);
                break;
            case (4):
                dayOfWeek.setText(R.string.day_thirsday);
                break;
            case (5):
                dayOfWeek.setText(R.string.day_friday);
                break;
            case (6):
                dayOfWeek.setText(R.string.day_saturday);
                break;
            case (7):
                dayOfWeek.setText(R.string.day_sunday);
                break;
        }

        weekLong.setText(weekStr(dateTime));
    }
}