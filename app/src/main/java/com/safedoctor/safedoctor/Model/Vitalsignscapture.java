package com.safedoctor.safedoctor.Model;

import java.io.Serializable;

/**
 * Created by stevkky on 08/12/2018.
 */

public class Vitalsignscapture implements Serializable
{
    private Integer id;
    private String actualperipheral;
    private String capturedate;
    private Integer consultationid;
    private String createtime;
    private String createuserid;
    private Integer patientid;
    private String peripheralid;
    private String result;
    private Integer vitaltypeid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActualperipheral() {
        return actualperipheral;
    }

    public void setActualperipheral(String actualperipheral) {
        this.actualperipheral = actualperipheral;
    }

    public String getCapturedate() {
        return capturedate;
    }

    public void setCapturedate(String capturedate) {
        this.capturedate = capturedate;
    }

    public Integer getConsultationid() {
        return consultationid;
    }

    public void setConsultationid(Integer consultationid) {
        this.consultationid = consultationid;
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

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public String getPeripheralid() {
        return peripheralid;
    }

    public void setPeripheralid(String peripheralid) {
        this.peripheralid = peripheralid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getVitaltypeid() {
        return vitaltypeid;
    }

    public void setVitaltypeid(Integer vitaltypeid) {
        this.vitaltypeid = vitaltypeid;
    }
}
