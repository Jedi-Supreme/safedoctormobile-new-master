package com.safedoctor.safedoctor.bluetooth;

import java.io.Serializable;

/**
 * Created by stevkky on 07/06/2018.
 */

public class BloodPressureData implements Serializable
{
    private Integer lowperssure;
    private Integer highpressure;
    private Integer mpaulse;
    private Integer maverge;

    private  String date;

    public Integer getLowperssure() {
        return lowperssure;
    }

    public void setLowperssure(Integer lowperssure) {
        this.lowperssure = lowperssure;
    }

    public Integer getHighpressure() {
        return highpressure;
    }

    public void setHighpressure(Integer highpressure) {
        this.highpressure = highpressure;
    }

    public Integer getMpaulse() {
        return mpaulse;
    }

    public void setMpaulse(Integer mpaulse) {
        this.mpaulse = mpaulse;
    }

    public Integer getMaverge() {
        return maverge;
    }

    public void setMaverge(Integer maverge) {
        this.maverge = maverge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
