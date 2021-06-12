package com.rc.ac.model;

import org.parceler.Parcel;

@Parcel
public class Appointment {

    private String id = "";
    private String update = "";
    private String aTime = "";
    private String aNo = "";
    private String remarks = "";
    private String crBy = "";
    private String aDate = "";
    private String crDate = "";
    private String aWith = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getaTime() {
        return aTime;
    }

    public void setaTime(String aTime) {
        this.aTime = aTime;
    }

    public String getaNo() {
        return aNo;
    }

    public void setaNo(String aNo) {
        this.aNo = aNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCrBy() {
        return crBy;
    }

    public void setCrBy(String crBy) {
        this.crBy = crBy;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getCrDate() {
        return crDate;
    }

    public void setCrDate(String crDate) {
        this.crDate = crDate;
    }

    public String getaWith() {
        return aWith;
    }

    public void setaWith(String aWith) {
        this.aWith = aWith;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", update='" + update + '\'' +
                ", aTime='" + aTime + '\'' +
                ", aNo='" + aNo + '\'' +
                ", remarks='" + remarks + '\'' +
                ", crBy='" + crBy + '\'' +
                ", aDate='" + aDate + '\'' +
                ", crDate='" + crDate + '\'' +
                ", aWith='" + aWith + '\'' +
                '}';
    }
}
