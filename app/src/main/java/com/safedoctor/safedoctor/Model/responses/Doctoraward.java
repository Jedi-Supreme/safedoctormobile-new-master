package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Doctoraward implements Serializable {

    private Integer id;

    private String award;

    private String createtime;

    private String createuserid;

    private String doctoruserid;

    private String organisation;

    private Integer year;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    public String getDoctoruserid() {
        return doctoruserid;
    }

    public void setDoctoruserid(String doctoruserid) {
        this.doctoruserid = doctoruserid;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
