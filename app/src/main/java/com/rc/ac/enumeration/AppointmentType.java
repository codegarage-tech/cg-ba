package com.rc.ac.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum AppointmentType {

    NONE("")
    , ADD("Add")
    , DETAIL("Detail")
    , EDIT("Edit")
    , DELETE("Delete");

    private final String appointmentTypeValue;

    private AppointmentType(String value) {
        appointmentTypeValue = value;
    }

    public boolean equalsName(String otherName) {
        return appointmentTypeValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.appointmentTypeValue;
    }


}