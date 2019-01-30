package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.DrugOut;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;

import java.io.Serializable;

/**
 * Created by beulahana on 21/02/2018.
 */

public class Prescriptiondrug implements Serializable {

    private String dosage;
    private DrugOut drug;
    private BasicObject frequency;
    private Integer durationid;
    private Apptunit durationunit;
    private Integer durationvalue;
    private Facilityinfo provider;
    private String providerid;
    private String prescriptiontime;
    private String unitfee;
    private String prescribedqty;
    private String amount;
    private Long consultationid;
    private Boolean isPaid;

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }

    public Long getConsultationid() {
        return consultationid;
    }

    public void setConsultationid(Long consultationid) {
        this.consultationid = consultationid;
    }

    private String doctor;

    public String getDoctor() {
        return doctor;
    }

    public String getQtygiven() {
        return prescribedqty;
    }

    public void setQtygiven(String prescribedqty) {
        this.prescribedqty = prescribedqty;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public Facilityinfo getProvider() {
        return provider;
    }

    public void setProvider(Facilityinfo provider) {
        this.provider = provider;
    }

    public String getPrescriptiontime() {
        return prescriptiontime;
    }

    public void setPrescriptiontime(String prescriptiontime) {
        this.prescriptiontime = prescriptiontime;
    }

    public String getUnitfee() {
        return unitfee;
    }

    public void setUnitfee(String unitfee) {
        this.unitfee = unitfee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getDurationid() {
        return durationid;
    }

    public void setDurationid(Integer durationid) {
        this.durationid = durationid;
    }

    public Apptunit getDurationunit() {
        return durationunit;
    }

    public void setDurationunit(Apptunit durationunit) {
        this.durationunit = durationunit;
    }

    public Integer getDurationvalue() {
        return durationvalue;
    }

    public void setDurationvalue(Integer durationvalue) {
        this.durationvalue = durationvalue;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public DrugOut getDrug() {
        return drug;
    }

    public void setDrug(DrugOut drug) {
        this.drug = drug;
    }


    public BasicObject getFrequency() {
        return frequency;
    }

    public void setFrequency(BasicObject frequency) {
        this.frequency = frequency;
    }
}
