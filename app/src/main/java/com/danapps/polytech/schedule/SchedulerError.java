package com.danapps.polytech.schedule;

import androidx.annotation.NonNull;

public class SchedulerError {

    private String message;
    private SchedulerErrorType type;

    SchedulerError(String message, SchedulerErrorType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public SchedulerErrorType getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", type, message);
    }
}
