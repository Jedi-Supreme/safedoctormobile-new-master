package com.safedoctor.safedoctor.Model.responses;

/**
 * Created by beulahana on 09/01/2018.
 */

public class Reviewtype {

    private int id;
    private String name;
    private boolean isactive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
}
