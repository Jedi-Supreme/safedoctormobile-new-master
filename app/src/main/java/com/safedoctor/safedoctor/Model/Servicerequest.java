package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Servicerequest implements Serializable {

    private BasicObject specialty;
    private String remark;
    private BasicObject service;
    private Facilityinfo serviceprovider;

    public BasicObject getSpecialty() {
        return specialty;
    }

    public void setSpecialty(BasicObject specialty) {
        this.specialty = specialty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BasicObject getService() {
        return service;
    }

    public void setService(BasicObject service) {
        this.service = service;
    }

    public Facilityinfo getServiceprovider() {
        return serviceprovider;
    }

    public void setServiceprovider(Facilityinfo serviceprovider) {
        this.serviceprovider = serviceprovider;
    }
}
