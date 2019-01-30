package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Doctorskill implements Serializable {

    private Integer id;
    private String doctoruserid;
    private Integer rating;
    private BasicObject skill;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctoruserid() {
        return doctoruserid;
    }

    public void setDoctoruserid(String doctoruserid) {
        this.doctoruserid = doctoruserid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public BasicObject getSkill() {
        return skill;
    }

    public void setSkill(BasicObject skill) {
        this.skill = skill;
    }
}
