package com.danapps.polytech.schedule;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Group;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.utils.Utf8StringRequest;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class Scheduler {
    private static final String SCHEDULER_URL_BASE = "https://ruz.spbstu.ru/api/v1/ruz/scheduler/";

    public interface Listener {
        void onResponseReady(Schedule schedule);
        void onResponseError(SchedulerError error);
    }

    private class Response extends Schedule {
        boolean error;
        String text;
    }

    private RequestQueue requestQueue;
    private Gson gson;

    Scheduler(RequestQueue requestQueue, Gson gson) {
        this.requestQueue = requestQueue;
        this.gson = gson;
    }

    public Request querySchedule(int groupId, @Nullable ScheduleDate date, @NotNull final Listener listener) {
        String url = SCHEDULER_URL_BASE + groupId;
        if(date != null) {
            url += "?date=" + date;
        }

        Request request = new Utf8StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Scheduler.Response schedulerResponse;
                try {
                    schedulerResponse = gson.fromJson(response, Scheduler.Response.class);
                } catch (JsonParseException e) {
                    listener.onResponseError(new SchedulerError(e.getMessage(), SchedulerErrorType.ParsingFailed));
                    return;
                }
                if(schedulerResponse.error) {
                    listener.onResponseError(new SchedulerError(schedulerResponse.text, SchedulerErrorType.ParsingFailed));
                }

                listener.onResponseReady(schedulerResponse);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onResponseError(new SchedulerError(error.getMessage(), SchedulerErrorType.NetworkFailed));
            }
        });

        requestQueue.add(request);

        return request;
    }

    public void querySchedule(@NotNull Group group, @Nullable ScheduleDate date, @NotNull final Listener listener) {
        querySchedule(group.getId(), date, listener);
    }
}
