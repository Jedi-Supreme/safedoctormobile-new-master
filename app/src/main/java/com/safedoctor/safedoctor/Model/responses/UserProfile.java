package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/12/17.
 */

public class UserProfile implements Serializable {

    private UserAccount user;
    private Userphoto photo;
    private ReviewStatistics reviewstatistics;
    private List<Doctoraccomplishment> accomplishments = new ArrayList<Doctoraccomplishment>();
    private List<Doctoraward> awards = new ArrayList<Doctoraward>();
    private List<Doctorcertificate> certificates = new ArrayList<Doctorcertificate>();
    private List<Doctorfacility> facilities = new ArrayList<Doctorfacility>();
    private List<Doctorlanguage> languages = new ArrayList<Doctorlanguage>();
    private List<Doctorskill> skills = new ArrayList<Doctorskill>();
    private List<Doctorspecialty> specialties = new ArrayList<Doctorspecialty>();

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Userphoto getPhoto() {
        return photo;
    }

    public void setPhoto(Userphoto photo) {
        this.photo = photo;
    }

    public ReviewStatistics getReviewstatistics() {
        return reviewstatistics;
    }

    public void setReviewstatistics(ReviewStatistics reviewstatistics) {
        this.reviewstatistics = reviewstatistics;
    }

    public List<Doctoraccomplishment> getAccomplishments() {
        return accomplishments;
    }

    public void setAccomplishments(List<Doctoraccomplishment> accomplishments) {
        this.accomplishments = accomplishments;
    }

    public List<Doctoraward> getAwards() {
        return awards;
    }

    public void setAwards(List<Doctoraward> awards) {
        this.awards = awards;
    }

    public List<Doctorcertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Doctorcertificate> certificates) {
        this.certificates = certificates;
    }

    public List<Doctorfacility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Doctorfacility> facilities) {
        this.facilities = facilities;
    }

    public List<Doctorlanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Doctorlanguage> languages) {
        this.languages = languages;
    }

    public List<Doctorskill> getSkills() {
        return skills;
    }

    public void setSkills(List<Doctorskill> skills) {
        this.skills = skills;
    }

    public List<Doctorspecialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Doctorspecialty> specialties) {
        this.specialties = specialties;
    }
}
