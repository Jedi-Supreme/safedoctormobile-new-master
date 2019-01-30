package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwabena on 8/17/17.
 */

public class RegistrationDataModel {

    @SerializedName("accountnumber")
    @Expose
    private String accountnumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("bloodgroupid")
    @Expose
    private Integer bloodgroupid;
    @SerializedName("cellphoneno")
    @Expose
    private String cellphoneno;
    @SerializedName("createtime")
    @Expose
    private String createtime;
    @SerializedName("createuserid")
    @Expose
    private String createuserid;
    @SerializedName("dateofbirth")
    @Expose
    private String dateofbirth;
    @SerializedName("denominationid")
    @Expose
    private Integer denominationid;
    @SerializedName("districtid")
    @Expose
    private Integer districtid;
    @SerializedName("educationallevelid")
    @Expose
    private Integer educationallevelid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("ethnicityid")
    @Expose
    private Integer ethnicityid;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("gendergroupid")
    @Expose
    private Integer gendergroupid;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("homedistrictid")
    @Expose
    private Integer homedistrictid;
    @SerializedName("homeregionid")
    @Expose
    private Integer homeregionid;
    @SerializedName("homestreet")
    @Expose
    private String homestreet;
    @SerializedName("hometelno")
    @Expose
    private String hometelno;
    @SerializedName("hometownid")
    @Expose
    private Integer hometownid;
    @SerializedName("identificationtypeid")
    @Expose
    private Integer identificationtypeid;
    @SerializedName("idnumber")
    @Expose
    private String idnumber;
    @SerializedName("isactive")
    @Expose
    private Boolean isactive;
    @SerializedName("islocked")
    @Expose
    private Boolean islocked;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("locationlatitude")
    @Expose
    private String locationlatitude;
    @SerializedName("locationlongitude")
    @Expose
    private String locationlongitude;
    @SerializedName("maritalstatusid")
    @Expose
    private Integer maritalstatusid;
    @SerializedName("middlename")
    @Expose
    private String middlename;
    @SerializedName("nationalityid")
    @Expose
    private Integer nationalityid;
    @SerializedName("occupationid")
    @Expose
    private Integer occupationid;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("patientcategorytypeid")
    @Expose
    private Integer patientcategorytypeid;
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("patientstatusid")
    @Expose
    private Integer patientstatusid;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("regionid")
    @Expose
    private Integer regionid;
    @SerializedName("registrationstatus")
    @Expose
    private String registrationstatus;
    @SerializedName("registrationtime")
    @Expose
    private String registrationtime;
    @SerializedName("religionid")
    @Expose
    private Integer religionid;
    @SerializedName("servertime")
    @Expose
    private String servertime;
    @SerializedName("smsalert")
    @Expose
    private Boolean smsalert;
    @SerializedName("streetaddress")
    @Expose
    private String streetaddress;
    @SerializedName("titleid")
    @Expose
    private Integer titleid;
    @SerializedName("townid")
    @Expose
    private Integer townid;
    @SerializedName("updatetime")
    @Expose
    private String updatetime;
    @SerializedName("updateuserid")
    @Expose
    private String updateuserid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("workdistrictid")
    @Expose
    private Integer workdistrictid;
    @SerializedName("workregionid")
    @Expose
    private Integer workregionid;
    @SerializedName("workstreet")
    @Expose
    private String workstreet;
    @SerializedName("worktelno")
    @Expose
    private String worktelno;
    @SerializedName("worktownid")
    @Expose
    private Integer worktownid;

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBloodgroupid() {
        return bloodgroupid;
    }

    public void setBloodgroupid(Integer bloodgroupid) {
        this.bloodgroupid = bloodgroupid;
    }

    public String getCellphoneno() {
        return cellphoneno;
    }

