package com.danapps.polytech.schedule.date;

import androidx.annotation.NonNull;

import java.util.Calendar;
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

    public static ScheduleDate fromCalendar(Calendar calendar) {
        return new ScheduleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
