package com.safedoctor.safedoctor.Model;

/**
 * Created by Kwabena Berko on 8/3/2017.
 */

public class Doctor{
    private String firstName;
    private String lastName;
    private String fullName;
    private String jobTitle;
    private String imageUrl;

    public Doctor(){

    }

    public Doctor(String fullName, String imageUrl)
    {
        this.fullName = fullName;
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
