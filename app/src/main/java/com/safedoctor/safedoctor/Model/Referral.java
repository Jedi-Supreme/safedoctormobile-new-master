package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.Facilityinfo;
import com.safedoctor.safedoctor.Model.responses.UserAccount;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Referral implements Serializable{
    private Integer consultationid;
    private Integer id;

    private String createtime;
    private String createuserid;
    private String doctorid;
    private String documentmimetype;
    private String referraldoctorname;
    private String referraldocument;
    private String referralpurpose;
    private String refferedproviderid;
    private String remarks;
    private String updatetime;
    private String updateuserid;

    private UserAccount doctor;
    private Facilityinfo refferedprovider;


    public UserAccount getDoctor() {
        return doctor;
    }

    public void setDoctor(UserAccount doctor) {
        this.doctor = doctor;
    }

    public Facilityinfo getRefferedprovider() {
        return refferedprovider;
    }

    public void setRefferedprovider(Facilityinfo refferedprovider) {
        this.refferedprovider = refferedprovider;
    }

    public Integer getConsultationid() {
        return consultationid;
    }

    public void setConsultationid(Integer consultationid) {
        this.consultationid = consultationid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getDocumentmimetype() {
        return documentmimetype;
    }

    public void setDocumentmimetype(String documentmimetype) {
        this.documentmimetype = documentmimetype;
    }

    public String getReferraldoctorname() {
        return referraldoctorname;
    }

    public void setReferraldoctorname(String referraldoctorname) {
        this.referraldoctorname = referraldoctorname;
    }

    public String getReferraldocument() {
        return referraldocument;
    }

    public void setReferraldocument(String referraldocument) {
        this.referraldocument = referraldocument;
    }

    public String getReferralpurpose() {
        return referralpurpose;
    }

    public void setReferralpurpose(String referralpurpose) {
        this.referralpurpose = referralpurpose;
    }

    public String getRefferedproviderid() {
        return refferedproviderid;
    }

    public void setRefferedproviderid(String refferedproviderid) {
        this.refferedproviderid = refferedproviderid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
