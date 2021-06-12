package io.armcha.ribble.presentation.navigationview;

public enum NavigationId {

    APPOINTMENTS("APPOINTMENTS"),
    ADD_APPOINTMENT("ADD APPOINTMENT"),
    LOGOUT("LOGOUT");

    private String value = "";

    NavigationId(String name) {
        value = name;
    }

    public String getValue() {
        return value;
    }
}