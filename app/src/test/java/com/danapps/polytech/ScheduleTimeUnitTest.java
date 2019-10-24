package com.danapps.polytech;

import com.danapps.polytech.schedule.time.ScheduleTime;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class ScheduleTimeUnitTest {
    @Test
    public void isScheduleTimeToStringCorrect() {
        assertEquals("00:00", new ScheduleTime(0, 0).toString());
        assertEquals("01:01", new ScheduleTime(1, 1).toString());
        assertEquals("09:59", new ScheduleTime(9, 59).toString());
        assertEquals("10:01", new ScheduleTime(10, 1).toString());
    }

    @Test
    public void isScheduleTimeFromCalendarCorrect() {
        Calendar calendar = Calendar.getInstance();
        ScheduleTime time = ScheduleTime.fromCalendar(calendar);
        assertEquals(time.toString(), new SimpleDateFormat("HH:mm").format(calendar.getTime()));
    }
}
