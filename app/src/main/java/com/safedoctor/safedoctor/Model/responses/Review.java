package com.safedoctor.safedoctor.Model.responses;

import com.safedoctor.safedoctor.Model.PatientModel;

import java.io.Serializable;

/**
 * Created by stevkky on 10/14/17.
 */

public class Review implements Serializable
{
    public Review() {
    }

    public Review(Integer answerid, Integer questionid,
                  Integer reviewtypeid, Long patientid,
                  String remarks, String reviewdate,
                  String revieweeid) {
        this.answerid = answerid;
        this.questionid = questionid;
        this.reviewtypeid = reviewtypeid;
        this.patientid = patientid;
        this.remarks = remarks;
        this.reviewdate = reviewdate;
        this.revieweeid = revieweeid;
    }

    private Integer id;

    private Reviewanswer reviewanswer;



    private Integer answerid;
    private Integer questionid;
    private Integer reviewtypeid;
    private Long patientid;
    private String remarks;
    private String reviewdate;
    private String revieweeid;


    public Integer getAnswerid() {
        return answerid;
    }

    public void setAnswerid(Integer answerid) {
        this.answerid = answerid;
    }

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public Integer getReviewtypeid() {
        return reviewtypeid;
    }

    public void setReviewtypeid(Integer reviewtypeid) {
        this.reviewtypeid = reviewtypeid;
    }

    public String getRevieweeid() {
        return revieweeid;
    }

    public void setRevieweeid(String revieweeid) {
        this.revieweeid = revieweeid;
    }

    private Reviewquestion question;

    private BasicObject reviewtype;



    private PatientModel patient;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reviewanswer getReviewanswer() {
        return reviewanswer;
    }

    public void setReviewanswer(Reviewanswer reviewanswer) {
        this.reviewanswer = reviewanswer;
    }

    public Long getPatientid() {
        return patientid;
    }

    public void setPatientid(Long patientid) {
        this.patientid = patientid;
    }

    public Reviewquestion getQuestion() {
        return question;
    }

    public void setQuestion(Reviewquestion question) {
        this.question = question;
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate;
    }

    public BasicObject getReviewtype() {
        return reviewtype;
    }

    public void setReviewtype(BasicObject reviewtype) {
        this.reviewtype = reviewtype;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }
}
