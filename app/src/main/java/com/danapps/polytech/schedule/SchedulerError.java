package com.danapps.polytech.schedule;

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
}
