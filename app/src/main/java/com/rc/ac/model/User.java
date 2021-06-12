package com.rc.ac.model;

public class User {

    private String statusCode = "";
    private String userToken = "";
    private String message = "";

    public User() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "statusCode='" + statusCode + '\'' +
                ", userToken='" + userToken + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
