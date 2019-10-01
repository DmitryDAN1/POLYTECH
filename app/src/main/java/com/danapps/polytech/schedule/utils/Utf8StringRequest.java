package com.danapps.polytech.schedule.utils;

import com.android.volley.Response;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import java.nio.charset.StandardCharsets;

public class Utf8StringRequest extends StringRequest {
    public Utf8StringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed = new String(response.data, StandardCharsets.UTF_8);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
