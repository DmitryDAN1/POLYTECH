
package com.venvw.spbstu.ruz.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

public class Day {

    @SerializedName("weekday")
    @Expose
    private Integer weekday;
    @SerializedName("date")
    @Expose
    private LocalDate date;
    @SerializedName("lessons")
    @Expose
    private List<Lesson> lessons = null;

    public Integer getWeekday() {
        return weekday;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

}
