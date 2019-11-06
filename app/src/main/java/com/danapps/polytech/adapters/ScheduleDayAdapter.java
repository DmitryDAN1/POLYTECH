package com.danapps.polytech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.venvw.spbstu.ruz.models.Day;
import com.venvw.spbstu.ruz.models.Lesson;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class ScheduleDayAdapter extends RecyclerView.Adapter<ScheduleDayAdapter.ViewHolder> {
    static class StructuredDay {
        class Window {
            final LocalTime begin;
            final LocalTime end;

            Window(LocalTime begin, LocalTime end) {
                this.begin = begin;
                this.end = end;
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
        private final LocalDate date;

        StructuredDay(Day day) {
            date = day.getDate();
            items = new ArrayList<>();

            if(day.getLessons() == null || day.getLessons().size() < 1) {
                return;
            }

            List<Lesson> previousLessons = new ArrayList<>();
            previousLessons.add(day.getLessons().get(0));
            for(int i = 1; i < day.getLessons().size(); ++i) {
                Lesson lesson = day.getLessons().get(i),
                        previous = previousLessons.get(previousLessons.size() - 1);

                Duration difference = new Duration(previous.getTimeEnd().getMillisOfDay(),
                        lesson.getTimeStart().getMillisOfDay());

                if(previous.getTimeStart().getMillisOfDay() - lesson.getTimeStart().getMillisOfDay() == 0) {
                    previousLessons.add(lesson);
                } else {
                    items.add(new Item(previousLessons));
                    previousLessons = new ArrayList<>();
                    if(difference.getStandardMinutes() >= 90) {
                        items.add(new Item(new Window(previous.getTimeEnd(), lesson.getTimeStart())));
                    }
                    previousLessons.add(lesson);
                }
            }
            items.add(new Item(previousLessons));

        }

        final List<Item> getItems() {
            return items;
        }
        final LocalDate getDate() { return date; }
    }

    private final Context context;
    private StructuredDay day;

    public ScheduleDayAdapter(@NonNull Context context) {
        this.context = context;
    }

    public StructuredDay getDay() {
        return day;
    }

    public void setStructuredDay(@Nullable StructuredDay day) {
        this.day = day;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    private void bindLessons(@NonNull LessonsViewHolder holder, @NonNull List<Lesson> lessons) {
        ScheduleLessonsAdapter adapter = (ScheduleLessonsAdapter)holder.lessonsRecyclerView.getAdapter();
        adapter.setLessons(lessons);
        if(lessons.size() < 2) {
            holder.switchButton.setVisibility(View.GONE);
            holder.indicator.setVisibility(View.GONE);
        } else {
            holder.switchButton.setVisibility(View.VISIBLE);
            holder.indicator.setVisibility(View.VISIBLE);
        }
    }

    private void bindWindowText(@NonNull WindowViewHolder holder, @NonNull StructuredDay.Window window) {
        Duration duration = Duration.millis(window.end.getMillisOfDay() - window.begin.getMillisOfDay());
        String deltaTimeString = String.format(context.getString(R.string.schedule_delta_time_format),
                duration.getStandardHours(), duration.getStandardMinutes() % 60);
        holder.textView.setText(String.format(context.getString(R.string.schedule_window_text),
                window.begin.toString("HH:mm"), deltaTimeString));
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
        final ScrollingPagerIndicator indicator;

        LessonsViewHolder(@NonNull View lessonsView) {
            super(lessonsView);
            lessonsRecyclerView = lessonsView.findViewById(R.id.schedule_lessons_recycler_view);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            lessonsRecyclerView.setLayoutManager(linearLayoutManager);
            lessonsRecyclerView.setAdapter(new ScheduleLessonsAdapter(context));
            lessonsRecyclerView.setNestedScrollingEnabled(false);

            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(lessonsRecyclerView);

            indicator = lessonsView.findViewById(R.id.schedule_lessons_indicator);
            indicator.attachToRecyclerView(lessonsRecyclerView);

            switchButton = lessonsView.findViewById(R.id.schedule_lessons_switch_button);

            switchButton.setOnClickListener(v -> lessonsRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) lessonsRecyclerView.getLayoutManager();
                    int position = linearLayoutManager1.findFirstVisibleItemPosition() + 1;
                    if (position >= lessonsRecyclerView.getAdapter().getItemCount()) {
                        position = 0;
                    }

                    lessonsRecyclerView.smoothScrollToPosition(position);
                }
            }));
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