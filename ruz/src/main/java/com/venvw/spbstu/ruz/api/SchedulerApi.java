package com.venvw.spbstu.ruz.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.venvw.spbstu.ruz.models.Day;
import com.venvw.spbstu.ruz.models.Group;
import com.venvw.spbstu.ruz.models.Schedule;
import com.venvw.spbstu.ruz.models.Week;

import org.joda.time.LocalDate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SchedulerApi {

    public class GetScheduleResponse extends Schedule {

        @SerializedName("error")
        @Expose
        private boolean error;
        @SerializedName("text")
        @Expose
        private String text;

        public boolean isError() {
            return error;
        }

        public String getText() {
            return text;
        }

    }

    @GET("scheduler/{id}")
    public Call<GetScheduleResponse> getSchedule(@Path("id") int groupId);

    @GET("scheduler/{id}")
    public Call<GetScheduleResponse> getSchedule(@Path("id") int groupId, @Query("date") LocalDate localDate);
}
