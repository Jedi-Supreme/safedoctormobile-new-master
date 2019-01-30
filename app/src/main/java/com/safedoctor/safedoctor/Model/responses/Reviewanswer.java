package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/15/17.
 */

public class Reviewanswer implements Serializable
{
    private Integer id;
    private String answer;
    private double rate;
    private String notes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
