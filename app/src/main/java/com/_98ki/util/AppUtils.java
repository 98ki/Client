package com._98ki.util;

import java.util.List;

import com.shape100.gym.MainApplication;
import com.shape100.gym.provider.SysSettings;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * AppUtils

 * 
 * @author zpy zpy@98ki.com
 */
public class AppUtils {

	
	/**
	 * whether this process is named with processName
	 * 
	 * @param context
	 * @param processName
	 * @return <ul>
	 *         return whether this process is named with processName
	 *         <li>if context is null, return false</li>
	 *         <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
	 *         <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
	 *         true, otherwise return false</li>
	 *         </ul>
	 */
	
	
	
	public static final String KEY_FIRSTSTART = "firststart";
	public static final String KEY_USERID = "userid";
	public static final String KEY_SCREENNAME = "screenname";
	
	
	
	
	
	public static boolean isNamedProcess(Context context, String processName) {
		if (context == null) {
			return false;
		}

		int pid = android.os.Process.myPid();
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
		if (processInfoList == null) {
			return true;
		}

		for (RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
			if (processInfo.pid == pid && ObjectUtils.isEquals(processName, processInfo.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * whether application is in background
	 * <ul>
	 * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
	 * </ul>
	 * 
	 * @param context
	 * @return if application is in background return true, otherwise return false
	 */
	public static boolean isApplicationInBackground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && !taskList.isEmpty()) {
			ComponentName topActivity = taskList.get(0).topActivity;
			if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static String getVersion(Context context) {
		String versionName = "";
		int versionCode = 0;
		String packageName = "";
		try {

			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			// 当前应用的版本名称
			versionName = info.versionName;

			// 当前版本的版本号
			versionCode = info.versionCode;

			// 当前版本的包名
			packageName = info.packageName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}
	
	public static void setFirstStart(boolean value){
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(KEY_FIRSTSTART, value).commit();	
	}
	
	public static boolean isFirstStart(){
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		return sp.getBoolean(KEY_FIRSTSTART, true);
	}
	
	public static void setUserId(int id){
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		sp.edit().putInt(KEY_USERID, id).commit();	
	}
	
	public static int getUserId(){
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		int userId = sp.getInt(KEY_USERID, 0);
		if(userId == 0){
			return 0;
		}else{
			return userId;
		}
		 
	}
}
