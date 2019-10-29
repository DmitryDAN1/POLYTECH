package com.danapps.polytech.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.danapps.polytech.R;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class ScheduleCalendarView extends View {

    private final String weekdays[];

    private Paint       backgroundPaint;
    private TextPaint   weekdayPaint;
    private TextPaint   datePaint;
    private TextPaint   highlightedDatePaint;
    private Paint       indicatorPaint;

    private float indicatorRadius;
    private float minSpaceBetweenDates;
    private float minSpaceBetweenWeekdayAndDate;

    private float contentWidth;
    private float contentHeight;

    private float selectedPosition;

    public ScheduleCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        weekdays = DateFormatSymbols.getInstance(Locale.getDefault()).getShortWeekdays();

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        weekdayPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        datePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        highlightedDatePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleCalendarView, 0, 0);

        Typeface font = Typeface.SANS_SERIF;
        if(typedArray.hasValue(R.styleable.ScheduleCalendarView_android_fontFamily)) {
            final int fontId = typedArray.getResourceId(R.styleable.ScheduleCalendarView_android_fontFamily, -1);
            font = ResourcesCompat.getFont(context, fontId);
        }

        final float textSize = Math.round(typedArray.getDimensionPixelSize(R.styleable.ScheduleCalendarView_android_textSize,
                Math.round(20f * getResources().getDisplayMetrics().scaledDensity)));

        final int backgroundColor = typedArray.getColor(R.styleable.ScheduleCalendarView_backgroundColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary));
        backgroundPaint.setColor(backgroundColor);

        final int weekdayColor = typedArray.getColor(R.styleable.ScheduleCalendarView_weekdayColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary_dark));
        weekdayPaint.setColor(weekdayColor);
        weekdayPaint.setTypeface(font);
        weekdayPaint.setTextSize(textSize);

        final int dateColor = typedArray.getColor(R.styleable.ScheduleCalendarView_dateColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary_dark));
        datePaint.setColor(dateColor);
        datePaint.setTypeface(font);
        datePaint.setTextSize(textSize);

        final int highlightedDateColor = typedArray.getColor(R.styleable.ScheduleCalendarView_highlightedDateColor,
                ContextCompat.getColor(context, R.color.design_default_color_primary));
        highlightedDatePaint.setColor(highlightedDateColor);
        highlightedDatePaint.setTypeface(font);
        highlightedDatePaint.setTextSize(textSize);

        final int indicatorColor = typedArray.getColor(R.styleable.ScheduleCalendarView_indicatorColor,
                ContextCompat.getColor(context, R.color.colorAccent));
        indicatorPaint.setColor(indicatorColor);

        typedArray.recycle();

        indicatorRadius = Math.round(typedArray.getDimension(R.styleable.ScheduleCalendarView_indicatorRadius,
                Math.round(18f * getResources().getDisplayMetrics().density)));

        minSpaceBetweenDates = Math.round(typedArray.getDimension(R.styleable.ScheduleCalendarView_minSpaceBetweenDates,
                Math.round(16f * getResources().getDisplayMetrics().density)));
        minSpaceBetweenWeekdayAndDate = Math.round(typedArray.getDimension(R.styleable.ScheduleCalendarView_minSpaceBetweenWeekdayAndDate,
                Math.round(5f * getResources().getDisplayMetrics().density)));

        float weekdaysWidth = 0;
        for(String weekday : weekdays) {
            weekdaysWidth += weekdayPaint.measureText(weekday);
        }
        contentWidth = Math.max(weekdaysWidth,
                Math.round(indicatorRadius * 2 * 7 + minSpaceBetweenDates * 6) + getPaddingStart() + getPaddingEnd());
        contentHeight = Math.round(indicatorRadius * 2 + minSpaceBetweenWeekdayAndDate
                + weekdayPaint.getFontMetrics().descent - weekdayPaint.getFontMetrics().ascent)
                + getPaddingTop() + getPaddingBottom();

        if(isInEditMode()) {
            scrollToPosition(2);
            invalidate();
        }
    }

    public void scrollToPosition(int weekday) {
        final float spaceBetween = getWidth();
        selectedPosition = spaceBetween * 5;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int canvasWidth = getWidth();
        final int canvasHeight = getHeight();

        final float spaceBetween =
                (canvasWidth - indicatorRadius * 2f * 7f - getPaddingStart() - getPaddingEnd()) / 6f;

        float currentX = getPaddingStart() + indicatorRadius;

        final float dateTextHeight = datePaint.getFontMetrics().descent - datePaint.getFontMetrics().ascent;

        final float weekdayTextY = getPaddingTop() + weekdayPaint.getFontMetrics().descent
                - weekdayPaint.getFontMetrics().ascent;
        final float dateY = weekdayTextY + minSpaceBetweenWeekdayAndDate + dateTextHeight;

        canvas.drawCircle(selectedPosition, dateY - dateTextHeight * 0.5f, indicatorRadius, indicatorPaint);
        for(int i = 0; i < 7; ++i) {
            canvas.drawText("Пн", currentX, weekdayTextY, weekdayPaint);
            canvas.drawText("21", currentX, dateY, datePaint);
            currentX += spaceBetween + indicatorRadius;
        }
    }

    private int getContentWidth() {
        return Math.round(indicatorRadius * 2 * 7 + minSpaceBetweenDates * 6) + getPaddingStart() + getPaddingEnd();
    }

    private int getContentHeight() {
        return Math.round(indicatorRadius * 2 + minSpaceBetweenWeekdayAndDate
                + weekdayPaint.getFontMetrics().descent - weekdayPaint.getFontMetrics().ascent)
                + getPaddingTop() + getPaddingBottom();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;
        if(widthMode == MeasureSpec.UNSPECIFIED) {
            width = getContentWidth();
        } else if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(getContentWidth(), widthSize);
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;
        if(heightMode == MeasureSpec.UNSPECIFIED) {
            height = getContentHeight();
        } else if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.min(getContentHeight(), heightSize);
        }

        setMeasuredDimension(width, height);
    }
}
