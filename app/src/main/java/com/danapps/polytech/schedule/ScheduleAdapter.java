package com.danapps.polytech.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.date.ScheduleDate;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.model.schedule.Day;
import com.google.firebase.database.annotations.NotNull;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Context context;
    private Schedule schedule;

    public ScheduleAdapter(@NotNull Context context) {
        this.context = context;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(@Nullable Schedule schedule) {
        this.schedule = schedule;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_day, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        viewHolder.lessonsRecyclerView.setAdapter(new ScheduleDayAdapter(context));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Day day = null;
        if(schedule != null && position < schedule.getDays().size()) {
            day = schedule.getDays().get(position);
        }
        ((ScheduleDayAdapter) holder.lessonsRecyclerView.getAdapter()).setDay(day);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView lessonsRecyclerView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonsRecyclerView = itemView.findViewById(R.id.schedule_items_recycler_view);
        }
    }
}