    public void setCellphoneno(String cellphoneno) {
        this.cellphoneno = cellphoneno;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public Integer getDenominationid() {
        return denominationid;
    }

    public void setDenominationid(Integer denominationid) {
        this.denominationid = denominationid;
    }

    public Integer getDistrictid() {
        return districtid;
    }

    public void setDistrictid(Integer districtid) {
        this.districtid = districtid;
    }

    public Integer getEducationallevelid() {
        return educationallevelid;
    }

    public void setEducationallevelid(Integer educationallevelid) {
        this.educationallevelid = educationallevelid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEthnicityid() {
        return ethnicityid;
    }

    public void setEthnicityid(Integer ethnicityid) {
        this.ethnicityid = ethnicityid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getGendergroupid() {
        return gendergroupid;
    }

    public void setGendergroupid(Integer gendergroupid) {
        this.gendergroupid = gendergroupid;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHomedistrictid() {
        return homedistrictid;
    }

    public void setHomedistrictid(Integer homedistrictid) {
        this.homedistrictid = homedistrictid;
    }

    public Integer getHomeregionid() {
        return homeregionid;
    }

    public void setHomeregionid(Integer homeregionid) {
        this.homeregionid = homeregionid;
    }

    public String getHomestreet() {
        return homestreet;
    }

    public void setHomestreet(String homestreet) {
        this.homestreet = homestreet;
    }

    public String getHometelno() {
        return hometelno;
    }

    public void setHometelno(String hometelno) {
        this.hometelno = hometelno;
    }

    public Integer getHometownid() {
        return hometownid;
    }

    public void setHometownid(Integer hometownid) {
        this.hometownid = hometownid;
    }

    public Integer getIdentificationtypeid() {
        return identificationtypeid;
    }

    public void setIdentificationtypeid(Integer identificationtypeid) {
        this.identificationtypeid = identificationtypeid;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public Boolean getIslocked() {
        return islocked;
    }

    public void setIslocked(Boolean islocked) {
        this.islocked = islocked;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocationlatitude() {
        return locationlatitude;
    }

    public void setLocationlatitude(String locationlatitude) {
        this.locationlatitude = locationlatitude;
    }

    public String getLocationlongitude() {
        return locationlongitude;
    }

    public void setLocationlongitude(String locationlongitude) {
        this.locationlongitude = locationlongitude;
    }

    public Integer getMaritalstatusid() {
        return maritalstatusid;
    }

    public void setMaritalstatusid(Integer maritalstatusid) {
        this.maritalstatusid = maritalstatusid;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public Integer getNationalityid() {
        return nationalityid;
    }

    public void setNationalityid(Integer nationalityid) {
        this.nationalityid = nationalityid;
    }

    public Integer getOccupationid() {
        return occupationid;
    }

    public void setOccupationid(Integer occupationid) {
        this.occupationid = occupationid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPatientcategorytypeid() {
        return patientcategorytypeid;
    }

    public void setPatientcategorytypeid(Integer patientcategorytypeid) {
        this.patientcategorytypeid = patientcategorytypeid;
    }

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public Integer getPatientstatusid() {
        return patientstatusid;
    }

    public void setPatientstatusid(Integer patientstatusid) {
        this.patientstatusid = patientstatusid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getRegionid() {
        return regionid;
    }

    public void setRegionid(Integer regionid) {
        this.regionid = regionid;
    }

    public String getRegistrationstatus() {
        return registrationstatus;
    }

    public void setRegistrationstatus(String registrationstatus) {
        this.registrationstatus = registrationstatus;
    }

    public String getRegistrationtime() {
        return registrationtime;
    }

    public void setRegistrationtime(String registrationtime) {
        this.registrationtime = registrationtime;
    }

    public Integer getReligionid() {
        return religionid;
    }

    public void setReligionid(Integer religionid) {
        this.religionid = religionid;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public Boolean getSmsalert() {
        return smsalert;
    }

    public void setSmsalert(Boolean smsalert) {
        this.smsalert = smsalert;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public Integer getTitleid() {
        return titleid;
    }

    public void setTitleid(Integer titleid) {
        this.titleid = titleid;
    }

    public Integer getTownid() {
        return townid;
    }

    public void setTownid(Integer townid) {
        this.townid = townid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public void setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWorkdistrictid() {
        return workdistrictid;
    }

    public void setWorkdistrictid(Integer workdistrictid) {
        this.workdistrictid = workdistrictid;
    }

    public Integer getWorkregionid() {
        return workregionid;
    }

    public void setWorkregionid(Integer workregionid) {
        this.workregionid = workregionid;
    }

    public String getWorkstreet() {
        return workstreet;
    }

    public void setWorkstreet(String workstreet) {
        this.workstreet = workstreet;
    }

    public String getWorktelno() {
        return worktelno;
    }

    public void setWorktelno(String worktelno) {
        this.worktelno = worktelno;
    }

    public Integer getWorktownid() {
        return worktownid;
    }

    public void setWorktownid(Integer worktownid) {
        this.worktownid = worktownid;
    }
}
