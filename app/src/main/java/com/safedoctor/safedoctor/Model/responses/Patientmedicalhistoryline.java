package com.safedoctor.safedoctor.Model.responses;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Patientmedicalhistoryline
{
    private String answer;
    private Integer answerid;
    private int patientmedicalhistoryid;
    private int id;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getAnswerid() {
        return answerid;
    }

    public void setAnswerid(Integer answerid) {
        this.answerid = answerid;
    }

    public int getPatientmedicalhistoryid() {
        return patientmedicalhistoryid;
    }

    public void setPatientmedicalhistoryid(int patientmedicalhistoryid) {
        this.patientmedicalhistoryid = patientmedicalhistoryid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
