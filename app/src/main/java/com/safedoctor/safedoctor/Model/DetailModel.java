package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kwabena on 8/22/17.
 */

public class DetailModel  implements Serializable {
    public DetailModel() {
    }

    public DetailModel(Integer bookingid, String createtime, String createuserid, String endtime, Integer id, String notes, Integer reasonid, String servertime, Integer servicefee, Integer serviceid, Integer serviceplaceid, Integer slotid, String starttime, String statusdate, Integer statusid, String updatetime, String updateuserid) {
        this.bookingid = bookingid;
        this.createtime = createtime;
        this.createuserid = createuserid;
        this.endtime = endtime;
        this.id = id;

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

    @SerializedName("bookingid")
    @Expose
    private Integer bookingid;
    @SerializedName("createtime")
    @Expose
    private String createtime;
    @SerializedName("createuserid")
    @Expose
    private String createuserid;
    @SerializedName("endtime")
    @Expose
    private String endtime;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("reasonid")
    @Expose
    private Integer reasonid;
    @SerializedName("servertime")
    @Expose
    private String servertime;
    @SerializedName("servicefee")
    @Expose
    private Integer servicefee;
    @SerializedName("serviceid")
    @Expose
    private Integer serviceid;
    @SerializedName("serviceplaceid")
    @Expose
    private Integer serviceplaceid;
    @SerializedName("slotid")
    @Expose
    private Integer slotid;
    @SerializedName("starttime")
    @Expose
    private String starttime;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
