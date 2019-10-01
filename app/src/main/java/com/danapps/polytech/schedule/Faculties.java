package com.danapps.polytech.schedule;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.danapps.polytech.schedule.model.Faculty;
import com.danapps.polytech.schedule.utils.Utf8StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.List;

public class Faculties {
    private static final String FACULTIES_URL_BASE = "https://ruz.spbstu.ru/api/v1/ruz/faculties/";

    public interface Listener {
        void onResponseReady(List<Faculty> faculties);
        void onResponseError(SchedulerError error);
    }

    private class Response {
        List<Faculty> faculties;
    }

    private RequestQueue requestQueue;
    private Gson gson;

    Faculties(RequestQueue requestQueue, Gson gson) {
        this.requestQueue = requestQueue;
        this.gson = gson;
    }

    public void queryFaculties(Listener listener) {
        requestQueue.add(new Utf8StringRequest(Request.Method.GET, FACULTIES_URL_BASE, response -> {
            List<Faculty> faculties;
            try {
                faculties = gson.fromJson(response, Response.class).faculties;
            } catch (JsonParseException e) {
                listener.onResponseError(new SchedulerError(e.getMessage(), SchedulerErrorType.ParsingFailed));
                return;
            }
            listener.onResponseReady(faculties);
        }, error ->
                listener.onResponseError(new SchedulerError(error.getMessage(), SchedulerErrorType.NetworkFailed))));
    }
}
