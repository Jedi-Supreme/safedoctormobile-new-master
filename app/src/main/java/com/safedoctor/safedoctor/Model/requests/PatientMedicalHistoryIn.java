package com.safedoctor.safedoctor.Model.requests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/22/2017.
 */

public class PatientMedicalHistoryIn
{
    private Integer id;
    private boolean answer;
    private String createdtime;
    private  int historytypeid;
    private int patientid;
    private int questionid;
    private String remarks;
    private List<ClinicalHistoryLineIn> historylines = new ArrayList<ClinicalHistoryLineIn>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
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

    public int getPatientid() {
        return patientid;
    }

    public void setPatientid(int patientid) {
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

    public List<ClinicalHistoryLineIn> getHistorylines() {
        return historylines;
    }

    public void setHistorylines(List<ClinicalHistoryLineIn> historylines) {
        this.historylines = historylines;
    }
}
