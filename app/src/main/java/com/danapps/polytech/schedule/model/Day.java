package com.danapps.polytech.schedule.model;

import java.util.List;

public class Day {
    private String date;
    private List<Lesson> lessons;
    private int weekday;

    public String getDate() {
        return date;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public int getWeekday() {
        return weekday;
    }
}
