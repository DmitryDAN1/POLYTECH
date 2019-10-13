package com.danapps.polytech.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ScheduleListItem> listItems;
    private Context context;

    public ScheduleAdapter(List<ScheduleListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_list_item, parent, false);

        return new ScheduleAdapter.ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        ScheduleListItem listItem = listItems.get(position);

        holder.lessonIdTV.setText(listItem.getLessonID());
        holder.lessonTypeTV.setText(listItem.getLessonType());
        holder.lessonTimeTV.setText(listItem.getLessonTime());
        holder.lessonNameTV.setText(listItem.getLessonName());
        holder.lessonTeacherTV.setText(listItem.getLessonTeacher());
        holder.lessonGeoTV.setText(listItem.getLessonGeo());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView lessonIdTV;
        TextView lessonTypeTV;
        TextView lessonTimeTV;
        TextView lessonNameTV;
        TextView lessonTeacherTV;
        TextView lessonGeoTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            lessonIdTV = itemView.findViewById(R.id.lessonId);
            lessonTypeTV = itemView.findViewById(R.id.lessonType);
            lessonTimeTV = itemView.findViewById(R.id.lessonTime);
            lessonNameTV = itemView.findViewById(R.id.lessonName);
            lessonTeacherTV = itemView.findViewById(R.id.lessonTeacher);
            lessonGeoTV = itemView.findViewById(R.id.lessonGeo);
        }
    }
}