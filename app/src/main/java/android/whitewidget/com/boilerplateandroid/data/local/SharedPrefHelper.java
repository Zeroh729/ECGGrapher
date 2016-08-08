package android.whitewidget.com.boilerplateandroid.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharedPrefHelper {
    private static SharedPrefHelper instance;
    private final String PREF_FILE_NAME = "android_boilerplate_pref_file";
    private SharedPreferences prefs;

    private SharedPrefHelper (Context context) {
        prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefHelper(context);
        }
        return instance;
    }

    public SharedPrefHelper putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return instance;
    }

    public SharedPrefHelper putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
        return instance;
    }

    public SharedPrefHelper putInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
        return instance;
    }

    public SharedPrefHelper putMapString(HashMap<String, String> map) {
        SharedPreferences.Editor editor = prefs.edit();
        for (String key : map.keySet()){
            editor.putString(key, map.get(key));
        }
        editor.commit();
        return instance;
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }

    public void clear() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
