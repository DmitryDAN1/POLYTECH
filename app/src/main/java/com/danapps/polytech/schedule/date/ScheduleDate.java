package com.danapps.polytech.schedule.date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
        return new ScheduleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
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

    public Calendar toCalendar() {
        return new GregorianCalendar(year, month - 1, day);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ScheduleDate) {
            ScheduleDate date = (ScheduleDate) obj;
            return year == date.year && month == date.month && day == date.day;
        }

        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(year) + '-' + month + '-' + day;
    }
}
