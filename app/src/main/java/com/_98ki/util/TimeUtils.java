package com._98ki.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TimeUtils
 * 
 * @author zpy zpy@98ki.com
 */
public class TimeUtils {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat ONLY_MONTH = new SimpleDateFormat(
			"mm.dd");

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	public static String dateToString(Date date) {
		return DEFAULT_DATE_FORMAT.format(date);
	}

	public static Date stringToDate(String string) {
		try {
			return DATE_FORMAT_DATE.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * getDayOfWeekByString
	 * 
	 * @param date
	 *            2000-00-00
	 * @return dayOfWeek
	 * @throws ParseException
	 */
	public static int getDayOfWeekByString(String string) {
		Date date = null;
		try {
			date = DATE_FORMAT_DATE.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week == 0) {
			week = 7;
		}
		return week;

	}

	/**
	 * 
	 * getWeekOfYearByString
	 * 
	 * @param date
	 *            2000-00-00
	 * @return weekOfYear
	 * @throws ParseException
	 */
	public static int getWeekOfYearByString(String string) {
		Date date = null;
		try {
			date = DATE_FORMAT_DATE.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		return week;

	}

	/**
	 * 
	 * getYearByString
	 * 
	 * @param date
	 *            2000-00-00
	 * @return weekOfYear
	 * @throws ParseException
	 */
	public static int getYearByString(String string) {
		Date date = null;
		try {
			date = DATE_FORMAT_DATE.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);

		return year;

	}

	public static int getWeekOfYearByCalendar(Calendar cd) {
		return cd.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getYear() {
		Calendar cd = Calendar.getInstance();
		return cd.get(Calendar.YEAR);
	}

	public static int getWeekOfYear() {
		Calendar cd = Calendar.getInstance();
		//cd.setFirstDayOfWeek(Calendar.MONDAY); // 这里设置在androidSDK下是无效的，所以做以下判断，if周末的话，减一天
		if (cd.get(Calendar.DAY_OF_WEEK) == 1) {
			return cd.get(Calendar.WEEK_OF_YEAR) - 1;
		}
		return cd.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 
	 * getWeekDate get the date from this monday to sunday
	 * 
	 * @param return startEndDate
	 */
	public static String[] getWeekDate() {
		String[] startEndDate = new String[2];
		Calendar cd = Calendar.getInstance();
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		int offset = 1 - dayOfWeek;
		cd.add(Calendar.DATE, offset - 1);
		// just get Monday's date
		startEndDate[0] = cd.get(Calendar.YEAR) + "-"
				+ (cd.get(Calendar.MONTH) + 1) + "-"
				+ (cd.get(Calendar.DAY_OF_MONTH) + 1);
		// set MM.dd to TextView
		for (int i = 0; i < 7; i++) {
			cd.add(Calendar.DATE, 1);
			String text = String.format("%d.%d", cd.get(Calendar.MONTH) + 1,
					cd.get(Calendar.DAY_OF_MONTH));
		}
		startEndDate[1] = cd.get(Calendar.YEAR) + "-"
				+ (cd.get(Calendar.MONTH) + 1) + "-"
				+ cd.get(Calendar.DAY_OF_MONTH);
		return startEndDate;
	}

	/**
	 * 
	 * getWeekDate get the date from this monday to sunday
	 * 
	 * @param return startEndDate
	 */
	public static String[] getWeekDate(Calendar cd) {
		String[] startEndDate = new String[2];

		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}

		int offset = 1 - dayOfWeek;
		cd.add(Calendar.DATE, offset);
		// just get Monday's date
		startEndDate[0] = cd.get(Calendar.YEAR) + "-"
				+ (cd.get(Calendar.MONTH) + 1) + "-"
				+ (cd.get(Calendar.DAY_OF_MONTH));
		cd.add(Calendar.DATE, 6);
		startEndDate[1] = cd.get(Calendar.YEAR) + "-"
				+ (cd.get(Calendar.MONTH) + 1) + "-"
				+ cd.get(Calendar.DAY_OF_MONTH);
		return startEndDate;
	}
}
