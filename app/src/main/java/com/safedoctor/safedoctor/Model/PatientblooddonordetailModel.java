package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Utils.AppConstants;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class PatientblooddonordetailModel
{
    private String certificateimage;
    private String donornumber;
    private int id;
    private long patientid;
    private String uploadeddate;


    public PatientblooddonordetailModel(String certificateimage, String donornumber, int id)
    {
        this.certificateimage = certificateimage;
        this.donornumber = donornumber;
        this.patientid = AppConstants.patientID;
        this.id = id;
    }

    public PatientblooddonordetailModel()
    {

    }
    public String getCertificateimage() {
        return certificateimage;
    }

    public void setCertificateimage(String certificateimage) {
        this.certificateimage = certificateimage;
    }

    public String getDonornumber() {
        return donornumber;
    }

    public void setDonornumber(String donornumber) {
        this.donornumber = donornumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPatientid() {
        return patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    public String getUploadeddate() {
        return uploadeddate;
    }

    public void setUploadeddate(String uploadeddate) {
        this.uploadeddate = uploadeddate;
    }
}
