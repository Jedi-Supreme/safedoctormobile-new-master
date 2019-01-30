package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alvin on 21/08/2017.
 */

public class ChatSessionDataModel {

    @SerializedName("bookingid")
    @Expose
    private Integer bookingid;
    @SerializedName("bookingnumber")
    @Expose
    private String bookingnumber;
    @SerializedName("doctoruserid")
    @Expose
    private String doctoruserid;
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("sessionid")
    @Expose
    private String sessionid;
    @SerializedName("token")
    @Expose
    private String token;

    public Integer getBookingid() {
        return bookingid;
    }

    public void setBookingid(Integer bookingid) {
        this.bookingid = bookingid;
    }

    public String getBookingnumber() {
        return bookingnumber;
    }

    public void setBookingnumber(String bookingnumber) {
        this.bookingnumber = bookingnumber;
    }

    public String getDoctoruserid() {
        return doctoruserid;
    }

    public void setDoctoruserid(String doctoruserid) {
        this.doctoruserid = doctoruserid;
    }

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
