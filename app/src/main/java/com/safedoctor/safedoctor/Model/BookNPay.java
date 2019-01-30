package com.safedoctor.safedoctor.Model;

/**
 * Created by kwabena on 8/21/17.
 */

public class BookNPay {
    private Integer consultationchattypeid;
    private String doctorid;
    private String mobilemoneynetworkid;
    private Integer patientid;
    private Integer reasonid = 1;
    private Integer serviceid;
    private Integer slotid;
    private String walletnumber;
    private String vodafonetoken = "";

    public Integer getConsultationchattypeid() {
        return consultationchattypeid;
    }

    public void setConsultationchattypeid(Integer consultationchattypeid) {
        this.consultationchattypeid = consultationchattypeid;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getMobilemoneynetworkid() {
        return mobilemoneynetworkid;
    }

    public void setMobilemoneynetworkid(String mobilemoneynetworkid) {
        this.mobilemoneynetworkid = mobilemoneynetworkid;
    }

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public Integer getReasonid() {
        return reasonid;
    }

    public void setReasonid(Integer reasonid) {
        this.reasonid = reasonid;
    }

    public Integer getServiceid() {
        return serviceid;
    }

    public void setServiceid(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public Integer getSlotid() {
        return slotid;
    }

    public void setSlotid(Integer slotid) {
        this.slotid = slotid;
    }

    public String getWalletnumber() {
        return walletnumber;
    }

    public void setWalletnumber(String walletnumber) {
        this.walletnumber = walletnumber;
    }

    public String getVodafonetoken() {
        return vodafonetoken;
    }

    public void setVodafonetoken(String vodafonetoken) {
        this.vodafonetoken = vodafonetoken;
    }
}
