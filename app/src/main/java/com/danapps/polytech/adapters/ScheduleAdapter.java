package com.danapps.polytech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.google.firebase.database.annotations.NotNull;
import com.venvw.spbstu.ruz.models.Day;
import com.venvw.spbstu.ruz.models.Schedule;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Context context;
    private Schedule schedule;
    private List<ScheduleDayAdapter.StructuredDay> structuredDays;

    public ScheduleAdapter(@NotNull Context context) {
        this.context = context;
        this.structuredDays = new ArrayList<>();
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(@Nullable Schedule schedule) {
        this.schedule = schedule;
        structuredDays.clear();
        for (LocalDate date = schedule.getWeek().getDateStart();
             !date.minusDays(1).equals(schedule.getWeek().getDateEnd());
             date = date.plusDays(1)) {

            ScheduleDayAdapter.StructuredDay day = null;
            if(schedule != null && schedule.getDays() != null) {
                for (Day d : schedule.getDays()) {
                    if(d.getDate().equals(date)) {
                        day = new ScheduleDayAdapter.StructuredDay(d);
                        break;
                    }
                }
            }

            structuredDays.add(day);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_day, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemsRecycler.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        viewHolder.itemsRecycler.setAdapter(new ScheduleDayAdapter(context));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position < structuredDays.size()) {
            ScheduleDayAdapter.StructuredDay day = structuredDays.get(position);
            if(day != null) {
                ((ScheduleDayAdapter) holder.itemsRecycler.getAdapter()).setStructuredDay(day);
                holder.text.setVisibility(View.GONE);
                return;
            }
        }
        ((ScheduleDayAdapter) holder.itemsRecycler.getAdapter()).setStructuredDay(null);
        holder.text.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final RecyclerView itemsRecycler;
        final TextView text;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemsRecycler = itemView.findViewById(R.id.schedule_day_items_recycler);
            text = itemView.findViewById(R.id.schedule_day_text);
        }
    }
}
