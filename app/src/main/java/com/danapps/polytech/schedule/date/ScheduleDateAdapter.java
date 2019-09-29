package com.danapps.polytech.schedule.date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ScheduleDateAdapter implements JsonSerializer<ScheduleDate>, JsonDeserializer<ScheduleDate> {
    @Override
    public JsonElement serialize(ScheduleDate src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getYear() + "-" + src.getMonth() + "-" + src.getDay());
    }

    @Override
    public ScheduleDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        String[] yearMonthDayString = dateString.split("\\.");
        if(yearMonthDayString.length != 3) {
            yearMonthDayString = dateString.split("-");
            if(yearMonthDayString.length != 3) {
                throw new JsonParseException("ScheduleDate has wrong format");
            }
        }

        int year = Integer.parseInt(yearMonthDayString[0]);
        int month = Integer.parseInt(yearMonthDayString[1]);
        int day = Integer.parseInt(yearMonthDayString[2]);

        return new ScheduleDate(year, month, day);
    }
}
