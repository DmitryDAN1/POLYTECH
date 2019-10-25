package com.danapps.polytech.schedule.utils;

import com.danapps.polytech.schedule.model.schedule.Lesson;

public final class LessonUtils {
    private LessonUtils() {
        throw new UnsupportedOperationException();
    }

    public static int getNumber(Lesson lesson) {
        return (lesson.getTimeStart().getHour() - 8) / 2 + 1;
    }
}
