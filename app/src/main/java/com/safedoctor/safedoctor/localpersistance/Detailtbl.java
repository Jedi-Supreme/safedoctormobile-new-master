package com.safedoctor.safedoctor.localpersistance;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by beulahana on 17/01/2018.
 */

@Table(name="Detail")
public class Detailtbl extends Model {

    public Detailtbl() {
    }

    public Detailtbl(Integer bookingid, String createtime, String createuserid, String endtime, Integer detailId, String notes, Integer reasonid, String servertime, Integer servicefee, Integer serviceid, Integer serviceplaceid, Integer slotid, String starttime, String statusdate, Integer statusid, String updatetime, String updateuserid) {
        this.bookingid = bookingid;
        this.createtime = createtime;
        this.createuserid = createuserid;
        this.endtime = endtime;
        this.detailId = detailId;

        this.notes = notes;
        this.reasonid = reasonid;
        this.servertime = servertime;
        this.servicefee = servicefee;
        this.serviceid = serviceid;

        this.serviceplaceid = serviceplaceid;
        this.slotid = slotid;
        this.starttime = starttime;
        this.statusdate = statusdate;
        this.statusid = statusid;

        this.updatetime = updatetime;
        this.updateuserid = updateuserid;
    }

    @Column(name="bookingid")
    private Integer bookingid;
    @Column(name="createtime")
    private String createtime;
    @Column(name="createuserid")
    private String createuserid;
    @Column(name="endtime")
    private String endtime;
    @Column(name="detailId")
    private Integer detailId;
    @Column(name="notes")
    private String notes;
    @Column(name="reasonid")
    private Integer reasonid;
    @Column(name="servertime")
    private String servertime;
    @Column(name="servicefee")
    private Integer servicefee;
    @Column(name="serviceid")
    private Integer serviceid;
    @Column(name="serviceplaceid")
    private Integer serviceplaceid;
    @Column(name="slotid")
    private Integer slotid;
    @Column(name="starttime")
    private String starttime;
    @Column(name="statusdate")
    private String statusdate;
    @Column(name="statusid")
    private Integer statusid;
    @Column(name="updatetime")
    private String updatetime;
    @Column(name="updateuserid")
    private String updateuserid;

    public Integer getBookingid() {
        return bookingid;
    }

    public void setBookingid(Integer bookingid) {
        this.bookingid = bookingid;
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

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Integer getDetailIdId() {
        return detailId;
    }

    public void setId(Integer detailId) {
        this.detailId = detailId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getReasonid() {
        return reasonid;
    }

    public void setReasonid(Integer reasonid) {
        this.reasonid = reasonid;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public Integer getServicefee() {
        return servicefee;
    }

    public void setServicefee(Integer servicefee) {
        this.servicefee = servicefee;
    }

    public Integer getServiceid() {
        return serviceid;
    }

    public void setServiceid(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public Integer getServiceplaceid() {
        return serviceplaceid;
    }

    public void setServiceplaceid(Integer serviceplaceid) {
        this.serviceplaceid = serviceplaceid;
    }

    public Integer getSlotid() {
        return slotid;
    }

    public void setSlotid(Integer slotid) {
        this.slotid = slotid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
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

    public static List<Detailtbl> getSavedDetail() {
        return new Select().from(Detailtbl.class).orderBy("id DESC").execute();
    }
}
