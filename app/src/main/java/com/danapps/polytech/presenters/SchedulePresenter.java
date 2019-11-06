package com.danapps.polytech.presenters;

import androidx.annotation.NonNull;

import com.danapps.polytech.R;
import com.danapps.polytech.repositories.UserPreferencesRepository;
import com.danapps.polytech.views.ScheduleView;
import com.google.android.material.snackbar.Snackbar;
import com.venvw.spbstu.ruz.RuzService;
import com.venvw.spbstu.ruz.api.SchedulerApi;
import com.venvw.spbstu.ruz.models.Schedule;
import com.venvw.spbstu.ruz.utils.WeekUtils;

import org.joda.time.LocalDate;

import retrofit2.Call;
import retrofit2.Response;

public class SchedulePresenter {

    private UserPreferencesRepository userPreferences;

    private ScheduleView view;

    private SchedulerApi schedulerApi;
    private Schedule schedule;
    private Call<SchedulerApi.GetScheduleResponse> getScheduleCall;

    public SchedulePresenter(@NonNull UserPreferencesRepository userPreferences) {
        this.userPreferences = userPreferences;
        this.schedulerApi = RuzService.getInstance().getSchedulerApi();
    }

    public void attachToView(@NonNull ScheduleView view) {
        this.view = view;
    }

    public void presentForSpecificDate(LocalDate date) {
        if(getScheduleCall != null) {
            return;
        }

        int weekday = date.getDayOfWeek() - 1;

        if(schedule != null) {
            if(WeekUtils.isIncludes(schedule.getWeek(), date)) {
                view.smoothScrollToWeekday(weekday);
                //view.setDate(date);
                return;
            }
        }

        view.showProgressBar();

        int groupId = userPreferences.getGroupId();
        getScheduleCall = schedulerApi.getSchedule(groupId, date);
        getScheduleCall.enqueue(new retrofit2.Callback<SchedulerApi.GetScheduleResponse>() {
            private void finishScheduleCall() {
                view.hideProgressBar();
                getScheduleCall = null;
            }

            @Override
            public void onResponse(@NonNull Call<SchedulerApi.GetScheduleResponse> call, @NonNull Response<SchedulerApi.GetScheduleResponse> response) {
                if(response.body().isError()) {
                    view.showSnackbar(R.string.schedule_parsing_failed, Snackbar.LENGTH_SHORT);
                } else {
                    schedule = response.body();

                    view.setSchedule(schedule);
                    view.setDate(date);
                }
                finishScheduleCall();
            }

            @Override
            public void onFailure(@NonNull Call<SchedulerApi.GetScheduleResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                finishScheduleCall();
            }
        });
    }

    public void presentDefault() {
        LocalDate date = LocalDate.now();
        if(date.getDayOfWeek() == 7) {
            presentForSpecificDate(date.plusDays(1));
        } else {
            presentForSpecificDate(date);
        }
    }

    public void detachFromView() {
        if(getScheduleCall != null) {
            getScheduleCall.cancel();
            getScheduleCall = null;
        }
    }

}
