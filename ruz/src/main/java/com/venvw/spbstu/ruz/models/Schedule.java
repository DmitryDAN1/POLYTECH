package com.venvw.spbstu.ruz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {
    @SerializedName("week")
    @Expose
    private Week week;
    @SerializedName("days")
    @Expose
    private List<Day> days = null;
    @SerializedName("group")
    @Expose
    private Group group;

    public Week getWeek() {
        return week;
    }

    public List<Day> getDays() {
        return days;
    }

    public Group getGroup() {
        return group;
    }
}
