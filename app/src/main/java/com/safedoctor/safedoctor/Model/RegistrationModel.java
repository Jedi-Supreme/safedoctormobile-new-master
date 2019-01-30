package com.safedoctor.safedoctor.Model;


/**
 * Created by kwabena on 8/17/17.
 */

public class RegistrationModel {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String dateofbirth;
    private String phonenumber;
    private Integer gendergroupid;

    public RegistrationModel(String username, String password, String firstname, String lastname, String dateofbirth, String phonenumber, Integer gendergroupid) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateofbirth = dateofbirth;
        this.phonenumber = phonenumber;
        this.gendergroupid = gendergroupid;
    }
}
