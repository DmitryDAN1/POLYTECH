package com.danapps.polytech.schedule;

public class ScheduleListItem {

    private String lessonID;
    private String lessonType;
    private String lessonTime;
    private String lessonName;
    private String lessonTeacher;
    private String lessonGeo;

    public ScheduleListItem(String lessonID, String lessonType, String lessonTime, String lessonName, String lessonTeacher, String lessonGeo) {
        this.lessonID = lessonID;
        this.lessonType = lessonType;
        this.lessonTime = lessonTime;
        this.lessonName = lessonName;
        this.lessonTeacher = lessonTeacher;
        this.lessonGeo = lessonGeo;
    }

    public String getLessonID() {
        return lessonID;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getLessonTeacher() {
        return lessonTeacher;
    }

    public String getLessonGeo() {
        return lessonGeo;
    }
}
