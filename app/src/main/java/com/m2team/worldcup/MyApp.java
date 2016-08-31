package com.m2team.worldcup;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AvenirLTStd-Roman.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
