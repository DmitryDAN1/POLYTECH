package com.danapps.polytech.schedule.time;

import androidx.annotation.NonNull;

public class ScheduleDeltaTime {
    private static final int WINDOW_MINUTES = 90;

    private int totalMinutes;

    private ScheduleDeltaTime(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getHours() {
        return totalMinutes / 60;
    }

    public int getMinutes() {
        return totalMinutes % 60;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public boolean isWindow() {
        return totalMinutes >= WINDOW_MINUTES;
    }

    public boolean isZero() {
        return totalMinutes == 0;
    }

    public static ScheduleDeltaTime difference(ScheduleTime a, ScheduleTime b) {
        return new ScheduleDeltaTime(Math.abs(a.getTotalMinute() - b.getTotalMinute()));
    }
}
