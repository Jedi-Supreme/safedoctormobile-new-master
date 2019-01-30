package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/12/17.
 */

public class ReviewStatistics implements Serializable {

    private String revieweeid;

    private String name;

    private String reviewtype;

    private ReviewBlock summary;

    private List<ReviewBlock> details = new ArrayList<ReviewBlock>();

    public String getRevieweeid() {
        return revieweeid;
    }

    public void setRevieweeid(String revieweeid) {
        this.revieweeid = revieweeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviewtype() {
        return reviewtype;
    }

    public void setReviewtype(String reviewtype) {
        this.reviewtype = reviewtype;
    }

    public ReviewBlock getSummary() {
        return summary;
    }

    public void setSummary(ReviewBlock summary) {
        this.summary = summary;
    }

    public List<ReviewBlock> getDetails() {
        return details;
    }

    public void setDetails(List<ReviewBlock> details) {
        this.details = details;
    }
}
