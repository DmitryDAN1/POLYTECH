package com.danapps.polytech.schedule.model.schedule;

import com.danapps.polytech.schedule.model.Group;
import com.danapps.polytech.schedule.time.ScheduleTime;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lesson {
    @SerializedName("additional_info")
    private String additionalInfo;
    private List<Auditory> auditories;
    private List<Group> groups;
    private int parity;
    private String subject;
    @SerializedName("subject_short")
    private String subjectShort;
    private List<Teacher> teachers;
    @SerializedName("time_end")
    private ScheduleTime timeEnd;
    @SerializedName("time_start")
    private ScheduleTime timeStart;
    private int type;
    private TypeObj typeObj;

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public List<Auditory> getAuditories() {
        return auditories;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public int getParity() {
        return parity;
    }

    public String getSubject() {
        return subject;
    }

    public String getSubjectShort() {
        return subjectShort;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public ScheduleTime getTimeEnd() {
        return timeEnd;
    }

    public ScheduleTime getTimeStart() {
        return timeStart;
    }

    public int getType() {
        return type;
    }

    public TypeObj getTypeObj() {
        return typeObj;
    }
}
