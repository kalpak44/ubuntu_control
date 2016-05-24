package com.home.kalpak44.utilities;

import android.app.Application;



/**
 * Created by kalpak44 on 23.05.16.
 */
public class MainContext extends Application {
    private Settings config = null;
    @Override
    public void onCreate() {
        super.onCreate();
        config = new Settings(getApplicationContext());
    }

    synchronized public Settings getConfig() {
        return config;
    }

}
