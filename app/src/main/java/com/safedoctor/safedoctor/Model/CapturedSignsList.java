package com.safedoctor.safedoctor.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 08/12/2018.
 */

public class CapturedSignsList implements Serializable {

    private List<Vitalsignscapture> list = new ArrayList<>();

    public List<Vitalsignscapture> getList() {
        return list;
    }

    public void setList(List<Vitalsignscapture> list) {
        this.list = list;
    }
}
