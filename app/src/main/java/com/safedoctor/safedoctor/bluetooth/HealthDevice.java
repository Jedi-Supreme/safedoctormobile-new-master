package com.safedoctor.safedoctor.bluetooth;

import java.io.Serializable;

/**
 * Created by stevkky on 07/05/2018.
 */

public class HealthDevice implements Serializable
{
    private String prefix;
    private  String displayname;
    private int imageresource;
    private String vitalname;

    public HealthDevice()
    {

    }

    public HealthDevice(String prefix, String displayname, int imageresource, String vitalname)
    {
        this.prefix = prefix;
        this.displayname = displayname;
        this.imageresource = imageresource;
        this.vitalname = vitalname;

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public int getImageresource() {
        return imageresource;
    }

    public void setImageresource(int imageresource) {
        this.imageresource = imageresource;
    }

    public String getVitalname() {
        return vitalname;
    }

    public void setVitalname(String vitalname) {
        this.vitalname = vitalname;
    }
}
