package com.safedoctor.safedoctor.Model.responses;

import com.safedoctor.safedoctor.Utils.AppConstants;

import java.io.Serializable;

/**
 * Created by stevkky on 10/13/17.
 */

public class ProfileBasicObject implements Serializable
{
    public String image = null;
    public int color = AppConstants.LIST_ROUND_COLOR;

    private String otherid = null;
    private String othername = null;

    private Integer id = null;
    private String name;

    public String getOtherid() {
        return otherid;
    }

    public void setOtherid(String otherid) {
        this.otherid = otherid;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
