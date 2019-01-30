package com.safedoctor.safedoctor.Model.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Medicalhistoryquestion
{
    private int id;
    private String question;
    private int questiontypeid;
    private int serialnumber;
    private int gendergroupid;
    private int agegroupid;
    private int dependantserialno;
    private List<Medicalhistoryquestionvalues> answervalues = new ArrayList<Medicalhistoryquestionvalues>();

    public List<Medicalhistoryquestionvalues> getAnswervalues() {
        return answervalues;
    }

    public void setAnswervalues(List<Medicalhistoryquestionvalues> answervalues) {
        this.answervalues = answervalues;
    }


    public int getGendergroupid() {
        return gendergroupid;
    }

    public void setGendergroupid(int gendergroupid) {
        this.gendergroupid = gendergroupid;
    }

    public int getAgegroupid() {
        return agegroupid;
    }

    public void setAgegroupid(int agegroupid) {
        this.agegroupid = agegroupid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestiontypeid() {
        return questiontypeid;
    }

    public void setQuestiontypeid(int questiontypeid) {
        this.questiontypeid = questiontypeid;
    }

    public int getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(int serialnumber) {
        this.serialnumber = serialnumber;
    }

    public int getDependantserialno() {
        return dependantserialno;
    }

    public void setDependantserialno(int dependantserialno) {
        this.dependantserialno = dependantserialno;
    }
}
