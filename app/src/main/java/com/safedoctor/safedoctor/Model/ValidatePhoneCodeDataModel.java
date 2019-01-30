package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nab on 8/18/17.
 */

public class ValidatePhoneCodeDataModel {
    @SerializedName("valid")
    @Expose
    private boolean valid;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean data) {
        this.valid = data;
    }
}

