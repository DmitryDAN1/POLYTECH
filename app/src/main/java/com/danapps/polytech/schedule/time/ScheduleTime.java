package com.danapps.polytech.schedule.time;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.Date;

public class ScheduleTime {
    private int totalMinute;

    ScheduleTime(int totalMinute) {
        this.totalMinute = totalMinute;
    }

    public int getHour() {
        return totalMinute % 1440 / 60;
    }

    public int getMinute() {
        return totalMinute % 1440 % 60;
    }

    public int getTotalMinute() {
        return totalMinute;
    }

    public ScheduleTime add(ScheduleTime x, DeltaScheduleTime y) {
        return new ScheduleTime(x.totalMinute + y.getTotalMinutes());
    }

    public DeltaScheduleTime subtract(ScheduleTime x, ScheduleTime y) {
        return new DeltaScheduleTime(Math.abs(x.totalMinute - y.totalMinute));
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(getHour()) + '-' + getMinute();
    }
}
