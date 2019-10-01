package com.danapps.polytech.schedule.model.schedule;

import com.danapps.polytech.schedule.date.ScheduleDate;

import java.util.List;

public class Day {
    private ScheduleDate date;
    private List<Lesson> lessons;
    private int weekday;

    public ScheduleDate getDate() {
        return date;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public int getWeekday() {
        return weekday;
    }
}
