package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwabena on 8/17/17.
 */

public class PatientPhotoModel {
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("uploadedtime")
    @Expose
    private String uploadedtime;

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUploadedtime() {
        return uploadedtime;
    }

    public void setUploadedtime(String uploadedtime) {
        this.uploadedtime = uploadedtime;
    }
}
