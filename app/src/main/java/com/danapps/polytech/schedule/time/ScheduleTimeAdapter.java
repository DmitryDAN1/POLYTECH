package com.danapps.polytech.schedule.time;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ScheduleTimeAdapter implements JsonSerializer<ScheduleTime>, JsonDeserializer<ScheduleTime> {
    @Override
    public JsonElement serialize(ScheduleTime src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getHour() + ":" + src.getMinute());
    }

    @Override
    public ScheduleTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String timeString = json.getAsString();
        String[] hourMinuteString = timeString.split(":");

        if(hourMinuteString.length != 2) {
            throw new JsonParseException("ScheduleDate has wrong format");
        }

        int hour = Integer.parseInt(hourMinuteString[0]);
        int minute = Integer.parseInt(hourMinuteString[1]);
        return new ScheduleTime(hour * 60 + minute);
    }
}
