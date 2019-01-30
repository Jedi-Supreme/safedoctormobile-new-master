package com.safedoctor.safedoctor.Model.responses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stevkkys on 9/26/2017.
 */

public class TimeSlot implements Parcelable
{
    private String date;
    private String doctorid;
    private String doctorname;
    private String starttime;
    private String endtime;
    private int roasterid;
    private int slotid;
    private int statusid;
    private int serviceid;
    private int specialityid;

    public String specialtytext;
    public String bookingnumber;

    public TimeSlot()
    {

    }

    protected TimeSlot(Parcel in) {
        date = in.readString();
        doctorid = in.readString();
        doctorname = in.readString();
        starttime = in.readString();
        endtime = in.readString();
        roasterid = in.readInt();
        slotid = in.readInt();
        statusid = in.readInt();
        serviceid = in.readInt();
        specialtytext = in.readString();
        bookingnumber = in.readString();
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    public int getSpecialityid() {
        return specialityid;
    }

    public void setSpecialityid(int specialityid) {
        this.specialityid = specialityid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getRoasterid() {
        return roasterid;
    }

    public void setRoasterid(int roasterid) {
        this.roasterid = roasterid;
    }

    public int getSlotid() {
        return slotid;
    }

    public void setSlotid(int slotid) {
        this.slotid = slotid;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public int getServiceid() {
        return serviceid;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(doctorid);
        dest.writeString(doctorname);
        dest.writeString(starttime);
        dest.writeString(endtime);
        dest.writeInt(roasterid);
        dest.writeInt(slotid);
        dest.writeInt(statusid);
        dest.writeInt(serviceid);
        dest.writeString(specialtytext);
        dest.writeString(bookingnumber);
    }
}
