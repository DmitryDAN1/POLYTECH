package com.danapps.polytech.schedule.time;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

public class ScheduleTime {
    private int totalMinute;

    public ScheduleTime(int totalMinute) {
        this.totalMinute = totalMinute;
    }

    public ScheduleTime(int hour, int minute) {
        this.totalMinute = (hour % 24) * 60 + minute % 60;
    }

    public static ScheduleTime fromCalendar(Calendar calendar) {
        return new ScheduleTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
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

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(String.format(Locale.getDefault(),"%02d", getHour()))
                + ':' + String.format(Locale.getDefault(), "%02d", getMinute());
    }
}
