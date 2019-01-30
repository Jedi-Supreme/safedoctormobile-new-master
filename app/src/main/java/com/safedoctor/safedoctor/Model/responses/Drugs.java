package com.safedoctor.safedoctor.Model.responses;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Drugs
{
    private String drugid;
    private String name;
    private String picture;

    public Drugs()
    {

    }

    public Drugs(String drugid, String name)
    {
        this.drugid = drugid;
        this.name = name;
    }
    public String getDrugid() {
        return drugid;
    }

    public void setDrugid(String drugid) {
        this.drugid = drugid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
