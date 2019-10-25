package com.danapps.polytech.schedule.model;

import androidx.cardview.widget.CardView;

import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Group;
import com.danapps.polytech.schedule.model.schedule.Day;
import com.danapps.polytech.schedule.model.schedule.Week;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Schedule {
    private List<Day> days;
    private Group group;
    private Week week;

    public List<Day> getDays() {
        return days;
    }

    public Group getGroup() {
        return group;
    }

    public Week getWeek() {
        return week;
    }
}
