package com.danapps.polytech.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.model.schedule.Day;
import com.danapps.polytech.schedule.model.schedule.Lesson;
import com.danapps.polytech.schedule.time.ScheduleDeltaTime;
import com.danapps.polytech.schedule.time.ScheduleTime;

import org.byters.dotsindicator.ViewDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDayAdapter extends RecyclerView.Adapter<ScheduleDayAdapter.ViewHolder> {
    class StructuredDay {
        class Window {
            final ScheduleTime from;
            final ScheduleDeltaTime delta;

            Window(ScheduleTime from, ScheduleDeltaTime delta) {
                this.from = from;
                this.delta = delta;
            }
        }

        class Item {
            final List<Lesson> lessons;
            final Window window;

            boolean isLessons() {
                return lessons != null;
            }

            boolean isWindow() {
                return window != null;
            }

            Item(@NonNull List<Lesson> lesson) {
                this.lessons = lesson;
                this.window = null;
            }

            Item(@NonNull Window window) {
                this.lessons = null;
                this.window = window;
            }
        }

        private final List<Item> items;

        StructuredDay(Day day) {
            items = new ArrayList<>();
            if(day.getLessons() == null || day.getLessons().size() < 1) {
                return;
            }

            List<Lesson> previousLessons = new ArrayList<>();
            previousLessons.add(day.getLessons().get(0));
            for(int i = 1; i < day.getLessons().size(); ++i) {
                Lesson lesson = day.getLessons().get(i),
                        previous = previousLessons.get(previousLessons.size() - 1);

                ScheduleDeltaTime difference = ScheduleDeltaTime.difference(previous.getTimeEnd(),
                        lesson.getTimeStart());

                if(ScheduleDeltaTime.difference(previous.getTimeStart(), lesson.getTimeStart()).isZero() &&
                    ScheduleDeltaTime.difference(previous.getTimeEnd(), lesson.getTimeEnd()).isZero()) {
                    previousLessons.add(lesson);
                } else {
                    items.add(new Item(previousLessons));
                    previousLessons = new ArrayList<>();
                    if(difference.isWindow()) {
                        items.add(new Item(new Window(previous.getTimeEnd(), difference)));
                    }
                    previousLessons.add(lesson);
                }
            }
            items.add(new Item(previousLessons));
        }

        final List<Item> getItems() {
            return items;
        }
    }

    private final Context context;
    private StructuredDay day;

    public ScheduleDayAdapter(@NonNull Context context) {
        this.context = context;
    }

    public StructuredDay getDay() {
        return day;
    }

    public void setDay(@Nullable Day day) {
        if(day != null) {
            this.day = new StructuredDay(day);
        } else {
            this.day = null;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        RecyclerView lessonsRecyclerView = viewHolder.lessonsViewHolder.lessonsRecyclerView;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        lessonsRecyclerView.setLayoutManager(linearLayoutManager);
        lessonsRecyclerView.setNestedScrollingEnabled(false);
        lessonsRecyclerView.setAdapter(new ScheduleLessonsAdapter(context));

        return viewHolder;
    }

    private void bindLessons(@NonNull LessonsViewHolder holder, @NonNull List<Lesson> lessons) {
        ScheduleLessonsAdapter adapter = (ScheduleLessonsAdapter)holder.lessonsRecyclerView.getAdapter();
        adapter.setLessons(lessons);
        if(lessons.size() < 2) {
            holder.switchButton.setVisibility(View.GONE);
            holder.viewDotsIndicator.setVisibility(View.GONE);
        } else {
            holder.switchButton.setVisibility(View.VISIBLE);
            holder.viewDotsIndicator.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) holder.lessonsRecyclerView.getLayoutManager();
            int position = linearLayoutManager.findFirstVisibleItemPosition();
            if(position < 0) {
                position = 0;
            }
            holder.viewDotsIndicator.updateData(lessons.size(), position);
        }
    }

    private void bindWindowText(@NonNull WindowViewHolder holder, @NonNull StructuredDay.Window window) {
        String deltaTimeString = String.format(context.getString(R.string.schedule_delta_time_format),
                window.delta.getHours(), window.delta.getMinutes());
        holder.textView.setText(String.format(context.getString(R.string.schedule_window_text),
                window.from, deltaTimeString));
    }

    private void bindWindow(@NonNull WindowViewHolder holder, @NonNull StructuredDay.Window window) {
        bindWindowText(holder, window);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StructuredDay.Item item = day.getItems().get(position);
        if(item.isLessons()) {
            holder.itemView.findViewById(R.id.schedule_lessons).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.schedule_window).setVisibility(View.GONE);
            bindLessons(holder.lessonsViewHolder, item.lessons);
        } else if(item.isWindow()) {
            holder.itemView.findViewById(R.id.schedule_lessons).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.schedule_window).setVisibility(View.VISIBLE);
            bindWindow(holder.windowViewHolder, item.window);
        }
    }

    @Override
    public int getItemCount() {
        if(day != null) {
            return day.getItems().size();
        }
        return 0;
    }

    class LessonsViewHolder extends RecyclerView.ViewHolder {
        final RecyclerView lessonsRecyclerView;
        final Button switchButton;
        final ViewDotsIndicator viewDotsIndicator;

        LessonsViewHolder(@NonNull View lessonsView) {
            super(lessonsView);
            lessonsRecyclerView = lessonsView.findViewById(R.id.schedule_lessons_recycler_view);
            switchButton = lessonsView.findViewById(R.id.schedule_lessons_switch_button);
            viewDotsIndicator = lessonsView.findViewById(R.id.schedule_lessons_dots_indicator);
            viewDotsIndicator.init(lessonsRecyclerView);

            switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lessonsRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) lessonsRecyclerView.getLayoutManager();
                            int position = linearLayoutManager.findFirstVisibleItemPosition() + 1;
                            if (position >= lessonsRecyclerView.getAdapter().getItemCount()) {
                                position = 0;
                            }

                            lessonsRecyclerView.smoothScrollToPosition(position);
                            viewDotsIndicator.updateData(lessonsRecyclerView.getAdapter().getItemCount(), position);
                        }
                    });
                }
            });
        }
    }

    class WindowViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        WindowViewHolder(@NonNull View windowView) {
            super(windowView);
            textView = windowView.findViewById(R.id.window_text);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final LessonsViewHolder lessonsViewHolder;
        final WindowViewHolder windowViewHolder;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonsViewHolder = new LessonsViewHolder(itemView.findViewById(R.id.schedule_lessons));
            windowViewHolder = new WindowViewHolder(itemView.findViewById(R.id.schedule_window));
        }
    }
}