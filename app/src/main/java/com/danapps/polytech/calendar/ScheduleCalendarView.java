package com.danapps.polytech.calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.google.firebase.database.annotations.NotNull;

import org.joda.time.LocalDate;

import java.util.Calendar;

public class ScheduleCalendarView extends View {

    public interface OnDateSelectedListener {
        void onDateSelected(LocalDate date);
    }

    private static final int WEEKDAYS_COUNT = 7;

    private final String[] shortWeekdays;

    private TextPaint   weekdayPaint;
    private TextPaint   datePaint;
    private Rect        dateTextBounds;
    private TextPaint   highlightedDatePaint;
    private TextPaint   dateTodayTextPaint;
    private Paint       indicatorPaint;

    private float spaceBetweenDatesWeekdays;
    private float indicatorRadius;

    private DatePickerDialog datePickerDialog;
    private LocalDate date;
    private OnDateSelectedListener onDateSelectedListener;
    private RecyclerView recyclerView;
    private RecyclerView.OnScrollListener onScrollListener;

    private float relativeIndicatorPosition;

    private GestureDetector gestureDetector;

    public ScheduleCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        shortWeekdays = getResources().getStringArray(R.array.calendar_short_weekdays);

        weekdayPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        datePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        highlightedDatePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        dateTodayTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleCalendarView, 0, 0);

        Typeface font = Typeface.SANS_SERIF;
        if(typedArray.hasValue(R.styleable.ScheduleCalendarView_android_fontFamily)) {
            final int fontId = typedArray.getResourceId(R.styleable.ScheduleCalendarView_android_fontFamily, -1);
            font = ResourcesCompat.getFont(context, fontId);
        }

        final float weekdaySize = typedArray.getDimension(R.styleable.ScheduleCalendarView_weekdayTextSize,
                18f * getResources().getDisplayMetrics().scaledDensity);
        final int weekdayColor = typedArray.getColor(R.styleable.ScheduleCalendarView_weekdayTextColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary));
        weekdayPaint.setColor(weekdayColor);
        weekdayPaint.setTypeface(font);
        weekdayPaint.setTextSize(weekdaySize);
        weekdayPaint.setTextAlign(Paint.Align.CENTER);

        final float dateSize = typedArray.getDimension(R.styleable.ScheduleCalendarView_dateTextSize,
                18f * getResources().getDisplayMetrics().scaledDensity);
        final int dateColor = typedArray.getColor(R.styleable.ScheduleCalendarView_dateTextColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary_dark));
        datePaint.setColor(dateColor);
        datePaint.setTypeface(Typeface.create(font, Typeface.BOLD));
        datePaint.setTextSize(dateSize);
        datePaint.setTextAlign(Paint.Align.CENTER);

        final int highlightedDateColor = typedArray.getColor(R.styleable.ScheduleCalendarView_dateHighlightedTextColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary));
        highlightedDatePaint.setColor(highlightedDateColor);
        highlightedDatePaint.setTypeface(datePaint.getTypeface());
        highlightedDatePaint.setTextSize(dateSize);
        highlightedDatePaint.setTextAlign(Paint.Align.CENTER);


        final int todayColor = typedArray.getColor(R.styleable.ScheduleCalendarView_dateTodayTextColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary_dark));
        dateTodayTextPaint.setColor(todayColor);
        dateTodayTextPaint.setTypeface(datePaint.getTypeface());
        dateTodayTextPaint.setTextSize(dateSize);
        dateTodayTextPaint.setTextAlign(Paint.Align.CENTER);

        final int indicatorColor = typedArray.getColor(R.styleable.ScheduleCalendarView_indicatorColor,
                ContextCompat.getColor(context, R.color.colorAccent));
        indicatorPaint.setColor(indicatorColor);

        indicatorRadius = Math.round(typedArray.getDimension(R.styleable.ScheduleCalendarView_indicatorRadius,
                Math.round(18f * getResources().getDisplayMetrics().density)));

        spaceBetweenDatesWeekdays = Math.round(typedArray.getDimension(R.styleable.ScheduleCalendarView_spaceBetweenDatesWeekdays,
                16f * getResources().getDisplayMetrics().scaledDensity));

        typedArray.recycle();

        dateTextBounds = new Rect();

        date = LocalDate.now();
        relativeIndicatorPosition = date.getDayOfWeek() - 1;
        datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            if(onDateSelectedListener != null) {
                LocalDate date = new LocalDate(year, month + 1, dayOfMonth);
                onDateSelectedListener.onDateSelected(date);
            }
        }, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

        datePaint.getTextBounds("31", 0, 2, dateTextBounds);

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                relativeIndicatorPosition += (float)dx / layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition()).getMeasuredWidth();

                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(position != -1) {
                    date = date.minusDays(date.getDayOfWeek() - 1).plusDays(position);
                    datePickerDialog.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
                    relativeIndicatorPosition = position;
                }

                invalidate();
            }
        };

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                float x = e.getX();
                if(x < getPaddingStart() || x > getPaddingStart() + getWidth()) {
                    return false;
                }

                float width = getWidth();
                float start = getPaddingStart();
                float end = getPaddingEnd();

                int weekday = Math.min(6, Math.max(0, Math.round(x / ((width - start - end) / 7f) - 1)));

                if(ScheduleCalendarView.this.onDateSelectedListener != null) {
                    ScheduleCalendarView.this.onDateSelectedListener.onDateSelected(
                            date.minusDays(date.getDayOfWeek() - 1).plusDays(weekday));
                }

                return true;
            }
        });

        if(isInEditMode()) {
            invalidate();
        }
    }

    public void setOnDateSelected(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public void setWeekday(int weekday) {
        if(weekday < 0 ||weekday > 6) {
            throw new IndexOutOfBoundsException();
        }
        setDay(date.minusDays(date.getDayOfWeek() - 1).plusDays(weekday));
        invalidate();
    }

    public void setDay(@NotNull LocalDate date) {
        this.date = date;
        relativeIndicatorPosition = date.getDayOfWeek() - 1;
        datePickerDialog.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        invalidate();
    }

    public void showDatePickerDialog() {
        datePickerDialog.show();
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        if(recyclerView != null) {
            detachFromRecyclerView();
        }
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public void detachFromRecyclerView() {
        if(recyclerView != null) {
            this.recyclerView.removeOnScrollListener(onScrollListener);
            this.recyclerView = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int canvasWidth = getWidth();

        final float contentWidth = getContentWidth();
        final float dateHeight = getDateHeight();
        final float weekdayHeight = getWeekdayHeight();
        final float spaceBetween = (canvasWidth - contentWidth) / 6f;
        final float weekdayTextY = getPaddingTop() + weekdayHeight;
        final float dateY = weekdayTextY + spaceBetweenDatesWeekdays + dateHeight;
        final float offsetX = spaceBetween + 2 * indicatorRadius;

        final int weekday = date.getDayOfWeek() - 1;

        float dayPositionX = getPaddingStart() + indicatorRadius;

        canvas.drawCircle(dayPositionX + offsetX * relativeIndicatorPosition,
                dateY + dateTextBounds.exactCenterY(), indicatorRadius, indicatorPaint);
        for(int i = 0; i < WEEKDAYS_COUNT; ++i) {
            LocalDate currentDate = date;
            if(weekday < i) {
                currentDate = date.plusDays(i - weekday);
            } else if(weekday > i) {
                currentDate = date.minusDays(weekday - i);
            }
            date.minusDays(date.getDayOfWeek());
            canvas.drawText(shortWeekdays[i], dayPositionX, weekdayTextY, weekdayPaint);
            canvas.drawText(String.valueOf(currentDate.getDayOfMonth()), dayPositionX, dateY,
                    Math.abs(relativeIndicatorPosition - i) < 0.5f
                            ? highlightedDatePaint : (currentDate.equals(LocalDate.now())
                            ? dateTodayTextPaint : datePaint));
            dayPositionX += offsetX;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private float getWeekWidth() {
        return 2 * indicatorRadius * 7;
    }

    private float getContentWidth() {
        return getPaddingStart() + getWeekWidth() + getPaddingEnd();
    }

    private float getWeekdayHeight() {
        final Paint.FontMetrics fontMetrics = weekdayPaint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    private float getDateHeight() {
        final Paint.FontMetrics fontMetrics = datePaint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    private float getDayHeight() {
        return getWeekdayHeight() + spaceBetweenDatesWeekdays + indicatorRadius * 2;
    }

    private float getContentHeight() {
        return getPaddingTop() + getDayHeight() + getPaddingBottom();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;
        if(widthMode == MeasureSpec.UNSPECIFIED) {
            width = Math.round(getContentWidth());
        } else if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(Math.round(getContentWidth()), widthSize);
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;
        if(heightMode == MeasureSpec.UNSPECIFIED) {
            height = Math.round(getContentHeight());
        } else if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.min(Math.round(getContentHeight()), heightSize);
        }

        setMeasuredDimension(width, height);
    }
}
