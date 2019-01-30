package com.safedoctor.safedoctor.Model.responses;

/**
 * Created by Stevkkys on 9/6/2017.
 */

public class PhoneValidationRequest
{
    private String generatedtime;
    private boolean isvalidated;
    private long patientid;
    private String phonenumber;
    private String validateddate;
    private String validationcode;

    public String getGeneratedtime() {
        return generatedtime;
    }

    public void setGeneratedtime(String generatedtime) {
        this.generatedtime = generatedtime;
    }

    public boolean isvalidated() {
        return isvalidated;
    }

    public void setIsvalidated(boolean isvalidated) {
        this.isvalidated = isvalidated;
    }

    public long getPatientid() {
        return patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getValidateddate() {
        return validateddate;
    }

    public void setValidateddate(String validateddate) {
        this.validateddate = validateddate;
    }

    public String getValidationcode() {
        return validationcode;
    }

    public void setValidationcode(String validationcode) {
        this.validationcode = validationcode;
    }
}
