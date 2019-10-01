package com.danapps.polytech.schedule.date;

import androidx.annotation.NonNull;

import java.util.Locale;

public class ScheduleDate {
    private int year;
    private int month;
    private int day;

    public ScheduleDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(year) + '-' + month + '-' + day;
    }
}
