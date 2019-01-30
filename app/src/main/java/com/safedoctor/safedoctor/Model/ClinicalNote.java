package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.UserAccount;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class ClinicalNote implements Serializable {
    private String clinicalnotes;
    private UserAccount doctor;

    public String getClinicalnotes() {
        return clinicalnotes;
    }

    public void setClinicalnotes(String clinicalnotes) {
        this.clinicalnotes = clinicalnotes;
    }

    public UserAccount getDoctor() {
        return doctor;
    }

    public void setDoctor(UserAccount doctor) {
        this.doctor = doctor;
    }
}
