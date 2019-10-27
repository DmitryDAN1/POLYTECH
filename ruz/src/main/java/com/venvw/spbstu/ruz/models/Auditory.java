
package com.venvw.spbstu.ruz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auditory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("building")
    @Expose
    private Building building;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Building getBuilding() {
        return building;
    }

}
