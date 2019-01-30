package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/12/17.
 */

public class ReviewBlock implements Serializable {

    private Integer typeid;

    private String name;

    private double percentage = 0.0d;

    private long totalnumber = 0l;

    private double average = 0.0d;

    private List<Review> details = new ArrayList<Review>();

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public long getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(long totalnumber) {
        this.totalnumber = totalnumber;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public List<Review> getDetails() {
        return details;
    }

    public void setDetails(List<Review> details) {
        this.details = details;
    }
}
