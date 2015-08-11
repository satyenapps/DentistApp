package com.appslight.dentistapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.appslight.dentistapp.gcmpackage.GCMRegistration;

/**
 * Created by Satyen on 01/06/2015.
 */
public class SessionManager {

    public static final String PREFERENCE_NAME = "com.appslight.dentistapp";

    public static final String DEFAULT = "default";

    public static final String AGREE = "agree";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DOB = "dob";
    public static final String GENDER = "gender";
    public static final String LOCATION = "location";

    public static final String CURRENT_IMAGE = "cur_image";


    private Context context;
    SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveImage(String imageName) {
        sharedPreferences.edit().putString(CURRENT_IMAGE, imageName).apply();
    }

    public String getImage() {
        return sharedPreferences.getString(CURRENT_IMAGE, DEFAULT);
    }

    public void createSession(Detail detail) {
        sharedPreferences.edit().putInt(ID, detail.getId()).apply();
        sharedPreferences.edit().putString(NAME, detail.getName()).apply();
        sharedPreferences.edit().putString(DOB, detail.getDob()).apply();
        sharedPreferences.edit().putString(GENDER, detail.getGender()).apply();
        sharedPreferences.edit().putString(LOCATION, detail.getLocation()).apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(ID, -1);
    }

    public String getUserName() {
        return sharedPreferences.getString(NAME, DEFAULT);
    }

    public String getUserIDob() {
        return sharedPreferences.getString(DOB, "");
    }

    public String getUserGender() {
        return sharedPreferences.getString(GENDER, "");
    }

    public String getUserLocation() {
        return sharedPreferences.getString(LOCATION, "");
    }

    public boolean isLogin() {
        return sharedPreferences.getString(GCMRegistration.REG_ID, "") != "" ? true : false;
    }

    public void setAgree() {
        sharedPreferences.edit().putString(AGREE, "yes").apply();
    }

    public boolean isAgreed() {
        return sharedPreferences.getString(AGREE, "no") != "no" ? true : false;
    }
}
