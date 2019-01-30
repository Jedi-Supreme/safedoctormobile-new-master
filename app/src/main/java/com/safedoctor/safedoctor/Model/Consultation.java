package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.UserAccount;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Consultation implements Serializable {
    private UserAccount doctor;
    private String doctorremarks;
    private Consultationtype consultationtype;
    private Integer consultationtypeid;

    private String conendtime;
    private String contime;


    public Integer getConsultationtypeid() {
        return consultationtypeid;
    }

    public void setConsultationtypeid(Integer consultationtypeid) {
        this.consultationtypeid = consultationtypeid;
    }

    public Consultationtype getConsultationtype() {
        return consultationtype;
    }

    public void setConsultationtype(Consultationtype consultationtype) {
        this.consultationtype = consultationtype;
    }

    public UserAccount getDoctor() {
        return doctor;
    }

    public void setDoctor(UserAccount doctor) {
        this.doctor = doctor;
    }

    public String getDoctorremarks() {
        return doctorremarks;
    }

    public void setDoctorremarks(String doctorremarks) {
        this.doctorremarks = doctorremarks;
    }

    /*
    public BaseObject getConsultationtype() {
        return consultationtype;
    }

    public void setConsultationtype(BaseObject consultationtype) {
        this.consultationtype = consultationtype;
    }*/

    public String getConendtime() {
        return conendtime;
    }

    public void setConendtime(String conendtime) {
        this.conendtime = conendtime;
    }

    public String getContime() {
        return contime;
    }

    public void setContime(String contime) {
        this.contime = contime;
    }
}
