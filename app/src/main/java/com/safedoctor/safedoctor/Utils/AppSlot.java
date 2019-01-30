package com.safedoctor.safedoctor.Utils;

import android.os.Parcel;
import android.os.Parcelable;


import com.stevkky.customcalendarview.EventDay;

import java.util.Calendar;

/**
 * Created by Stevkkys on 9/24/2017.
 */

public class AppSlot extends EventDay implements Parcelable
{
    private String mNote;


    public AppSlot(Calendar day, int color, String note) {
        super(day, color);
        mNote = note;
    }

    public String getNote() {
        return mNote;
    }

    private AppSlot(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        mNote = in.readString();
    }

    public static final Creator<AppSlot> CREATOR = new Creator<AppSlot>() {
        @Override
        public AppSlot createFromParcel(Parcel in) {
            return new AppSlot(in);
        }

        @Override
        public AppSlot[] newArray(int size) {
            return new AppSlot[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getCalendar());
        parcel.writeInt(getColor());
        parcel.writeString(mNote);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
