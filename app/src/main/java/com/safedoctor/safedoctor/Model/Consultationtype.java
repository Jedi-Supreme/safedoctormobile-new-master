package com.safedoctor.safedoctor.Model;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Consultationtype implements Serializable{

    private String name;
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
