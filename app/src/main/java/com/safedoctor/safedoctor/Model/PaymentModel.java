package com.safedoctor.safedoctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kwabena on 8/22/17.
 */

public class PaymentModel   implements Serializable {
    @SerializedName("paymentid")
    @Expose
    private Integer paymentid;
    @SerializedName("amountpaid")
    @Expose
    private Integer amountpaid;
    @SerializedName("amounttendered")
    @Expose
    private Integer amounttendered;
    @SerializedName("bookingid")
    @Expose
    private Integer bookingid;
    @SerializedName("changeamount")
    @Expose
    private Object changeamount;
    @SerializedName("currencyid")
    @Expose
    private Integer currencyid;
    @SerializedName("invoiceid")
    @Expose
    private Object invoiceid;
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("paymenttime")
    @Expose
    private String paymenttime;
    @SerializedName("receiptid")
    @Expose
    private Object receiptid;
    @SerializedName("totalbill")
    @Expose
    private Integer totalbill;
    @SerializedName("userid")
    @Expose
    private String userid;

    public Integer getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(Integer paymentid) {
        this.paymentid = paymentid;
    }

    public Integer getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(Integer amountpaid) {
        this.amountpaid = amountpaid;
    }

    public Integer getAmounttendered() {
        return amounttendered;
    }

    public void setAmounttendered(Integer amounttendered) {
        this.amounttendered = amounttendered;
    }

    public Integer getBookingid() {
        return bookingid;
    }

    public void setBookingid(Integer bookingid) {
        this.bookingid = bookingid;
    }

    public Object getChangeamount() {
        return changeamount;
    }

    public void setChangeamount(Object changeamount) {
        this.changeamount = changeamount;
    }

    public Integer getCurrencyid() {
        return currencyid;
    }

    public void setCurrencyid(Integer currencyid) {
        this.currencyid = currencyid;
    }

    public Object getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(Object invoiceid) {
        this.invoiceid = invoiceid;
    }

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public String getPaymenttime() {
        return paymenttime;
    }

    public void setPaymenttime(String paymenttime) {
        this.paymenttime = paymenttime;
    }

    public Object getReceiptid() {
        return receiptid;
    }

    public void setReceiptid(Object receiptid) {
        this.receiptid = receiptid;
    }

    public Integer getTotalbill() {
        return totalbill;
    }

    public void setTotalbill(Integer totalbill) {
        this.totalbill = totalbill;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
