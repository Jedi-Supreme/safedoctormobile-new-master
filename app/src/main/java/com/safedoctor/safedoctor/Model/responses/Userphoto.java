package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Userphoto implements Serializable {

    private String userid;
    private String uploadedtime;
    private String photo;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUploadedtime() {
        return uploadedtime;
    }

    public void setUploadedtime(String uploadedtime) {
        this.uploadedtime = uploadedtime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
