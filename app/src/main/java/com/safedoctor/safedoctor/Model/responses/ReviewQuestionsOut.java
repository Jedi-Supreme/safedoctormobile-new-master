package com.safedoctor.safedoctor.Model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by beulahana on 10/01/2018.
 */

public class ReviewQuestionsOut<T> {

    @SerializedName("questions")
    @Expose
    private T questions;

    public T getQuestions() {
        return questions;
    }

    public void setQuestions(T questions) {
        this.questions = questions;
    }

    //private Reviewquestiontype questiontype;
}
