package com.safedoctor.safedoctor.Model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stevkky on 10/14/17.
 */

public class Reviewquestion implements Serializable
{
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("question")
    @Expose
    private String question;

    private List<Reviewquestion> questions;

    public List<Reviewquestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Reviewquestion> questions) {
        this.questions = questions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
