package com.danapps.polytech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.venvw.spbstu.ruz.models.Auditory;
import com.venvw.spbstu.ruz.models.Building;
import com.venvw.spbstu.ruz.models.Lesson;
import com.venvw.spbstu.ruz.models.Teacher;
import com.venvw.spbstu.ruz.models.TypeObj;
import com.venvw.spbstu.ruz.utils.LessonUtils;

import java.util.List;
import java.util.Locale;

public class ScheduleLessonsAdapter extends RecyclerView.Adapter<ScheduleLessonsAdapter.ViewHolder> {
    private Context context;
    private List<Lesson> lessons;

    public ScheduleLessonsAdapter(@NonNull Context context) {
        this.context = context;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_lesson, parent, false);
        return new ViewHolder(view);
    }

    private void bindLessonNumber(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        holder.numberTextView.setText(String.format(Locale.getDefault(), "%d", LessonUtils.getNumber(lesson)));
    }

    private void bindLessonType(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        String typeObjName = context.getString(R.string.unknown);
        if(lesson.getTypeObj() != null) {
            TypeObj typeObj = lesson.getTypeObj();
            if(typeObj != null) {
                typeObjName = typeObj.getName();
            }
        }
        holder.typeTextView.setText(typeObjName);
    }

    private void bindLessonTime(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        String timeString = context.getString(R.string.unknown);
        if(lesson.getTimeStart() != null && lesson.getTimeEnd() != null) {
            timeString = String.format("%s-%s", lesson.getTimeStart().toString("HH:mm"), lesson.getTimeEnd().toString("HH:mm"));
        }
        holder.timeTextView.setText(timeString);
    }

    private void bindLessonName(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        String subjectName = context.getString(R.string.unknown);
        if(lesson.getSubject() != null && !lesson.getSubject().isEmpty()) {
            subjectName = lesson.getSubject();
        }
        holder.subjectTextView.setText(subjectName);
    }

    private void bindLessonTeacher(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        String teacherFullName = context.getString(R.string.unknown);
        if(lesson.getTeachers() != null) {
            Teacher teacher = lesson.getTeachers().get(0);
            if(teacher != null) {
                teacherFullName = teacher.getFullName();
            }
        }
        holder.teacherTextView.setText(teacherFullName);
    }

    private void bindLessonAuditory(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        String auditoryName = context.getString(R.string.unknown),
                buildingName = context.getString(R.string.unknown);
        if(lesson.getAuditories() != null) {
            Auditory auditory = lesson.getAuditories().get(0);
            if(auditory != null) {
                auditoryName = auditory.getName();
                Building building = auditory.getBuilding();
                if(building != null) {
                    buildingName = building.getName();
                }
            }
        }
        holder.locationTextView.setText(String.format(context.getString(R.string.auditory_format),
                buildingName, auditoryName));
    }

    private void bindLesson(@NonNull ViewHolder holder, @NonNull Lesson lesson) {
        bindLessonNumber(holder, lesson);
        bindLessonType(holder, lesson);
        bindLessonTime(holder, lesson);
        bindLessonName(holder, lesson);
        bindLessonTeacher(holder, lesson);
        bindLessonAuditory(holder, lesson);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bindLesson(holder, lessons.get(position));
    }

    @Override
    public int getItemCount() {
        if(lessons != null) {
            return lessons.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView numberTextView;
        final TextView typeTextView;
        final TextView timeTextView;
        final TextView subjectTextView;
        final TextView teacherTextView;
        final TextView locationTextView;

        ViewHolder(@NonNull View lessonView) {
            super(lessonView);
            numberTextView = lessonView.findViewById(R.id.lesson_number);
            typeTextView = lessonView.findViewById(R.id.lesson_type);
            timeTextView = lessonView.findViewById(R.id.lesson_time);
            subjectTextView = lessonView.findViewById(R.id.lesson_subject);
            teacherTextView = lessonView.findViewById(R.id.lesson_teacher);
            locationTextView = lessonView.findViewById(R.id.lesson_location);
        }
    }
}
