package com.danapps.polytech.schedule.model;

import com.google.gson.annotations.SerializedName;

public class Week {
    @SerializedName("date_end")
    private String dateEnd;
    @SerializedName("date_start")
    private String dateStart;
    @SerializedName("is_odd")
    private boolean isOdd;

    public String getDateEnd() {
        return dateEnd;
    }

    public String getDateStart() {
        return dateStart;
    }

    public boolean isOdd() {
        return isOdd;
    }
}
