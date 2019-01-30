package com.safedoctor.safedoctor.Model.responses;

/**
 * Created by beulahana on 15/01/2018.
 */

public class ChatRoomToken {

    private String generatedtime;
    private String sessionid;
    private String token;


    public String getGeneratedtime() {
        return generatedtime;
    }

    public void setGeneratedtime(String generatedtime) {
        this.generatedtime = generatedtime;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
