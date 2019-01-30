package com.safedoctor.safedoctor.Model;

import java.io.Serializable;

public class Apptunit implements Serializable {

    private Integer id;
    private String unit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
