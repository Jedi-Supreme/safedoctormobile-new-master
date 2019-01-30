package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Doctorspecialty implements Serializable {

    private BasicObject specialty;

    public BasicObject getSpecialty() {
        return specialty;
    }

    public void setSpecialty(BasicObject specialty) {
        this.specialty = specialty;
    }
}
