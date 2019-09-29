package com.danapps.polytech.schedule.time;

import java.util.Date;

public class DeltaScheduleTime {
    private int totalMinutes;

    DeltaScheduleTime(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getHours() {
        return totalMinutes % 1440 / 60;
    }

    public int getMinutes() {
        return totalMinutes % 1440 % 60;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }
}
