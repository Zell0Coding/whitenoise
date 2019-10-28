package com.ponicamedia.android.whitenoise.Utills;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PersistantStorage {

    private static final String STORAGE_NAME = "WHITENOISE";
    private static SharedPreferences settings = null;
    private static SharedPreferences.Editor editor = null;
    private static Context context = null;

    public static void init(Context _context){
        context = _context;
    }

    private static void init() {
        settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.apply();
    }

    public static void addProperty(String name, boolean value){
        if(settings==null) init();
        editor.putBoolean(name,value);
        editor.apply();
    }

    public static void addProperty(String name, String value){
        if(settings==null) init();
        editor.putString(name,value);
        editor.apply();
    }

    public static boolean getProperty(String name){

        if(settings==null) init();

        Log.d("FIRST",settings.getBoolean(name,false) + "");

        return settings.getBoolean(name,false);
    }

    public static String getPropertyString(String name){
        if(settings==null) init();
        return settings.getString(name,null);
    }

    public static void deleteProperty(String name){
        if(settings==null) init();
        editor.remove(name);
        editor.apply();
    }


}
