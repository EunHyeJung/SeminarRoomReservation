package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ehye on 2015-09-23.
 */
public class SharedPreferenceClass {
    static Context context;

    public final static String PREF_NAME = "seminarroomreservation.pref";

    public SharedPreferenceClass(Context context) {
        this.context = context;
    }


    public static void putValue(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putValue(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putValue(String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static int getValue(String key, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            return preferences.getInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getValue(String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            return preferences.getString(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Boolean getValue(String key, Boolean defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            return preferences.getBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void removeAllPreferences() {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
