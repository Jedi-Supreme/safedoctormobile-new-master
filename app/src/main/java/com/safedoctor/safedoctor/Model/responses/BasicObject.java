package com.safedoctor.safedoctor.Model.responses;

import java.io.Serializable;

/**
 * Created by Stevkkys on 9/20/2017.
 */

public class BasicObject implements BaseObject,Serializable
{
    private Integer id = null;
    private String name;
    private String info;
    private String info1;
    public String otherinfo;



    //for titles
    private Integer gendergroupid;

    public BasicObject(String name, String info, String info1) {
        this.name = name;
        this.info = info;
        this.info1 = info1;
    }


    public BasicObject(String name, String info, String info1,String otherinfo) {
        this.name = name;
        this.info = info;
        this.info1 = info1;
        this.otherinfo=otherinfo;
    }

    public Integer getGendergroupid() {
        return gendergroupid;
    }

    public void setGendergroupid(Integer gendergroupid) {
        this.gendergroupid = gendergroupid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    @Override
   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicObject()
    {

    }

    public BasicObject(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

}
