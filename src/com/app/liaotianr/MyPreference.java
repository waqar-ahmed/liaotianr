package com.app.liaotianr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreference {
	
	public static class Keys{
		public static final String IS_USER_REGISTERED = "is_user_registered";
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
	}
	
	public static int getInt(Context context, String key){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		int value = prefs.getInt(key, -1);
		return value;
	}
	
	public static boolean getBoolean(Context context, String key){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean value = prefs.getBoolean(key, false);
		return value;
	}
	
	public static String getString(Context context, String key){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String value = prefs.getString(key, "");
		return value;
	}
	
	public static void saveString(Context context, String key, String value){
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void saveInt(Context context, String key, int value){
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void saveBoolean(Context context, String key, boolean value){
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}
