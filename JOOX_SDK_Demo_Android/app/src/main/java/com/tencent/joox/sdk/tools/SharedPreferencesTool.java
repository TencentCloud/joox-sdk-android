package com.tencent.joox.sdk.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesTool {

    private static String TAG = "SharedPreferencesTool";
    private SharedPreferences sp =  null;
    private static SharedPreferencesTool mInstance = null;
    private SharedPreferences.Editor editor;

    public static String LOGIN_TYPE = "LOGIN_METHOD";

    public static SharedPreferencesTool getmInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPreferencesTool(context);
        }
        return  mInstance;
    }

    public SharedPreferencesTool(Context context){
        sp  = context.getSharedPreferences("DEMO",Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public boolean commitIntValue(String key,Integer value){
        editor.putInt(key,value);
        return editor.commit();
    }

    public int getIntValue(String key,int defaultValue){
        return sp.getInt(key,defaultValue);
    }

    public boolean commitStringValue(String key, String value){
        editor.putString(key,value);
        return editor.commit();
    }

    public String getStringValue(String key, String defaultValue){
        return sp.getString(key, defaultValue);
    }
}
