package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwabena on 8/17/17.
 */

public class ServiceContentModel {

    @SerializedName("agegroupid")
    @Expose
    private Integer agegroupid;
    @SerializedName("allowcopay")
    @Expose
    private Boolean allowcopay;
    @SerializedName("createtime")
    @Expose
    private String createtime;
    @SerializedName("createuserid")
    @Expose
    private String createuserid;
    @SerializedName("gendergroupid")
    @Expose
    private Integer gendergroupid;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("isactive")
    @Expose
    private Boolean isactive;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("patientstatusid")
    @Expose
    private Integer patientstatusid;
    @SerializedName("servertime")
    @Expose
    private String servertime;
    @SerializedName("servicecategoryid")
    @Expose
    private Integer servicecategoryid;
    @SerializedName("serviceplaceid")
    @Expose
    private Integer serviceplaceid;
    @SerializedName("servicesubcategoryid")
    @Expose
    private Integer servicesubcategoryid;
    @SerializedName("servicetypeid")
    @Expose
    private Integer servicetypeid;
    @SerializedName("updatetime")
    @Expose
    private String updatetime;
    @SerializedName("updateuserid")
    @Expose
    private String updateuserid;

    public Integer getAgegroupid() {
        return agegroupid;
    }

    public void setAgegroupid(Integer agegroupid) {
        this.agegroupid = agegroupid;
    }

    public Boolean getAllowcopay() {
        return allowcopay;
    }

    public void setAllowcopay(Boolean allowcopay) {
        this.allowcopay = allowcopay;
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

    public Integer getGendergroupid() {
        return gendergroupid;
    }

    public void setGendergroupid(Integer gendergroupid) {
        this.gendergroupid = gendergroupid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPatientstatusid() {
        return patientstatusid;
    }

    public void setPatientstatusid(Integer patientstatusid) {
        this.patientstatusid = patientstatusid;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public Integer getServicecategoryid() {
        return servicecategoryid;
    }

    public void setServicecategoryid(Integer servicecategoryid) {
        this.servicecategoryid = servicecategoryid;
    }

    public Integer getServiceplaceid() {
        return serviceplaceid;
    }

    public void setServiceplaceid(Integer serviceplaceid) {
        this.serviceplaceid = serviceplaceid;
    }

    public Integer getServicesubcategoryid() {
        return servicesubcategoryid;
    }

    public void setServicesubcategoryid(Integer servicesubcategoryid) {
        this.servicesubcategoryid = servicesubcategoryid;
    }

    public Integer getServicetypeid() {
        return servicetypeid;
    }

    public void setServicetypeid(Integer servicetypeid) {
        this.servicetypeid = servicetypeid;
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

}
