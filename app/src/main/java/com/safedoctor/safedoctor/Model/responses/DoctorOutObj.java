package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class DoctorOutObj  implements Serializable
{
    private Userphoto picture;
    private UserAccount doctor;
    private Integer slotcount;

    public Integer getSlotcount() {
        return slotcount;
    }

    public void setSlotcount(Integer slotcount) {
        this.slotcount = slotcount;
    }

    public Userphoto getPicture() {
        return picture;
    }

    public void setPicture(Userphoto picture) {
        this.picture = picture;
    }

    public UserAccount getDoctor() {
        return doctor;
    }

    public void setDoctor(UserAccount doctor) {
        this.doctor = doctor;
    }
}
