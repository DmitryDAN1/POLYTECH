package com.venvw.spbstu.ruz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.venvw.spbstu.ruz.api.FacultiesApi;
import com.venvw.spbstu.ruz.api.SchedulerApi;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.lang.reflect.Type;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RuzService {

    private static final String RUZ_API_URL = "https://ruz.spbstu.ru/api/v1/ruz/";

    private static RuzService instance;

    private Retrofit retrofit;

    private RuzService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(RUZ_API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                            @Override
                            public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return LocalTime.parse(json.getAsString());
                            }
                        })
                        .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                            @Override
                            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return LocalDate.parse(json.getAsString().replace('.', '-'));
                            }
                        })
                        .create()
                ))
                .build();
    }

    public static RuzService getInstance() {
        if(instance == null) {
            instance = new RuzService();
        }

        return instance;
    }

    public FacultiesApi getFacultiesApi() {
        return retrofit.create(FacultiesApi.class);
    }

    public SchedulerApi getSchedulerApi() {
        return retrofit.create(SchedulerApi.class);
    }

}
