package com.danapps.polytech.schedule;

import com.google.gson.annotations.SerializedName;

public class Teacher {
    private String chair;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("full_name")
    private String fullName;
    private String grade;
    private int id;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("middle_name")
    private String middleName;
    private int oid;

    public String getChair() {
        return chair;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGrade() {
        return grade;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public int getOid() {
        return oid;
    }
}
