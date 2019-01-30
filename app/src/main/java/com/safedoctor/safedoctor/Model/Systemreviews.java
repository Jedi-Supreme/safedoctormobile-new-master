package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.BasicObject;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Systemreviews implements Serializable {

    private BasicObject clinicalsystem;
    private String remarks;
    private BasicObject symptom;//rename consultation complain

    public BasicObject getClinicalsystem() {
        return clinicalsystem;
    }

    public void setClinicalsystem(BasicObject clinicalsystem) {
        this.clinicalsystem = clinicalsystem;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BasicObject getSymptom() {
        return symptom;
    }

    public void setSymptom(BasicObject symptom) {
        this.symptom = symptom;
    }
}
