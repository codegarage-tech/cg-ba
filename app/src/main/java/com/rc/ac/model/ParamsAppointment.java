package com.rc.ac.model;

public class ParamsAppointment {
    private String token = "";
    private String fromDate = "";

    public ParamsAppointment() {
    }

    public ParamsAppointment(String token, String fromDate) {
        this.token = token;
        this.fromDate = fromDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", fromDate='" + fromDate + '\'' +
                '}';
    }
}
