package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by stevkky on 10/12/17.
 */

public class UserAccount implements Serializable {

    private String id;
    private String  firstname;
    private String  lastname;
    private String  othername;
    private String  username;
    private int  titleid;
    private BasicObject title;
    private String  emailaddress;
    private String  primarycellno;
    private BasicObject usertype;
    private int  gendergroupid;
    private BasicObject gendergroup;
    private  int userprofileid;
    private boolean locked;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getFullname(){
        if(othername==null){
            return title.getName()+" "+firstname+" "+lastname;
        }else{
            return title.getName()+" "+firstname+" "+othername+" "+lastname;
        }

    }

    public int getUserprofileid() {
        return userprofileid;
    }

    public void setUserprofileid(int userprofileid) {
        this.userprofileid = userprofileid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTitleid() {
        return titleid;
    }

    public void setTitleid(int titleid) {
        this.titleid = titleid;
    }

    public BasicObject getTitle() {
        return title;
    }

    public void setTitle(BasicObject title) {
        this.title = title;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPrimarycellno() {
        return primarycellno;
    }

    public void setPrimarycellno(String primarycellno) {
        this.primarycellno = primarycellno;
    }

    public BasicObject getUsertype() {
        return usertype;
    }

    public void setUsertype(BasicObject usertype) {
        this.usertype = usertype;
    }

    public int getGendergroupid() {
        return gendergroupid;
    }

    public void setGendergroupid(int gendergroupid) {
        this.gendergroupid = gendergroupid;
    }

    public BasicObject getGendergroup() {
        return gendergroup;
    }

    public void setGendergroup(BasicObject gendergroup) {
        this.gendergroup = gendergroup;
    }
}
