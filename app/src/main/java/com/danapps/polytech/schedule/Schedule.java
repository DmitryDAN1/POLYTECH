package com.danapps.polytech.schedule;

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
