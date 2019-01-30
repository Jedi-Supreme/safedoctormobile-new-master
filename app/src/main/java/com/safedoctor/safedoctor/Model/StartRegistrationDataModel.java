package com.safedoctor.safedoctor.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartRegistrationDataModel {
    @SerializedName("patientid")
    @Expose
    private Integer patientid;

    @SerializedName("isvalidated")
    @Expose
    private Boolean isvalidated;
    @SerializedName("validationcode")
    @Expose
    private String validationcode;
    @SerializedName("validateddate")
    @Expose
    private String validateddate;
    @SerializedName("generatedtime")
    @Expose
    private String generatedtime;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public Boolean getIsvalidated() {
        return isvalidated;
    }

    public void setIsvalidated(Boolean isvalidated) {
        this.isvalidated = isvalidated;
    }

    public String getValidationcode() {
        return validationcode;
    }

    public void setValidationcode(String validationcode) {
        this.validationcode = validationcode;
    }

    public String getValidateddate() {
        return validateddate;
    }

    public void setValidateddate(String validateddate) {
        this.validateddate = validateddate;
    }

    public String getGeneratedtime() {
        return generatedtime;
    }

    public void setGeneratedtime(String generatedtime) {
        this.generatedtime = generatedtime;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
