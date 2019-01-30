package com.safedoctor.safedoctor.Model;

/**
 * Created by kwabena on 8/17/17.
 */

public class StartRegistrationModel {
    private String phonenumber;

    public StartRegistrationModel(String phonenumber) {
        this.phonenumber = phonenumber;

    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
