package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwabena on 8/17/17.
 */

public class LoginDataModel {
    @SerializedName("accessToken")
    @Expose
    private TokenModel accessToken;
    @SerializedName("patient")
    @Expose
    private PatientModel patient;

    public TokenModel getTokenModel() {
        return accessToken;
    }

    public void setTokenModel(TokenModel accessToken) {
        this.accessToken = accessToken;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }
}
