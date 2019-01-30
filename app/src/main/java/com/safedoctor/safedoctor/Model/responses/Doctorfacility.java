package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Doctorfacility implements Serializable {

    private Facilityinfo facility;

    public Facilityinfo getFacility() {
        return facility;
    }

    public void setFacility(Facilityinfo facility) {
        this.facility = facility;
    }
}
