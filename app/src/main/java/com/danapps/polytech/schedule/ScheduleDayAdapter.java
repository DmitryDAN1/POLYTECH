package com.danapps.polytech.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.model.schedule.Day;
import com.danapps.polytech.schedule.model.schedule.Lesson;

import java.util.Locale;

public class ScheduleDayAdapter extends RecyclerView.Adapter<ScheduleDayAdapter.ViewHolder> {
    private Day day;

    public ScheduleDayAdapter() {
        this.day = null;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleDayAdapter.ViewHolder holder, int position) {
        Lesson lesson = day.getLessons().get(position);
        holder.lessonNumberTV.setText(String.format(Locale.getDefault(), "%d", position + 1));
        if(lesson.getTypeObj() != null) {
            holder.lessonTypeTV.setText(lesson.getTypeObj().getName());
        }
        if(lesson.getTimeStart() != null && lesson.getTimeEnd() != null) {
            holder.lessonTimeTV.setText(String.format("%s - %s",
                    lesson.getTimeStart().toString(), lesson.getTimeEnd().toString()));
        }
        holder.lessonNameTV.setText(lesson.getSubject());
        if(lesson.getTeachers() != null && lesson.getTeachers().get(0) != null) {
            holder.lessonTeacherTV.setText(lesson.getTeachers().get(0).getFullName());
        }
        if(lesson.getAuditories() != null && lesson.getAuditories().get(0) != null) {
            holder.lessonGeoTV.setText(lesson.getAuditories().get(0).getName());
        }
    }

    @Override
    public int getItemCount() {
        return day.getLessons().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lessonNumberTV;
        TextView lessonTypeTV;
        TextView lessonTimeTV;
        TextView lessonNameTV;
        TextView lessonTeacherTV;
        TextView lessonGeoTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            lessonNumberTV  = itemView.findViewById(R.id.lessonNumber);
            lessonTypeTV    = itemView.findViewById(R.id.lessonType);
            lessonTimeTV    = itemView.findViewById(R.id.lessonTime);
            lessonNameTV    = itemView.findViewById(R.id.lessonName);
            lessonTeacherTV = itemView.findViewById(R.id.lessonTeacher);
            lessonGeoTV     = itemView.findViewById(R.id.lessonGeo);
        }
    }
}