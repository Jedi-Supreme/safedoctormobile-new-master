package com.stevkky.customcalendarview.listeners;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.stevkky.customcalendarview.EventDay;
import com.stevkky.customcalendarview.R;
import com.stevkky.customcalendarview.adapters.CalendarPageAdapter;
import com.stevkky.customcalendarview.utils.DateUtils;
import com.stevkky.customcalendarview.utils.DayColorsUtils;
import com.stevkky.customcalendarview.utils.SelectedDay;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class is responsible for handle click events
 * <p>
 * Created by Stephen Adjei-Kyei.
 */

public class DayRowClickListener implements AdapterView.OnItemClickListener {
    private CalendarPageAdapter mCalendarPageAdapter;
    private Context mContext;
    private List<EventDay> mEventDays;
    private OnDayClickListener mOnDayClickListener;
    private boolean mIsDatePicker;
    private int mTodayLabelColor;
    private int mSelectionColor;
    private boolean mallowPreviousDates;


    public DayRowClickListener(CalendarPageAdapter calendarPageAdapter, Context context,
                               List<EventDay> eventDays, OnDayClickListener onDayClickListener,
                               boolean isDatePicker, int todayLabelColor, int selectionColor, boolean allowPreviousDates) {
        mCalendarPageAdapter = calendarPageAdapter;
        mContext = context;
        mEventDays = eventDays;
        mOnDayClickListener = onDayClickListener;
        mIsDatePicker = isDatePicker;
        mTodayLabelColor = todayLabelColor;
        mSelectionColor = selectionColor;
        mallowPreviousDates = allowPreviousDates;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Calendar day = new GregorianCalendar();
        day.setTime((Date) adapterView.getItemAtPosition(position));

        //I will not allow any on click event trigger if the data is before current date
        //whene mallowPreviousDates is false
        if(!mallowPreviousDates)
        {
            Calendar cal = Calendar.getInstance();
            DateUtils.setMidnight(cal);
            if(day.getTime().before(cal.getTime())){
                return;
            }
        }

        // If calendar is in picker mode than day is selected
        if (mIsDatePicker) {
            selectDay(view, day);
        }

        //Call onClick listener
        if (mOnDayClickListener != null) {
            mCalendarPageAdapter.setSelectedDate(day);

            onClick(day);
        }
    }

    private void selectDay(View view, Calendar day) {
        // Getting previous selected day
        SelectedDay selectedDay = mCalendarPageAdapter.getSelectedDay();

        if (selectedDay != null && !day.equals(selectedDay.getCalendar())) {
            TextView dayLabel = (TextView) view.findViewById(R.id.dayLabel);

            // Checking if current month day is selecting
            if (dayLabel.getCurrentTextColor() !=
                    ContextCompat.getColor(mContext, R.color.nextMonthDayColor)) {

                mCalendarPageAdapter.setSelectedDate(day);

                // Coloring selected day
                DayColorsUtils.setSelectedDayColors(mContext, dayLabel, mSelectionColor);

                TextView previousDayLabel =
                        (TextView) selectedDay.getView().findViewById(R.id.dayLabel);

                // Coloring previous selected day
                DayColorsUtils.setCurrentMonthDayColors(mContext, selectedDay.getCalendar(),
                        DateUtils.getCalendar(), previousDayLabel, mTodayLabelColor);

                mCalendarPageAdapter.setSelectedDay(new SelectedDay(dayLabel, day));
            }
        }
    }

    private void onClick(Calendar day) {
        if (mEventDays == null) {
            mOnDayClickListener.onDayClick(new EventDay(day));
            return;
        }

        Stream.of(mEventDays).filter(eventDate ->
                eventDate.getCalendar().equals(day)).findFirst().ifPresentOrElse(
                calendarEventDay -> mOnDayClickListener.onDayClick(calendarEventDay),
                () -> mOnDayClickListener.onDayClick(new EventDay(day)));
    }
}
