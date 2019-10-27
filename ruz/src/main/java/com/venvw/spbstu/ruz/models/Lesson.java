package com.venvw.spbstu.ruz.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalTime;

public class Lesson {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("subject_short")
    @Expose
    private String subjectShort;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("additional_info")
    @Expose
    private String additionalInfo;
    @SerializedName("time_start")
    @Expose
    private LocalTime timeStart;
    @SerializedName("time_end")
    @Expose
    private LocalTime timeEnd;
    @SerializedName("parity")
    @Expose
    private Integer parity;
    @SerializedName("typeObj")
    @Expose
    private TypeObj typeObj;
    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;
    @SerializedName("teachers")
    @Expose
    private List<Teacher> teachers = null;
    @SerializedName("auditories")
    @Expose
    private List<Auditory> auditories = null;

    public String getSubject() {
        return subject;
    }

    public String getSubjectShort() {
        return subjectShort;
    }

    public Integer getType() {
        return type;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public Integer getParity() {
        return parity;
    }

    public TypeObj getTypeObj() {
        return typeObj;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Auditory> getAuditories() {
        return auditories;
    }

}
