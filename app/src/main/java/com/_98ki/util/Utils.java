package com._98ki.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.widget.Toast;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.provider.SysSettings;

public class Utils {
	private static final Logger log = Logger.getLogger("Utils");

	public static final String SP_SETTING_FILENAME = "settings";

	public static final String KEY_FIRSTSTART = "firststart";

	public static final String BDpackage = "com.baidu.BaiduMap"; // 百度地图包名
	public static final String GDpackage = "com.autonavi.minimap";// 高德地图包名
	public static final String TCpackage = "tengxun";// 腾讯地图 ,腾讯不支持

	// public static final String KEY_TOKEN = "oauth_token";
	// public static final String KEY_TOKEN_SECRET = "oauth_token_secret";
	public static final String KEY_USERID = "userid";
	public static final String KEY_SCREENNAME = "screenname";

	public static void setFirstStart(boolean value) {
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(
				SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(KEY_FIRSTSTART, value).commit();
	}

	public static boolean isFirstStart() {
		SharedPreferences sp = MainApplication.sContext.getSharedPreferences(
				SysSettings.SP_SETTING_FILENAME, Context.MODE_PRIVATE);
		return sp.getBoolean(KEY_FIRSTSTART, true);
	}

	/**
	 * 
	 * setCalendarEvent
	 * 
	 * @param context
	 * @param title
	 * @param description
	 * @param location
	 * @param start
	 *            long
	 * @param end
	 *            long
	 * @param allDay
	 *            allDay:1
	 * @param hasAlarm
	 *            Alarm:1
	 * @param minutes
	 *            how many minutes early to alarm
	 * @param method
	 *            alarm method, ALERT:1, EMAIL:2, SMS:3
	 * 
	 * @return 1).calId+","+remid 2).calId 3).0
	 */
	public static String setCalendarEvent(Context context, String title,
			String description, String location, long start, long end,
			int allDay, int hasAlarm, int minutes, int method) {

		String calanderURL = "content://com.android.calendar/calendars";
		String calanderEventURL = "content://com.android.calendar/events";
		String calanderRemiderURL = "content://com.android.calendar/reminders";
		Cursor userCursor = context.getContentResolver().query(
				Uri.parse(calanderURL), null, null, null, null);
		if (userCursor.getCount() > 0) {
			userCursor.moveToFirst();
			String calId = userCursor.getString(userCursor
					.getColumnIndex("_id"));
			ContentValues event = new ContentValues();

			event.put(Events.CALENDAR_ID, calId);
			event.put(Events.TITLE, title);
			event.put(Events.DESCRIPTION, description);
			event.put(Events.EVENT_TIMEZONE, Time.getCurrentTimezone());
			event.put(Events.DTSTART, start);
			event.put(Events.DTEND, end);
			event.put(Events.EVENT_LOCATION, location);
			event.put(Events.ALL_DAY, allDay);
			event.put(Events.HAS_ALARM, hasAlarm);
			Uri newEvent = context.getContentResolver().insert(
					Uri.parse(calanderEventURL), event);

			Toast.makeText(context, "加入日历成功!", Toast.LENGTH_SHORT).show();

			long remId = 0;
			if (hasAlarm == 1) {
				ContentValues reminder = new ContentValues();
				remId = Long.parseLong(newEvent.getLastPathSegment());
				reminder.put(Reminders.EVENT_ID, remId);
				reminder.put("minutes", minutes);

				int methodType = Reminders.METHOD_DEFAULT;
				if (method == 1) {
					methodType = Reminders.METHOD_ALERT;
				} else if (method == 2) {
					methodType = Reminders.METHOD_EMAIL;
				} else if (method == 3) {
					methodType = Reminders.METHOD_SMS;
				}
				reminder.put(Reminders.METHOD, methodType);

				context.getContentResolver().insert(
						Uri.parse(calanderRemiderURL), reminder);

				return calId + "," + remId;
			}
			return calId;

		}
		return 0 + "";
	}

	public static void deleteCalendarEvent(Context context, String EventId) {
		String calanderURL = "content://com.android.calendar/calendars";
		String calanderEventURL = "content://com.android.calendar/events";
		String calanderRemiderURL = "content://com.android.calendar/reminders";

		String calId = "";
		long remId = 0;
		String sql = "";
		if (!EventId.equals("0")) {
			if (EventId.split(",").length == 1) {
				calId = EventId;
				sql = Events.CALENDAR_ID + " = '" + calId + "'";
				context.getContentResolver().delete(
						Uri.parse(calanderEventURL), sql, null);

			} else {
				calId = EventId.split(",")[0];
				remId = Long.parseLong(EventId.split(",")[1]);
				sql = Events.CALENDAR_ID + " = '" + calId + "'";
				context.getContentResolver().delete(
						Uri.parse(calanderEventURL), sql, null);
				sql = Reminders.EVENT_ID + " = '" + remId + "'";
				context.getContentResolver().delete(
						Uri.parse(calanderRemiderURL), sql, null);
			}
		}

		Toast.makeText(context, "删除日历项成功!", Toast.LENGTH_SHORT).show();

	}

	public void showCalendarEvent(Context context) {
		String calanderURL = "content://com.android.calendar/calendars";
		String calanderEventURL = "content://com.android.calendar/events";
		String calanderRemiderURL = "content://com.android.calendar/reminders";
		//
		Cursor c = context.getContentResolver().query(
				Uri.parse(calanderEventURL),
				new String[] { Events.CALENDAR_ID, Events.TITLE,
						Events.DESCRIPTION }, null, null, null);
		while (c.moveToNext()) {
			log.e(c.toString());
		}
		//
	}

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	public static boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	/**
	 * 验证手机号是否合法
	 * 
	 * @author yupu
	 * @date 2015年1月30日
	 */
	public static boolean isValidMobile(String mobile) {
		String reg = "^1\\d{10}";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}

	/**
	 * 验证密码是否合法
	 * 
	 * @author yupu
	 * @date 2015年1月30日
	 */
	public static boolean isValidPassword(String password) {
		return password.replaceAll(" ", "").length() >= 6;
	}

	/**
	 * 获取手机的信息
	 * 
	 * @author yupu
	 * @date 2015年2月6日
	 */
	public static String getinfo() {
		TelephonyManager tm = (TelephonyManager) MainApplication.getInstance()
				.getSystemService(
						MainApplication.getInstance().TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		String number = tm.getLine1Number(); // 获取手机号码
		String type = android.os.Build.MODEL;
		return number;
	}

	/**
	 * 将所有的数字、字母及标点全部转为全角字符
	 * 
	 * @author yupu
	 * @date 2015年3月16日
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 移除字符串中多余的空格
	 * 
	 * @author yupu
	 * @date 2015年4月11日
	 */
	public static String RemoveMore(String text) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '\n' && i < text.length() - 1) {
				if (text.charAt(i + 1) == '\n') {
					continue;
				}
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 查看内存使用
	 * 
	 * @author yupu
	 * @date 2015年4月17日
	 */
	public static String getMemory(Context context) {
		String me = new String();
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		manager.getMemoryInfo(info);
		System.out.println("-----------------------系统剩余内存" + info.availMem);
		return me;
	}
}
