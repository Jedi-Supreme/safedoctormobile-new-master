package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kwabena on 8/20/17.
 */

public class ServiceFeeDataModel {
    @SerializedName("agegroupid")
    @Expose
    private Integer agegroupid;
    @SerializedName("consultationtyid")
    @Expose
    private Integer consultationtyid;
    @SerializedName("createtime")
    @Expose
    private String createtime;
    @SerializedName("createuserid")
    @Expose
    private String createuserid;
    @SerializedName("currencyid")
    @Expose
    private Integer currencyid;
    @SerializedName("effectivedate")
    @Expose
    private String effectivedate;
    @SerializedName("fee")
    @Expose
    private Integer fee;
    @SerializedName("feetypeid")
    @Expose
    private Integer feetypeid;
    @SerializedName("gendergroupid")
    @Expose
    private Integer gendergroupid;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("isactive")
    @Expose
    private Boolean isactive;
    @SerializedName("servertime")
    @Expose
    private String servertime;
    @SerializedName("serviceid")
    @Expose
    private Integer serviceid;
    @SerializedName("servicetypeid")
    @Expose
    private Integer servicetypeid;
    @SerializedName("sponsorid")
    @Expose
    private Integer sponsorid;
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

    public Integer getConsultationtyid() {
        return consultationtyid;
    }

    public void setConsultationtyid(Integer consultationtyid) {
        this.consultationtyid = consultationtyid;
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

    public Integer getCurrencyid() {
        return currencyid;
    }

    public void setCurrencyid(Integer currencyid) {
        this.currencyid = currencyid;
    }

    public String getEffectivedate() {
        return effectivedate;
    }

    public void setEffectivedate(String effectivedate) {
        this.effectivedate = effectivedate;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getFeetypeid() {
        return feetypeid;
    }

    public void setFeetypeid(Integer feetypeid) {
        this.feetypeid = feetypeid;
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

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public Integer getServiceid() {
        return serviceid;
    }

    public void setServiceid(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public Integer getServicetypeid() {
        return servicetypeid;
    }

    public void setServicetypeid(Integer servicetypeid) {
        this.servicetypeid = servicetypeid;
    }

    public Integer getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(Integer sponsorid) {
        this.sponsorid = sponsorid;
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
