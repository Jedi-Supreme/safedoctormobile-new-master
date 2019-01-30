package com.safedoctor.safedoctor.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by beulahana on 21/02/2018.
 */

public class ConsultationProfile implements Serializable {
    private List<ClinicalNote> clinicalnotes;
    private List<ConsComplain> complains;
    private Consultation consultation;
    private List<Diagnoses> diagnoses;
    private List<Document> documents;
    private List<Examfinding> examsandfindings;
    private List<Prescriptiondrug> prescriptions;
    private List<Referral> referrals;
    private List<Servicerequest> servicerequests;
    private List<Systemreviews> systemsreviews;

    public List<ClinicalNote> getClinicalnotes() {
        return clinicalnotes;
    }

    public void setClinicalnotes(List<ClinicalNote> clinicalnotes) {
        this.clinicalnotes = clinicalnotes;
    }

    public List<ConsComplain> getComplains() {
        return complains;
    }

    public void setComplains(List<ConsComplain> complains) {
        this.complains = complains;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public List<Diagnoses> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnoses> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Examfinding> getExamsandfindings() {
        return examsandfindings;
    }

    public void setExamsandfindings(List<Examfinding> examsandfindings) {
        this.examsandfindings = examsandfindings;
    }

    public List<Prescriptiondrug> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescriptiondrug> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<Referral> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<Referral> referrals) {
        this.referrals = referrals;
    }

    public List<Servicerequest> getServicerequests() {
        return servicerequests;
    }

    public void setServicerequests(List<Servicerequest> servicerequests) {
        this.servicerequests = servicerequests;
    }

    public List<Systemreviews> getSystemreviews() {
        return systemsreviews;
    }

    public void setSystemreviews(List<Systemreviews> systemreviews) {
        this.systemsreviews = systemreviews;
    }
}
