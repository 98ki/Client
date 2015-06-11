package com.shape100.gym.provider;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.shape100.gym.MainApplication;

public class SysSettings {
	public static final String SP_SETTING_FILENAME = "settings";
	
	public static final String KEY_FIRSTSTART = "firststart";

//	public static final String KEY_TOKEN = "oauth_token";
//	public static final String KEY_TOKEN_SECRET = "oauth_token_secret";
	public static final String KEY_USERID = "userid";
	public static final String KEY_SCREENNAME = "screenname";
	
	public static void setFirstStart(boolean value){
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(KEY_FIRSTSTART, value).commit();	
	}
	
	public static boolean isFirstStart(){
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		return sp.getBoolean(KEY_FIRSTSTART, true);
	}
	
//	public static void setUserId(int id){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		sp.edit().putInt(KEY_USERID, id).commit();	
//	}
//	
//	public static int getUserId(){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		int userId = sp.getInt(KEY_USERID, 0);
//		if(userId == 0){
//			return 0;
//		}else{
//			return userId;
//		}
//	}
	
	
	
//	public static void setOAuthToken(String token){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		sp.edit().putString(KEY_TOKEN, token).commit();		
//	}
//	
//	public static String getOAuthToken(){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		return sp.getString(KEY_TOKEN, "");
//	}
//	
//	public static void setOAuthSecretToken(String secretToken){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		sp.edit().putString(KEY_TOKEN_SECRET, secretToken).commit();		
//	}
//	
//	public static String getOAuthSecretToken(){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		return sp.getString(KEY_TOKEN_SECRET, "");
//	}
//	
	
	
	
	public static int countBMI(float height,float weight){
		int bmi = 0;
		
		double fHeight =   height /100.0;
		bmi = (int)(weight / Math.pow(fHeight, 2));
		return bmi;
	}

	public static String doMorning() {
		String s;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		int hour = Integer.parseInt(sdf.format(date).split(":")[0]);
		if (hour < 12) {
			return "Good morning!";
		} else if (hour < 18) {
			return "Good afternoon!";
		} else {
			return "Good evening!";
		}
	}
//	
//	public static void setScreenName(String name){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		sp.edit().putString(KEY_SCREENNAME, name).commit();		
//	}
//	
//	public static String getScreenName(){
//		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
//		return sp.getString(KEY_SCREENNAME, "");
//	}
}
