package com.safedoctor.safedoctor.Model.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/20/2017.
 */

public class PatientProfileFormDataOut
{
    private List<BasicObject> bloodgroup = new ArrayList<BasicObject>();
    private List<BasicObject> title = new ArrayList<BasicObject>();
    private List<BasicObject> maritalstatus = new ArrayList<BasicObject>();
    private List<Town> town = new ArrayList<Town>();
    private List<District> district = new ArrayList<District>();
    private List<BasicObject> region = new ArrayList<BasicObject>();
    private List<BasicObject> identificationtype = new ArrayList<BasicObject>();
    private List<BasicObject> country = new ArrayList<BasicObject>();
    private List<BasicObject> ethnicity = new ArrayList<BasicObject>();
    private List<BasicObject> occupation = new ArrayList<BasicObject>();
    private List<BasicObject> educationlevel = new ArrayList<BasicObject>();
    private List<BasicObject> religion = new ArrayList<BasicObject>();
    private List<Denomination> denomination = new ArrayList<Denomination>();
    private List<BasicObject> relation = new ArrayList<BasicObject>();
    private List<DrugOut> drugs = new ArrayList<DrugOut>();
    private List<MedicalhistorytypeQuestion> medicalhistorytypequestion = new ArrayList<MedicalhistorytypeQuestion>();
    private List<Patientprofilemedicalhistory> patientprofilemedicalhistory = new ArrayList<Patientprofilemedicalhistory>();


    public List<District> getDistrict() {
        return district;
    }

    public List<District> getDistrict(int regionid) {

        List<District> lst = new ArrayList<District>();
        for(District d : district)
        {
            if(d.getRegionid() == regionid)
            {
                lst.add(d);
            }
        }

        return lst;
    }

    public void setDistrict(List<District> district) {
        this.district = district;
    }

    public List<BasicObject> getRegion() {
        return region;
    }

    public void setRegion(List<BasicObject> region) {
        this.region = region;
    }

    public List<BasicObject> getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(List<BasicObject> bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public List<BasicObject> getTitle() {
        return title;
    }

    public void setTitle(List<BasicObject> title) {
        this.title = title;
    }

    public List<BasicObject> getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(List<BasicObject> maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public List<Town> getTown() {
        return town;
    }

    public List<Town> getTown(int districtid) {

        List<Town> lst = new ArrayList<Town>();
        for(Town t : town)
        {
            if(t.getDistrictid() == districtid)
            {
                lst.add(t);
            }
        }

        return lst;
    }

    public void setTown(List<Town> town) {
        this.town = town;
    }

    public List<BasicObject> getIdentificationtype() {
        return identificationtype;
    }

    public void setIdentificationtype(List<BasicObject> identificationtype) {
        this.identificationtype = identificationtype;
    }

    public List<BasicObject> getCountry() {
        return country;
    }

    public void setCountry(List<BasicObject> country) {
        this.country = country;
    }

    public List<BasicObject> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(List<BasicObject> ethnicity) {
        this.ethnicity = ethnicity;
    }

    public List<BasicObject> getOccupation() {
        return occupation;
    }

    public void setOccupation(List<BasicObject> occupation) {
        this.occupation = occupation;
    }

    public List<BasicObject> getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(List<BasicObject> educationlevel) {
        this.educationlevel = educationlevel;
    }

    public List<BasicObject> getReligion() {
        return religion;
    }

    public void setReligion(List<BasicObject> religion) {
        this.religion = religion;
    }

    public List<Denomination> getDenomination() {
        return denomination;
    }

    public List<Denomination> getDenomination(int religionid)
    {
        List<Denomination> lst = new ArrayList<Denomination>();

        for(Denomination d: denomination)
        {
            if(d.getReligionid() == religionid)
            {
                lst.add(d);
            }
        }
        return lst;
    }

    public void setDenomination(List<Denomination> denomination) {
        this.denomination = denomination;
    }

    public List<BasicObject> getRelation() {
        return relation;
    }

    public void setRelation(List<BasicObject> relation) {
        this.relation = relation;
    }

    public List<DrugOut> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<DrugOut> drugs) {
        this.drugs = drugs;
    }

    public List<MedicalhistorytypeQuestion> getMedicalhistorytypequestion() {
        return medicalhistorytypequestion;
    }

    public List<Medicalhistoryquestion> getMedicalhistorytypequestion(int typeid)
    {
        for(MedicalhistorytypeQuestion q : medicalhistorytypequestion)
        {
            if( q.getId() == typeid)
            {
                return q.getQuestions();
            }
        }

        return new ArrayList<Medicalhistoryquestion>();
    }

    public void setMedicalhistorytypequestion(List<MedicalhistorytypeQuestion> medicalhistorytypequestion) {
        this.medicalhistorytypequestion = medicalhistorytypequestion;
    }

    public List<Patientprofilemedicalhistory> getPatientprofilemedicalhistory() {
        return patientprofilemedicalhistory;
    }

    public Patientprofilemedicalhistory getPatientprofilemedicalhistory(int questionid)
    {
        for(Patientprofilemedicalhistory h : patientprofilemedicalhistory)
        {
            if(h.getQuestionid() == questionid)
            {
                return h;
            }
        }
        return null;
    }

    public void setPatientprofilemedicalhistory(int questionid, Patientprofilemedicalhistory newhistory)
    {
        for(int i=0;i<patientprofilemedicalhistory.size();i++)
        {
            if(patientprofilemedicalhistory.get(i).getQuestionid() == questionid)
            {
                patientprofilemedicalhistory.set(i,newhistory);
                return;
            }
        }

        addPatientprofilemedicalhistory(newhistory);
    }

    public void setPatientprofilemedicalhistory(List<Patientprofilemedicalhistory> patientprofilemedicalhistory) {
        this.patientprofilemedicalhistory = patientprofilemedicalhistory;
    }

    public void addPatientprofilemedicalhistory(Patientprofilemedicalhistory patientprofilemedicalhistory) {
        if(patientprofilemedicalhistory != null) {
            this.patientprofilemedicalhistory.add(patientprofilemedicalhistory);
        }
    }


}
