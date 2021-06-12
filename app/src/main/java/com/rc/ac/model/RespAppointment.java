package com.rc.ac.model;

import java.util.ArrayList;
import java.util.List;

public class RespAppointment {

    private String statusCode = "";
    private String message = "";
    private List<Appointment> app = new ArrayList<>();

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Appointment> getApp() {
        return app;
    }

    public void setApp(List<Appointment> app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "{" +
                "statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                ", app=" + app +
                '}';
    }
}
