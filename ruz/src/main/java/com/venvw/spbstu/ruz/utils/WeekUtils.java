package com.venvw.spbstu.ruz.utils;

import com.venvw.spbstu.ruz.models.Week;

import org.joda.time.LocalDate;

public final class WeekUtils {
    private WeekUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isIncludes(Week week, LocalDate date) {
        return date.equals(week.getDateStart()) || date.equals(week.getDateEnd()) ||
                (date.isAfter(week.getDateStart()) && date.isBefore(week.getDateEnd()));
    }
}
