package com.safedoctor.safedoctor.Model.requests;

/**
 * Created by Stevkkys on 9/22/2017.
 */

public class ClinicalHistoryLineIn
{
    private String answer;
    private Integer answerid;

    public ClinicalHistoryLineIn()
    {

    }

    public ClinicalHistoryLineIn(String answer, Integer answerid)
    {
        this.answer = answer;
        this.answerid = answerid;
    }

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
}
