package com.rc.ac.model;

import org.parceler.Parcel;

@Parcel
public class CreatedBy {
    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                '}';
    }
}
