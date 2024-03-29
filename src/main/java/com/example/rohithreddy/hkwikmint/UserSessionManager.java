package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by rohith reddy on 6/8/2017.
 */

public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASS = "PASS";
    public static final String KEY_FULLNAME = "FULLNAME";
    private static final String ATTENDANCE = "ISON";
    private static final String VIDEO = "VIDEO";

    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String name, String pass,String fullanme){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_PASS, pass);

        editor.putString(KEY_FULLNAME,fullanme);

        // commit changes
        editor.commit();
    }
    public void startattendance(){
        System.out.print("\nattendance recorded");
        editor.putBoolean(ATTENDANCE, true);
        editor.commit();
    }
    public void stopattendance(){
        System.out.print("\nattendance recorded stipped");
        editor.putBoolean(ATTENDANCE, false);
        editor.commit();
    }
    public boolean isattendancestarted(){
        System.out.print("my session is "+ pref.getBoolean(ATTENDANCE, false)+" ");
        return pref.getBoolean(ATTENDANCE, false);
    }
    public void startvideo(){
        System.out.print("\nVIDEO recorded");
        editor.putBoolean(VIDEO, true);
        editor.commit();
    }
    public void stopvideo(){
        System.out.print("\nVIDEO recorded stipped");
        editor.putBoolean(VIDEO, false);
        editor.commit();
    }
    public boolean isvideostarted(){
        System.out.print("my VIDEO is "+ pref.getBoolean(VIDEO, false)+" ");
        return pref.getBoolean(VIDEO, false);
    }


    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_PASS, pref.getString(KEY_PASS, null));
        user.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
       // Intent loginIntent = new Intent(tabs.this, LoginActivity.class);
       // tabs.this.startActivity(loginIntent);
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, Login.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
