package com.safedoctor.safedoctor.Model.responses;

import com.safedoctor.safedoctor.Utils.AppConstants;

import java.io.Serializable;

/**
 * Created by Stevkkys on 9/21/2017.
 */

public interface BaseObject extends Serializable
{

    public String image = null;
    public int color = AppConstants.LIST_ROUND_COLOR;

    public Integer getId();
    public String getName();

}
