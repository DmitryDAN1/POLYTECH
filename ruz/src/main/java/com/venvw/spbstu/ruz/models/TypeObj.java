package com.venvw.spbstu.ruz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeObj {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("abbr")
    @Expose
    private String abbr;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

}
