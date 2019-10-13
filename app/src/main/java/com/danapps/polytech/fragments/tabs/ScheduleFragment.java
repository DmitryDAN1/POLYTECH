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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        SharedPreferences sPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        DateTime dateTime = DateTime.now();
        Ruz ruz = new Ruz(getContext());
        recyclerView = view.findViewById(R.id.schedule_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

////        ruz.newScheduler().querySchedule(sPref.getInt("UserGroupId", 0), new ScheduleDate(2019, 10, 18), new Scheduler.Listener() {
////            @Override
////            public void onResponseReady(Schedule schedule) {
////                List<Day> dayList = schedule.getDays();
////                Day day = dayList.get(5);
////                List<Lesson> lessonList = day.getLessons();
////                Lesson lesson = lessonList.get(3);
////                List<Auditory> auditoryList = lesson.getAuditories();
////                Auditory auditory = auditoryList.get(1);
////                Log.e("123", String.valueOf(auditory.getName()));
////            }
//
//            @Override
//            public void onResponseError(SchedulerError error) {
//
//            }
//        });

        UpdateRV(recyclerView, sPref, dateTime);


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
        String result = "Тот, кого нельзя называть!";

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

    private void UpdateRV (RecyclerView recyclerView, SharedPreferences sPref, DateTime dateTime) {
        listItems = new ArrayList<>();
        Ruz ruz = new Ruz(getContext());

        ruz.newScheduler().querySchedule(sPref.getInt("UserGroupId", 0),
                new ScheduleDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()),
                new Scheduler.Listener() {
                    @Override
                    public void onResponseReady(Schedule schedule) {
                        dayList = schedule.getDays();
                        Day day = dayList.get(dateTime.getDayOfWeek() - 2);
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
                        }

                        adapter = new ScheduleAdapter(listItems, getContext());
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onResponseError(SchedulerError error) {

                    }
                });
    }
}