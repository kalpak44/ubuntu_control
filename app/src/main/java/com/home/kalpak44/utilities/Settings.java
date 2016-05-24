package com.home.kalpak44.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kalpak44 on 23.05.16.
 */
public class Settings {
    private SharedPreferences SETTINGS;
    private String CONFIG_FILE = "configurations.pref";
    public Settings(Context context){
        SETTINGS = context.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = SETTINGS.edit();
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = SETTINGS.edit();
        editor.putString("PASSWORD", password);
        editor.commit();
    }
    public void setHost(String host) {
        SharedPreferences.Editor editor = SETTINGS.edit();
        editor.putString("HOST", host);
        editor.commit();
    }
    public void setPort(int port) {
        SharedPreferences.Editor editor = SETTINGS.edit();
        editor.putInt("PORT", port);
        editor.commit();
    }

    public String getUsername() {
        return SETTINGS.getString("USERNAME","");
    }

    public String getPassword() {
        return SETTINGS.getString("PASSWORD","");
    }

    public String getHost() {
        return SETTINGS.getString("HOST","");
    }

    public int getPort() {
        return SETTINGS.getInt("PORT", 22);
    }

    public boolean hasSettings() {
        // We just check if a username has been set
        return !SETTINGS.getString("HOST", "").equals("") &&
               !SETTINGS.getString("PASSWORD","").equals("") &&
               !SETTINGS.getString("USERNAME","").equals("");
    }
}
