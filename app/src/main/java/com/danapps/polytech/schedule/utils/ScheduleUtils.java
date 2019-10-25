package com.danapps.polytech.schedule.utils;

import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.model.schedule.Day;

import java.util.Calendar;

public final class ScheduleUtils {
    private ScheduleUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean includes(Schedule schedule, ScheduleDate date) {
        if(date.equals(schedule.getWeek().getDateStart()) || date.equals(schedule.getWeek().getDateEnd())) {
            return true;
        }

        Calendar calendarDate = date.toCalendar();
        return calendarDate.after(schedule.getWeek().getDateStart().toCalendar())
                && calendarDate.before(schedule.getWeek().getDateEnd().toCalendar());
    }

    public static int getWeekday(Schedule schedule, ScheduleDate date) {
        Calendar calendar = schedule.getWeek().getDateStart().toCalendar();
        for(int i = 0; i < 7; ++i) {
            if(date.equals(ScheduleDate.fromCalendar(calendar))) {
                return i;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return -1;
    }
}
