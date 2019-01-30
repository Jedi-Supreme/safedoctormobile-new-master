package com.safedoctor.safedoctor.Model.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Patientprofilemedicalhistory
{
    private Boolean answer ;
    private String createdtime;
    private int historytypeid;
    private int id;
    private long patientid;
    private int questionid;
    private String remarks;
    private List<Patientmedicalhistoryline> patientmedicalhistorylines = new ArrayList<Patientmedicalhistoryline>();

    public Boolean isAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public int getHistorytypeid() {
        return historytypeid;
    }

    public void setHistorytypeid(int historytypeid) {
        this.historytypeid = historytypeid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPatientid() {
        return patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Patientmedicalhistoryline> getPatientmedicalhistorylines() {
        return patientmedicalhistorylines;
    }

    public Patientmedicalhistoryline getPatientmedicalhistoryline(int answerid) {

        for(Patientmedicalhistoryline l : patientmedicalhistorylines)
        {
            if(l.getAnswerid() != null && l.getAnswerid().intValue() == answerid)
            {
                return l;
            }
        }
        return null;
    }

    public void setPatientmedicalhistorylines(List<Patientmedicalhistoryline> patientmedicalhistorylines) {
        this.patientmedicalhistorylines = patientmedicalhistorylines;
    }
}
