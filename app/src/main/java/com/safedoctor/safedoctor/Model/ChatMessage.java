package com.safedoctor.safedoctor.Model;

/**
 * Created by beulahana on 23/01/2018.
 */

public class ChatMessage {

    private String msg;
    private String sender;
    private String time;
    private String pic;
    private String mobile;
    private boolean fromserver;// to check datetime

    public boolean isFromserver() {
        return fromserver;
    }

    public void setFromserver(boolean fromserver) {
        this.fromserver = fromserver;
    }

    public ChatMessage(String msg, String sender, String time, String pic, String mobile) {
        this.msg = msg;
        this.sender = sender;
        this.time = time;
        this.pic = "";
        this.mobile=mobile;
    }

    public ChatMessage(String msg, String sender, String time, String pic, String mobile,boolean fromserver) {
        this.msg = msg;
        this.sender = sender;
        this.time = time;
        this.pic = "";
        this.mobile=mobile;
        this.fromserver=fromserver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
