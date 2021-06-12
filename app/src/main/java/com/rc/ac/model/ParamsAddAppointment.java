package com.rc.ac.model;

public class ParamsAddAppointment {
    private String token = "";
    private String aDate = "";
    private String aWith = "";
    private String aTime = "";
    private String aRemarks = "";
    private String aSlNo = "";

    public ParamsAddAppointment() {
    }

    public ParamsAddAppointment(String token, String aDate, String aWith, String aTime, String aRemarks, String aSlNo) {
        this.token = token;
        this.aDate = aDate;
        this.aWith = aWith;
        this.aTime = aTime;
        this.aRemarks = aRemarks;
        this.aSlNo = aSlNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getaWith() {
        return aWith;
    }

    public void setaWith(String aWith) {
        this.aWith = aWith;
    }

    public String getaTime() {
        return aTime;
    }

    public void setaTime(String aTime) {
        this.aTime = aTime;
    }

    public String getaRemarks() {
        return aRemarks;
    }

    public void setaRemarks(String aRemarks) {
        this.aRemarks = aRemarks;
    }

    public String getaSlNo() {
        return aSlNo;
    }

    public void setaSlNo(String aSlNo) {
        this.aSlNo = aSlNo;
    }

    @Override
    public String toString() {
        return "ParamsAddAppointment{" +
                "token='" + token + '\'' +
                ", aDate='" + aDate + '\'' +
                ", aWith='" + aWith + '\'' +
                ", aTime='" + aTime + '\'' +
                ", aRemarks='" + aRemarks + '\'' +
                ", aSlNo='" + aSlNo + '\'' +
                '}';
    }
}
