package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kwabena on 8/22/17.
 */

public class NotificationModel   implements Serializable  {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("notificationid")
    @Expose
    private Integer notificationid;
    @SerializedName("bookingid")
    @Expose
    private Integer bookingid;
    @SerializedName("notificationdate")
    @Expose
    private String notificationdate;
    @SerializedName("deliverystatusid")
    @Expose
    private Integer deliverystatusid;
    @SerializedName("deliverysourceid")
    @Expose
    private Integer deliverysourceid;
    @SerializedName("notes")
    @Expose
    private Object notes;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("servertime")
    @Expose
    private String servertime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(Integer notificationid) {
        this.notificationid = notificationid;
    }

    public Integer getBookingid() {
        return bookingid;
    }

    public void setBookingid(Integer bookingid) {
        this.bookingid = bookingid;
    }

    public String getNotificationdate() {
        return notificationdate;
    }

    public void setNotificationdate(String notificationdate) {
        this.notificationdate = notificationdate;
    }

    public Integer getDeliverystatusid() {
        return deliverystatusid;
    }

    public void setDeliverystatusid(Integer deliverystatusid) {
        this.deliverystatusid = deliverystatusid;
    }

    public Integer getDeliverysourceid() {
        return deliverysourceid;
    }

    public void setDeliverysourceid(Integer deliverysourceid) {
        this.deliverysourceid = deliverysourceid;
    }

    public Object getNotes() {
        return notes;
    }

    public void setNotes(Object notes) {
        this.notes = notes;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }
}
