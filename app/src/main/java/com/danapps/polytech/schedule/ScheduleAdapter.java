package com.danapps.polytech.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.model.Schedule;
import com.danapps.polytech.schedule.model.schedule.Day;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Schedule schedule;

    public ScheduleAdapter() {
        this.schedule = null;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_day, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        viewHolder.lessonsRecyclerView.setAdapter(new ScheduleDayAdapter());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ScheduleDayAdapter)holder.lessonsRecyclerView.getAdapter()).setDay(schedule.getDays().get(position));
    }

    @Override
    public int getItemCount() {
        if(schedule != null) {
            return schedule.getDays().size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView lessonsRecyclerView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonsRecyclerView = itemView.findViewById(R.id.lessons_recycler_view);
        }
    }
}
