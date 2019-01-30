package com.safedoctor.safedoctor.Model.responses;

import com.safedoctor.safedoctor.Utils.AppConstants;

/**
 * Created by Stevkkys on 9/22/2017.
 */

public class OtherBasicObject
{
    public String image = null;

    public Integer slotcount;
    public int color = AppConstants.LIST_ROUND_COLOR;

    private String id;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OtherBasicObject()
    {
    }

    public OtherBasicObject(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
