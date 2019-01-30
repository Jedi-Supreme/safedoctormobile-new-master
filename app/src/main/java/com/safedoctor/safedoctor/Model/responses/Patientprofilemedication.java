package com.safedoctor.safedoctor.Model.responses;

import com.safedoctor.safedoctor.Utils.AppConstants;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Patientprofilemedication
{
    public int color = AppConstants.LIST_ROUND_COLOR;

    private Integer id = null;
    private long patientid;
    private String remarks;
    private String drugid;
    private String createdtime;
    private Drugs drug;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getPatientid() {
        return patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDrugid() {
        return drugid;
    }

    public void setDrugid(String drugid) {
        this.drugid = drugid;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public Drugs getDrug() {
        return drug;
    }

    public void setDrug(Drugs drug) {
        this.drug = drug;
    }
}
