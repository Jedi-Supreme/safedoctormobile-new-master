package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.UserAccount;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class ConsComplain implements Serializable {
    private String complaint;
    private String remarks;
    private UserAccount doctor;

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public UserAccount getDoctor() {
        return doctor;
    }

    public void setDoctor(UserAccount doctor) {
        this.doctor = doctor;
    }
}
