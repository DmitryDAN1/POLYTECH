package com.danapps.polytech.schedule.model.schedule;

import com.danapps.polytech.schedule.date.ScheduleDate;
import com.google.gson.annotations.SerializedName;

public class Week {
    @SerializedName("date_end")
    private ScheduleDate dateEnd;
    @SerializedName("date_start")
    private ScheduleDate dateStart;
    @SerializedName("is_odd")
    private boolean isOdd;

    public ScheduleDate getDateEnd() {
        return dateEnd;
    }

    public ScheduleDate getDateStart() {
        return dateStart;
    }

    public boolean isOdd() {
        return isOdd;
    }
}
