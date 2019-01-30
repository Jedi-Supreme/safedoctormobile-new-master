package com.safedoctor.safedoctor.Model;

/**
 * Created by beulahana on 15/01/2018.
 */

public class PatientChangeCredentials {
    public PatientChangeCredentials(String newpassword, String oldpassword) {
        this.newpassword = newpassword;
        this.oldpassword = oldpassword;
    }

    private String newpassword;
    private String oldpassword;

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }
}
