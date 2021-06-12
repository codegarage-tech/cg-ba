package com.rc.ac.model;

public class APIMessage {
    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "APIMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
