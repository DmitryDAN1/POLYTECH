package com.venvw.spbstu.ruz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("kind")
    @Expose
    private Integer kind;
    @SerializedName("spec")
    @Expose
    private String spec;
    @SerializedName("year")
    @Expose
    private Integer year;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public Integer getKind() {
        return kind;
    }

    public String getSpec() {
        return spec;
    }

    public Integer getYear() {
        return year;
    }

}