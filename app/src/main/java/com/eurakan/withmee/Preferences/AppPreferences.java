package com.eurakan.withmee.Preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.eurakan.withmee.Activity.LoginActivity;
import com.eurakan.withmee.Models.UserModel;

public class AppPreferences {
    private static final String APP_SHARED_PREFS = "withmee";
    //the constants
    private static final String SHARED_PREF_NAME = "withmeeapplicationnew";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_NAME = "fullname";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_ADDRESS = "fullAddress";
    private static final String KEY_AADHAR = "aadharNumber";
    private static final String KEY_WORK = "work";
    private static final String KEY_STUDY = "studyIn";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DOB = "dob";
    private static final String KEY_PROFILE_PHOTO = "profilephoto";
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;
    private static AppPreferences mInstance;
    private static Context mCtx;


    public AppPreferences(Context context) {
        mCtx = context;
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public void RemoveAllSharedPreference() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public String getName() {
        return appSharedPrefs.getString("name", " ");
    }

    public void setName(String text) {
        prefsEditor.putString("name", text);
        prefsEditor.commit();
    }

    public static synchronized AppPreferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppPreferences(context);
        }
        return mInstance;
    }

    public void userLogin(UserModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_ADDRESS, user.getFullAddress());
        editor.putString(KEY_MOBILE, user.getMobile());
        editor.putString(KEY_AADHAR, String.valueOf(user.getAadharNumber()));
        editor.putString(KEY_WORK, user.getWork());
        editor.putString(KEY_STUDY, user.getStudyIn());
        editor.putString(KEY_CITY, user.getCity());
        editor.putString(KEY_STATUS, user.getStatus());
        editor.putString(KEY_DOB, user.getDateOfBirth());
        editor.putString(KEY_PROFILE_PHOTO, user.getProfileImageUrl());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public UserModel getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        UserModel user =  new UserModel(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_MOBILE, ""),
                sharedPreferences.getString(KEY_EMAIL, ""),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_AADHAR, ""),
                sharedPreferences.getString(KEY_GENDER, ""),
                sharedPreferences.getString(KEY_ADDRESS, ""),
                sharedPreferences.getString(KEY_WORK, ""),
                sharedPreferences.getString(KEY_STATUS, ""),
                sharedPreferences.getString(KEY_CITY, ""),
                sharedPreferences.getString(KEY_STUDY, ""),
                sharedPreferences.getString(KEY_DOB, "")
                );
        user.setProfileImageUrl(sharedPreferences.getString(KEY_PROFILE_PHOTO, ""));
        return  user;
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

}