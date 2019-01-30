package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Doctorlanguage implements Serializable {

    private BasicObject language;

    public BasicObject getLanguage() {
        return language;
    }

    public void setLanguage(BasicObject language) {
        this.language = language;
    }
}
