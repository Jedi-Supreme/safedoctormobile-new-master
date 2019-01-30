package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class MedicalhistorytypeQuestion implements Serializable
{
    private int id;
    private String history;
    
    private List<Medicalhistoryquestion> questions = new ArrayList<Medicalhistoryquestion>();
    private static final long serialVersionUID = 1L;

    public List<Medicalhistoryquestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Medicalhistoryquestion> questions) {
        this.questions = questions;
    }

    public void addQuestions(Medicalhistoryquestion questions) {
        this.questions.add(questions);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
