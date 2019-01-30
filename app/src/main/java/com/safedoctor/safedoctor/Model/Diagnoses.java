package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.BasicObject;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Diagnoses implements Serializable {

    private BasicObject disease;
    private BasicObject diagtype;

    public BasicObject getDisease() {
        return disease;
    }

    public void setDisease(BasicObject disease) {
        this.disease = disease;
    }

    public BasicObject getDiagtype() {
        return diagtype;
    }

    public void setDiagtype(BasicObject diagtype) {
        this.diagtype = diagtype;
    }
}
