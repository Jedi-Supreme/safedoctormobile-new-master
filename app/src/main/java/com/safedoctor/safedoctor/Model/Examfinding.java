package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.BasicObject;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Examfinding implements Serializable {

    private String examfindings;
    private String remarks;
    private BasicObject system;

    public String getExamfindings() {
        return examfindings;
    }

    public void setExamfindings(String examfindings) {
        this.examfindings = examfindings;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BasicObject getSystem() {
        return system;
    }

    public void setSystem(BasicObject system) {
        this.system = system;
    }
}
