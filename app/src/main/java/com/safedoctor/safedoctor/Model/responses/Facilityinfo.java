package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class Facilityinfo implements Serializable {

    private String id;

    private BasicObject country;

    private String dateestablished;

    private String email;

    private Integer facilitylevelid;

    private BasicObject facilitylevel;

    private Integer facilitytypeid;

    private BasicObject facilitytype;

    private String healthfacilitycode;

    private String latitude;

    private String location;

    private String logo;

    private String longitude;

    private String motto;

    private String name;
    private BasicObject ownershiptype;

    private String postaladdress;

    private String telno;
    private Town town;

    private String website;
    private Integer regionid;
    private Integer districtid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BasicObject getCountry() {
        return country;
    }

    public void setCountry(BasicObject country) {
        this.country = country;
    }

    public String getDateestablished() {
        return dateestablished;
    }

    public void setDateestablished(String dateestablished) {
        this.dateestablished = dateestablished;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFacilitylevelid() {
        return facilitylevelid;
    }

    public void setFacilitylevelid(Integer facilitylevelid) {
        this.facilitylevelid = facilitylevelid;
    }

    public BasicObject getFacilitylevel() {
        return facilitylevel;
    }

    public void setFacilitylevel(BasicObject facilitylevel) {
        this.facilitylevel = facilitylevel;
    }

    public Integer getFacilitytypeid() {
        return facilitytypeid;
    }

    public void setFacilitytypeid(Integer facilitytypeid) {
        this.facilitytypeid = facilitytypeid;
    }

    public BasicObject getFacilitytype() {
        return facilitytype;
    }

    public void setFacilitytype(BasicObject facilitytype) {
        this.facilitytype = facilitytype;
    }

    public String getHealthfacilitycode() {
        return healthfacilitycode;
    }

    public void setHealthfacilitycode(String healthfacilitycode) {
        this.healthfacilitycode = healthfacilitycode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicObject getOwnershiptype() {
        return ownershiptype;
    }

    public void setOwnershiptype(BasicObject ownershiptype) {
        this.ownershiptype = ownershiptype;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getRegionid() {
        return regionid;
    }

    public void setRegionid(Integer regionid) {
        this.regionid = regionid;
    }

    public Integer getDistrictid() {
        return districtid;
    }

    public void setDistrictid(Integer districtid) {
        this.districtid = districtid;
    }
}
