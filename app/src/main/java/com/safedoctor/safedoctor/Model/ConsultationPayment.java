package com.safedoctor.safedoctor.Model;

public class ConsultationPayment {


        private  Double amountpaid;
        private Long  consultationid;

        private String mobilemoneynetworkid;
        private String paymentdate;
        private String serviceid;
        private String serviceproviderid;
        private Integer servicetypeid;
        private String vodafonetoken;
        private String walletnumber;


    public ConsultationPayment(Double amountpaid, Long consultationid, String mobilemoneynetworkid, String serviceid, String serviceproviderid, Integer servicetypeid, String vodafonetoken, String walletnumber) {
        this.amountpaid = amountpaid;
        this.consultationid = consultationid;
        this.mobilemoneynetworkid = mobilemoneynetworkid;
        this.serviceid = serviceid;
        this.serviceproviderid = serviceproviderid;
        this.servicetypeid = servicetypeid;
        this.vodafonetoken = vodafonetoken;
        this.walletnumber = walletnumber;
    }

    public Double getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(Double amountpaid) {
        this.amountpaid = amountpaid;
    }

    public Long getConsultationid() {
        return consultationid;
    }

    public void setConsultationid(Long consultationid) {
        this.consultationid = consultationid;
    }

    public String getMobilemoneynetworkid() {
        return mobilemoneynetworkid;
    }

    public void setMobilemoneynetworkid(String mobilemoneynetworkid) {
        this.mobilemoneynetworkid = mobilemoneynetworkid;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getServiceproviderid() {
        return serviceproviderid;
    }

    public void setServiceproviderid(String serviceproviderid) {
        this.serviceproviderid = serviceproviderid;
    }

    public Integer getServicetypeid() {
        return servicetypeid;
    }

    public void setServicetypeid(Integer servicetypeid) {
        this.servicetypeid = servicetypeid;
    }

    public String getVodafonetoken() {
        return vodafonetoken;
    }

    public void setVodafonetoken(String vodafonetoken) {
        this.vodafonetoken = vodafonetoken;
    }

    public String getWalletnumber() {
        return walletnumber;
    }

    public void setWalletnumber(String walletnumber) {
        this.walletnumber = walletnumber;
    }
}
