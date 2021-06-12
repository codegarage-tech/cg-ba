package com.rc.ac.util;

public class AllUrls {
    private static final String BASE_URL = "http://45.64.134.103:8000/bepza_mis-122/CmApps/";

    public static String getLoginUrl(String userName, String password){
        String url = BASE_URL+"login?username="+userName+"&password="+password;
        return url;
    }
}
