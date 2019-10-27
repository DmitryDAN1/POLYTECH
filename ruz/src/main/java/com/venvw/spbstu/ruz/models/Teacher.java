package com.venvw.spbstu.ruz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Teacher {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("oid")
    @Expose
    private Integer oid;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("chair")
    @Expose
    private String chair;

    public Integer getId() {
        return id;
    }

    public Integer getOid() {
        return oid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGrade() {
        return grade;
    }

    public String getChair() {
        return chair;
    }

}
