package com.venvw.spbstu.ruz;

import com.venvw.spbstu.ruz.api.FacultiesApi;
import com.venvw.spbstu.ruz.api.SchedulerApi;
import com.venvw.spbstu.ruz.models.Faculty;
import com.venvw.spbstu.ruz.models.Group;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class FacultiesApiUnitTests {

    private FacultiesApi facultiesApi;
    private SchedulerApi schedulerApi;

    public FacultiesApiUnitTests() {
        facultiesApi = RuzService.getInstance().getFacultiesApi();
        schedulerApi = RuzService.getInstance().getSchedulerApi();
    }

    @Test
    public void getFacultiesTest() throws IOException {
        List<Faculty> faculties = facultiesApi.getFaculties().execute().body().getFaculties();
        Assert.assertFalse(faculties.isEmpty());
    }

    @Test
    public void getExistentFacultyTest() throws IOException {
        List<Faculty> faculties = facultiesApi.getFaculties().execute().body().getFaculties();

        for(Faculty faculty : faculties) {
            Assert.assertFalse(facultiesApi.getFaculty(faculty.getId()).execute().body().isError());
        }
    }

    @Test
    public void getNonExistentFacultyTest() throws IOException {
        Assert.assertTrue(facultiesApi.getFaculty(-1).execute().body().isError());
    }

    @Test
    public void getExistentGroupsTest() throws IOException {
        List<Faculty> faculties = facultiesApi.getFaculties().execute().body().getFaculties();

        for(Faculty faculty : faculties) {
            Assert.assertEquals(200, facultiesApi.getGroups(faculty.getId()).execute().code());
        }
    }

    @Test
    public void getNonExistentGroupsTest() throws IOException {
        Assert.assertEquals(500, facultiesApi.getGroups(-1).execute().code());
    }

    @Test
    public void getRandomExistentSchedulesTest() throws IOException {
        List<Faculty> faculties = facultiesApi.getFaculties().execute().body().getFaculties();
        Random random = new Random();
        for(Faculty faculty : faculties) {
            List<Group> groups = facultiesApi.getGroups(faculty.getId()).execute().body().getGroups();
            if(!groups.isEmpty()) {
                int groupId = groups.get(random.nextInt(groups.size())).getId();
                SchedulerApi.GetScheduleResponse response = schedulerApi.getSchedule(groupId, LocalDate.now()).execute().body();
                Assert.assertFalse(String.format("'%s' - для группы %d. Возможны проблемы со стороны сервера расписаний",
                        response.getText(), groupId), response.isError());
            }
        }
    }

    @Test
    public void getNonExistentScheduleTest() throws IOException {
        Assert.assertTrue(schedulerApi.getSchedule(0xdead, LocalDate.now()).execute().body().isError());
    }

}
