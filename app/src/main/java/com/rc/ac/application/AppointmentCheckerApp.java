package com.rc.ac.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.aacreations.aacustomfont.AACustomFont;
import com.rc.ac.util.AppUtil;
import com.rc.ac.util.Logger;


public class AppointmentCheckerApp extends Application {

    private static Context mContext;
    private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mContext == null) {
            mContext = this;
        }

        //Initialize logger
        new Logger.Builder()
                .isLoggable(AppUtil.isDebug(mContext))
//                .isLoggable(true)
                .build();

        //For using vector drawable
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //Initialize font
        initTypeface();
        AACustomFont.getInstance(mContext)
                .setAlias("myFont", "canaro_extra_bold.otf")
                .setDefaultFontName("myFont");

        //Multidex initialization
        MultiDex.install(this);
    }

    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);
    }

    public static Context getGlobalContext() {
        return mContext;
    }
}