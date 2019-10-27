package com.venvw.spbstu.ruz.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.venvw.spbstu.ruz.models.Faculty;
import com.venvw.spbstu.ruz.models.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FacultiesApi {

    public class GetFacultiesResponse {

        @SerializedName("faculties")
        @Expose
        private List<Faculty> faculties;

        public List<Faculty> getFaculties() {
            return faculties;
        }

    }

    @GET("faculties")
    public Call<GetFacultiesResponse> getFaculties();

    public class GetFacultyResponse extends Faculty {

        @SerializedName("error")
        @Expose
        private boolean error;
        @SerializedName("text")
        @Expose
        private String text;

        public boolean isError() {
            return error;
        }

        public String getText() {
            return text;
        }

    }

    @GET("faculties/{id}")
    public Call<GetFacultyResponse> getFaculty(@Path("id") int facultyId);

    public class GetGroupsResponse {

        @SerializedName("groups")
        @Expose
        private List<Group> groups;
        @SerializedName("faculty")
        @Expose
        private Faculty faculty;

        public List<Group> getGroups() {
            return groups;
        }

        public Faculty getFaculty() {
            return faculty;
        }

    }

    @GET("faculties/{id}/groups")
    public Call<GetGroupsResponse> getGroups(@Path("id") int facultyId);
}
