package com.safedoctor.safedoctor.Model;

/**
 * Created by stevkky on 08/12/2018.
 */

public class Peripheral
{
    private String id;
    private String name;
    private Integer vitaltypeid;
    private String manufacturer;
    private boolean isactive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVitaltypeid() {
        return vitaltypeid;
    }

    public void setVitaltypeid(Integer vitaltypeid) {
        this.vitaltypeid = vitaltypeid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
}
