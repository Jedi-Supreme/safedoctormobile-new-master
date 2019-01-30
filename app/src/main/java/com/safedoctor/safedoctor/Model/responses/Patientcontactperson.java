package com.safedoctor.safedoctor.Model.responses;

import android.graphics.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.safedoctor.safedoctor.Utils.AppConstants;

/**
 * Created by Stevkkys on 9/17/2017.
 */

public class Patientcontactperson
{
    public int color = AppConstants.LIST_ROUND_COLOR;
    public Integer image = null;
    /////Real data from endpoint

    @SerializedName("id")
    @Expose
    private Integer id = null;
    @SerializedName("contactnumbers")
    @Expose
    private String contactnumbers;
    @SerializedName("emails")
    @Expose
    private String emails;
    @SerializedName("homeaddress")
    @Expose
    private String homeaddress;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("patientid")
    @Expose
    private long patientid;
    @SerializedName("relationshipid")
    @Expose
    private Integer relationshipid;
    @SerializedName("workaddress")
    @Expose
    private String workaddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactnumbers() {
        return contactnumbers;
    }

    public void setContactnumbers(String contactnumbers) {
        this.contactnumbers = contactnumbers;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPatientid() {
        return patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    public Integer getRelationshipid() {
        return relationshipid;
    }

    public void setRelationshipid(Integer relationshipid) {
        this.relationshipid = relationshipid;
    }

    public String getWorkaddress() {
        return workaddress;
    }

    public void setWorkaddress(String workaddress) {
        this.workaddress = workaddress;
    }
}
