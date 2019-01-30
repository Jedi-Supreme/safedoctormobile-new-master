package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kwabena on 8/22/17.
 */

public class AppointmentModel  implements Serializable{
    public AppointmentModel(String bookingdate, Integer bookingid, String bookingnumber, String doctorname, Integer consultationchattypeid, String createtime, String createuserid, String doctoruserid, Integer patientid, String servertime, Integer sourceid, String statusdate, Integer statusid, String updatetime, String updateuserid, String doctorphoto,Integer remind) {
        this.bookingdate = bookingdate;
        this.bookingid = bookingid;
        this.bookingnumber = bookingnumber;
        this.doctorname = doctorname;
        this.consultationchattypeid = consultationchattypeid;

        this.createtime = createtime;
        this.createuserid = createuserid;
        this.doctoruserid = doctoruserid;
        this.patientid = patientid;
        this.servertime = servertime;

        this.sourceid = sourceid;
        this.statusdate = statusdate;
        this.statusid = statusid;
        this.updatetime = updatetime;
        this.updateuserid = updateuserid;

        this.doctorphoto = doctorphoto;
        this.remind=remind;
    }

    public AppointmentModel() {
    }

    @SerializedName("bookingdate")
    @Expose
    private String bookingdate;
    @SerializedName("bookingid")
    @Expose
    private Integer bookingid;
    @SerializedName("bookingnumber")
    @Expose
    private String bookingnumber;
    @SerializedName("doctorname")
    @Expose
    private  String doctorname;
    @SerializedName("consultationchattypeid")
    @Expose
    private Integer consultationchattypeid;
    @SerializedName("createtime")
    @Expose
    private String createtime;
    @SerializedName("createuserid")
    @Expose
    private String createuserid;
    @SerializedName("doctoruserid")
    @Expose
    private String doctoruserid;
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("servertime")
    @Expose
    private String servertime;
    @SerializedName("sourceid")
    @Expose
    private Integer sourceid;
    @SerializedName("statusdate")
    @Expose
    private String statusdate;
    @SerializedName("statusid")
    @Expose
    private Integer statusid;
    @SerializedName("updatetime")
    @Expose
    private String updatetime;
    @SerializedName("updateuserid")
    @Expose
    private String updateuserid;

    private String doctorphoto;

    private Integer remind;

    public Integer getRemind() {
        return remind;
    }

    public void setRemind(Integer remind) {
        this.remind = remind;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

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

    public Integer getConsultationchattypeid() {
        return consultationchattypeid;
    }

    public void setConsultationchattypeid(Integer consultationchattypeid) {
        this.consultationchattypeid = consultationchattypeid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
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

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public Integer getSourceid() {
        return sourceid;
    }

    public void setSourceid(Integer sourceid) {
        this.sourceid = sourceid;
    }

    public String getStatusdate() {
        return statusdate;
    }

    public void setStatusdate(String statusdate) {
        this.statusdate = statusdate;
    }

    public Integer getStatusid() {
        return statusid;
    }

    public void setStatusid(Integer statusid) {
        this.statusid = statusid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public void setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
    }

    public String getDoctorphoto() {
        return doctorphoto;
    }

    public void setDoctorphoto(String doctorphoto) {
        this.doctorphoto = doctorphoto;
    }
}
