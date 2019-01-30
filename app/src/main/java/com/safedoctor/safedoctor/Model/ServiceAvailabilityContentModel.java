package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alvin on 18/08/2017.
 */

public class ServiceAvailabilityContentModel {

    @SerializedName("starttime")
    @Expose
    private String starttime;

    @SerializedName("endtime")
    @Expose
    private String endtime;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("slotid")
    @Expose
    private Integer slotid;

    @SerializedName("roasterid")
    @Expose
    private Integer roasterid;

    @SerializedName("doctorid")
    @Expose
    private String doctorid;

    @SerializedName("doctorname")
    @Expose
    private String doctorname;

//    public ServiceAvailabilityContentModel(String starttime, String endtime, String date, String doctorname) {
//        this.starttime = starttime;
//        this.endtime = endtime;
//        this.date = date;
//        this.doctorname = doctorname;
//    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public Integer getSlotid() {
        return slotid;
    }

    public void setSlotid(Integer slotid) {
        this.slotid = slotid;
    }

    public Integer getRoasterid() {
        return roasterid;
    }

    public void setRoasterid(Integer roasterid) {
        this.roasterid = roasterid;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }
}
