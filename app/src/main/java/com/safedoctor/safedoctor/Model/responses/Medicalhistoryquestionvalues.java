package com.safedoctor.safedoctor.Model.responses;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Medicalhistoryquestionvalues
{
    private String description;
    private Integer id;
    private int questionid;
    private int valueorderno;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getValueorderno() {
        return valueorderno;
    }

    public void setValueorderno(int valueorderno) {
        this.valueorderno = valueorderno;
    }
}
