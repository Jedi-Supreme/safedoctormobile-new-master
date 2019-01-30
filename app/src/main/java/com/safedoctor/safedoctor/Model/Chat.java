package com.safedoctor.safedoctor.Model;

public class Chat {

    private String message;

    private String senderid;

    private String receiverid;

    private Long consultationid;

    private String createdtime;

    private int id;

    private String receiver;

    private String sender;


    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Chat(String message, String senderid, String receiverid, Long consultationid) {
        this.message = message;
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.consultationid = consultationid;
    }


    public Chat(String message, String senderid, String receiverid) {
        this.message = message;
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public Long getConsultationid() {
        return consultationid;
    }

    public void setConsultationid(Long consultationid) {
        this.consultationid = consultationid;
    }
}
