package com.stevkky.customcalendarview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.stevkky.customcalendarview.EventDay;
import com.stevkky.customcalendarview.R;
import com.stevkky.customcalendarview.utils.DateUtils;
import com.stevkky.customcalendarview.utils.DayColorsUtils;
import com.stevkky.customcalendarview.utils.ImageUtils;
import com.stevkky.customcalendarview.utils.SelectedDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class is responsible for loading a one day cell.
 * <p>
 * Created by Stephen Adjei-Kyei.
 */

class CalendarDayAdapter extends ArrayAdapter<Date> {
    private CalendarPageAdapter mCalendarPageAdapter;
    private Context mContext;
    private List<EventDay> mEventDays;
    private LayoutInflater mLayoutInflater;
    private int mItemLayoutResource;
    private int mMonth;
    private Calendar mToday = DateUtils.getCalendar();
    private boolean mIsDatePicker;
    private int mTodayLabelColor;
    private int mSelectionColor;
    private boolean mAllowPreviousDates;

    CalendarDayAdapter(CalendarPageAdapter calendarPageAdapter, Context context, int itemLayoutResource,
                       ArrayList<Date> dates, List<EventDay> eventDays, int month, boolean isDatePicker,
                       int todayLabelColor, int selectionColor, boolean mallowPreviousDates) {
        super(context, itemLayoutResource, dates);

        mCalendarPageAdapter = calendarPageAdapter;
        mContext = context;
        mEventDays = eventDays;
        mMonth = month < 0 ? 11 : month;
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayoutResource = itemLayoutResource;
        mIsDatePicker = isDatePicker;
        mTodayLabelColor = todayLabelColor;
        mSelectionColor = selectionColor;
        mAllowPreviousDates = mallowPreviousDates;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = mLayoutInflater.inflate(mItemLayoutResource, parent, false);
        }

        TextView dayLabel = (TextView) view.findViewById(R.id.dayLabel);
        ImageView dayIcon = (ImageView) view.findViewById(R.id.dayIcon);
        TextView badge_textView = (TextView) view.findViewById(R.id.badge_textView);

        Calendar day = new GregorianCalendar();
        day.setTime(getItem(position));

        // Loading an image of the event
        if (dayIcon != null) {
            loadIcon(dayIcon, day);
        }

        setEventCount(badge_textView,day);

        if (mIsDatePicker && day.equals(mCalendarPageAdapter.getSelectedDate())
                && day.get(Calendar.MONTH) == mMonth)
        {

            // Setting selected day color
            mCalendarPageAdapter.setSelectedDay(new SelectedDay(dayLabel, day));
            DayColorsUtils.setSelectedDayColors(mContext, dayLabel, mSelectionColor);

        }
        else
        {
            if (day.get(Calendar.MONTH) == mMonth)
            { // Setting current month day color

                if(!mAllowPreviousDates)
                {
                    Calendar cal = Calendar.getInstance();
                    DateUtils.setMidnight(cal);
                    if(day.getTime().before(cal.getTime()))
                    {
                        DayColorsUtils.setDayColors(dayLabel, ContextCompat.getColor(mContext,
                                R.color.nextMonthDayColor), Typeface.NORMAL, R.drawable.background_transparent);
                    }
                    else
                    {
                        DayColorsUtils.setCurrentMonthDayColors(mContext, day, mToday, dayLabel, mTodayLabelColor);
                    }
                }
                else
                {
                    DayColorsUtils.setCurrentMonthDayColors(mContext, day, mToday, dayLabel, mTodayLabelColor);
                }
            }
            else
            { // Setting not current month day color
                DayColorsUtils.setDayColors(dayLabel, ContextCompat.getColor(mContext,
                        R.color.nextMonthDayColor), Typeface.NORMAL, R.drawable.background_transparent);
            }
        }

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
        return view;
    }

    private void loadIcon(ImageView dayIcon, Calendar day) {
        if (mEventDays == null || mIsDatePicker) {
            dayIcon.setVisibility(View.GONE);
            return;
        }

        Stream.of(mEventDays).filter(eventDate ->
                eventDate.getCalendar().equals(day)).findFirst().executeIfPresent(eventDay -> {

            //TODO will use the color in the event to color the shape under the date
            //ImageUtils.loadResource(dayIcon, eventDay.getImageResource());

            // If a day doesn't belong to current month then image is transparent
            if (day.get(Calendar.MONTH) != mMonth) {
                dayIcon.setAlpha(0.2f);
            }

        });
    }

    private void setEventCount(TextView view, Calendar day)
    {
        if (mEventDays == null )
        {
            view.setText("");
            view.setBackgroundColor(Color.TRANSPARENT);
            return;
        }

        Stream.of(mEventDays).filter(eventDate ->
                eventDate.getCalendar().equals(day)).findFirst().ifPresentOrElse(eventDay -> {

            view.setText(String.valueOf(eventDay.getEventcount()));

            // If a day doesn't belong to current month then image is transparent
            if (day.get(Calendar.MONTH) != mMonth) {
                view.setAlpha(0.2f);
            }

        },()->{ view.setText(""); view.setBackgroundColor(Color.TRANSPARENT); });

    }
}
