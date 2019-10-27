package com.venvw.spbstu.ruz.utils;

import com.venvw.spbstu.ruz.models.Lesson;

public class LessonUtils {
    private LessonUtils() {
        throw new UnsupportedOperationException();
    }

    public static int getNumber(Lesson lesson) {
        return (lesson.getTimeStart().getHourOfDay() - 8) / 2 + 1;
    }
}
