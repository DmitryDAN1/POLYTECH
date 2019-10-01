package com.danapps.polytech.schedule;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.danapps.polytech.schedule.model.Faculty;
import com.danapps.polytech.schedule.model.Group;
import com.danapps.polytech.schedule.utils.Utf8StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.List;
import java.util.Locale;

public class Groups {
    private static final String GROUPS_URL_BASE = "https://ruz.spbstu.ru/api/v1/ruz/faculties/%d/groups/";

    public interface Listener {
        void onResponseReady(List<Group> groups);
        void onResponseError(SchedulerError error);
    }

    private class Response {
        List<Group> groups;
    }

    private RequestQueue requestQueue;
    private Gson gson;

    Groups(RequestQueue requestQueue, Gson gson) {
        this.requestQueue = requestQueue;
        this.gson = gson;
    }

    public void queryGroups(int facultyId, Groups.Listener listener) {
        requestQueue.add(new Utf8StringRequest(Request.Method.GET, String.format(Locale.getDefault(), GROUPS_URL_BASE, facultyId), response -> {
            List<Group> groups;
            try {
                groups = gson.fromJson(response, Response.class).groups;
            } catch (JsonParseException e) {
                listener.onResponseError(new SchedulerError(e.getMessage(), SchedulerErrorType.ParsingFailed));
                return;
            }
            listener.onResponseReady(groups);
        }, error ->
                listener.onResponseError(new SchedulerError(error.getMessage(), SchedulerErrorType.NetworkFailed))));
    }

    public void queryGroups(Faculty faculty, Groups.Listener listener) {
        this.queryGroups(faculty.getId(), listener);
    }
}
