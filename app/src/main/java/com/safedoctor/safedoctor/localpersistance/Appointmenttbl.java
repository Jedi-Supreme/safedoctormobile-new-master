package com.safedoctor.safedoctor.localpersistance;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by beulahana on 17/01/2018.
 */

@Table(name="Appointment")
public class Appointmenttbl extends Model {

    public Appointmenttbl() {
    }

    public Appointmenttbl(String bookingdate,
                          Integer bookingid,
                          String bookingnumber,
                          String doctorname,
                          Integer consultationchattypeid,

                          String createtime,
                          String createuserid,
                          String doctoruserid,
                          Integer patientid,
                          String servertime,

                          Integer sourceid,
                          String statusdate,
                          Integer statusid,
                          String updatetime,
                          String updateuserid,
                          String doctorphoto,Integer remind) {
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

    @Column(name="bookingdate")
    private String bookingdate;

    @Column(name="bookingid")
    private Integer bookingid;

    @Column(name="bookingnumber")
    private String bookingnumber;

    @Column(name="doctorname")
    private  String doctorname;
    @Column(name="consultationchattypeid")
    private Integer consultationchattypeid;
    @Column(name="createtime")
    private String createtime;
    @Column(name="createuserid")
    private String createuserid;
    @Column(name="doctoruserid")
    private String doctoruserid;
    @Column(name="patientid")
    private Integer patientid;
    @Column(name="servertime")
    private String servertime;
    @Column(name="sourceid")
    private Integer sourceid;
    @Column(name="statusdate")
    private String statusdate;
    @Column(name="statusid")
    private Integer statusid;
    @Column(name="updatetime")
    private String updatetime;
    @Column(name="updateuserid")
    private String updateuserid;
    @Column(name="doctorphoto")
    private String doctorphoto;
    @Column(name="remind")
    private Integer remind;

    public Integer isRemind() {
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

    public static List<Appointmenttbl> getSavedAppointment() {
        return new Select().from(Appointmenttbl.class).orderBy("id DESC").execute();
    }

    public static void setReminder(String bookingnumber,int status){
        List<Appointmenttbl> t=new Select().from(Appointmenttbl.class).where("bookingnumber = ?", bookingnumber).execute();
        Log.e("setReminder",t.size()+" records");

        new Update(Appointmenttbl.class)
                .set("remind = ?",status)
                .where("bookingnumber = ?", bookingnumber)
                .execute();

        //List<Appointmenttbl> mt=new Select().from(Appointmenttbl.class).where("bookingnumber = ?", bookingnumber).execute();
        //mt.size();
    }
}
