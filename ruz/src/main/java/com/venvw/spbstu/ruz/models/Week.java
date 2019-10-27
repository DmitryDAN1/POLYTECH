package com.venvw.spbstu.ruz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

public class Week {

    @SerializedName("date_start")
    @Expose
    private LocalDate dateStart;
    @SerializedName("date_end")
    @Expose
    private LocalDate dateEnd;
    @SerializedName("is_odd")
    @Expose
    private Boolean isOdd;

    public LocalDate getDateStart() {
        return dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public Boolean getIsOdd() {
        return isOdd;
    }

}
