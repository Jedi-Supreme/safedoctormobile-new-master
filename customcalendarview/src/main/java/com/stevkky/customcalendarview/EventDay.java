package com.stevkky.customcalendarview;

import com.stevkky.customcalendarview.utils.DateUtils;

import java.util.Calendar;

/**
 * This class represents an event of a day. An instance of this class is returned when user click
 * a day cell. This class can be overridden to make calendar more functional. A list of instances of
 * this class can be passed to CalendarView object using setEvents() method.
 * <p>
 * Created by Stephen Adjei-Kyei.
 */

public class EventDay {
    private Calendar mDay;
    private int mColor;
    private Integer eventid = 0 ;
    private Integer eventcount = 0 ;

    /**
     * @param day Calendar object which represents a date of the event
     */
    public EventDay(Calendar day) {
        mDay = day;
    }

    /**
     * @param day           Calendar object which represents a date of the event
     * @param color Resource of an image which will be displayed in a day cell
     */
    public EventDay(Calendar day, int color) {
        DateUtils.setMidnight(day);
        mDay = day;
        mColor = color;
    }


    /**
     * @return An color resource which will be displayed in the day row
     */
    public int getColor() {
        return mColor;
    }

    /**
     * @return Calendar object which represents a date of current event
     */
    public Calendar getCalendar() {
        return mDay;
    }


    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    public Integer getEventcount() {
        return eventcount;
    }

    public void setEventcount(Integer eventcount) {
        this.eventcount = eventcount;
    }
}
