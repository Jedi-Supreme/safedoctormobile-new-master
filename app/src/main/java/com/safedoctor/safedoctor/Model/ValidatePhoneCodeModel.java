package com.safedoctor.safedoctor.Model;

/**
 * Created by nab on 8/18/17.
 */

public class ValidatePhoneCodeModel {
    private String code;
    private String mobilephone;

    public ValidatePhoneCodeModel (String code, String mobilephone) {
        this.code = code;
        this.mobilephone = mobilephone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }
}
