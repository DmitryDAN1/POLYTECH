package com.danapps.polytech.views;

import com.venvw.spbstu.ruz.models.Schedule;

import org.joda.time.LocalDate;

public interface ScheduleView {
    void setSchedule(Schedule schedule);
    void smoothScrollToWeekday(int weekDay);
    void setDate(LocalDate date);
    void showSnackbar(int resourceId, int length);
    void showProgressBar();
    void hideProgressBar();
}
