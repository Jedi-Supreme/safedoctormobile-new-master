package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by Stevkkys on 9/20/2017.
 */

public class DrugOut implements Serializable
{
    private String drugid;
    private String name;

    public String getDrugid()
    {
        return drugid;
    }

    public DrugOut()
    {

    }

    public DrugOut(String drugid, String name)
    {
        this.drugid = drugid;
        this.name = name;
    }

    public void setDrugid( String drugid )
    {
        this.drugid = drugid;
    }
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
}
