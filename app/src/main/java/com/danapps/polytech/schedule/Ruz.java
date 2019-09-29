package com.danapps.polytech.schedule;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.date.ScheduleDateAdapter;
import com.danapps.polytech.schedule.time.ScheduleTime;
import com.danapps.polytech.schedule.time.ScheduleTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ruz {
    private RequestQueue requestQueue;
    private Gson gson;

    public Ruz(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder()
                .registerTypeAdapter(ScheduleDate.class, new ScheduleDateAdapter())
                .registerTypeAdapter(ScheduleTime.class, new ScheduleTimeAdapter())
                .create();
    }

    public Scheduler newScheduler() {
        return new Scheduler(requestQueue, gson);
    }
}
