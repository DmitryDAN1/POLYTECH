package com.danapps.polytech.schedule.model;

import com.danapps.polytech.schedule.model.Group;
import com.danapps.polytech.schedule.model.schedule.Day;
import com.danapps.polytech.schedule.model.schedule.Week;

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
