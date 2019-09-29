package com.danapps.polytech.schedule;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.danapps.polytech.schedule.model.Group;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.utils.Utf8StringRequest;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.time.LocalDate;

public class Scheduler {
    private static final String SCHEDULER_URL_BASE = "https://ruz.spbstu.ru/api/v1/ruz/scheduler/";

    public interface Listener {
        void onResponseReady(Schedule schedule);
        void onResponseError(SchedulerError error);
    }

    private RequestQueue requestQueue;
    private Gson gson;

    Scheduler(RequestQueue requestQueue, Gson gson) {
        this.requestQueue = requestQueue;
        this.gson = gson;
    }

    public void querySchedule(int groupId, @Nullable LocalDate date, @NotNull final Listener listener) {
        String url = SCHEDULER_URL_BASE + groupId;
        if(date != null) {
            url += "?date=" + date;
        }

        StringRequest request = new Utf8StringRequest(Request.Method.GET, url, response -> {
            Schedule schedule;
            try {
                schedule = gson.fromJson(response, Schedule.class);
            } catch (JsonParseException e) {
                listener.onResponseError(new SchedulerError(e.getMessage(), SchedulerErrorType.ParsingFailed));
                return;
            }
            listener.onResponseReady(schedule);
        }, error -> listener.onResponseError(new SchedulerError(error.getMessage(), SchedulerErrorType.NetworkFailed)));

        requestQueue.add(request);
    }

    public void querySchedule(@NotNull Group group, @Nullable LocalDate date, @NotNull final Listener listener) {
        querySchedule(group.getId(), date, listener);
    }
}
