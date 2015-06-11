package com.shape100.gym.provider;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.provider.DBConst.AccountDetailColumns;

public class AccountDetailUtil extends DatabaseHelper {
	private static final Logger log = Logger.getLogger("AccountDetailUtil");

	/**
	 * 
	 * New Instance： AccountDetailUtil.
	 * 
	 * @param context
	 */
	public AccountDetailUtil(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取用户信
	 * 
	 * @author yupu
	 * @date 2015年3月11日
	 */
	public static AccountDetailBean getAccountDetailBean() {
		int userId = AppConfig.getInstance().getUserId();
		if (userId == 0) {
			return null;
		}
		return getAccountDetailBean(userId);
	}

	public static AccountDetailBean getAccountDetailBean(int userId) {
		AccountDetailBean bean = new AccountDetailBean();

		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = null;

		bean.setUserId(userId);
		String sql = AccountDetailColumns.USER_ID + "=" + userId;
		c = db.query(DBConst.TABLE_ACCOUNTDETAIL, null, sql, null, null, null,
				null);

		while (c.moveToNext()) {
			if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY)).equals(
					"name")) {
				bean.setName(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("screen_name")) {
				bean.setScreenName(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("location")) {
				bean.setLocation(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("description")) {
				bean.setDescription(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("profile_image_url")) {
				bean.setProfileImageUrl(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("url")) {
				bean.setUrl(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("protected")) {
				bean.setProtect(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("followers_count")) {
				bean.setFollowersCount(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("friends_count")) {
				bean.setFriendsCount(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("favorites_count")) {
				bean.setFavoritesCount(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("utc_offset")) {
				bean.setUtcOffset(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("time_zone")) {
				bean.setTimeZone(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("statuses_Count")) {
				bean.setStatusesCount(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("following")) {
				bean.setFollowing(Boolean.parseBoolean(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE))));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("notifications")) {
				bean.setNotifications(Boolean.parseBoolean(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE))));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("gender")) {
				bean.setGender(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("birthday")) {
				bean.setBirthday(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("height")) {
				bean.setHeight(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("weight")) {
				bean.setWeight(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("club_id")) {
				bean.setClubId(c.getInt(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			} else if (c.getString(c.getColumnIndex(AccountDetailColumns.KEY))
					.equals("certification")) {
				bean.setCertification(c.getString(c
						.getColumnIndex(AccountDetailColumns.VALUE)));
			}
		}
		log.d("AccountDetailUtil-> height=" + bean.getHeight());
		if (c != null) {
			c.close();
		}
		return bean;
	}

	/**
	 * 
	 * saveAccountDetail
	 * 
	 * @param
	 */
	public static void saveAccountDetail(AccountDetailBean bean) {
		Map<String, String> map = bean.toHashMap();
		String userId = map.get("user_id");
		deleteAccountDetail(userId);
		insertAccountDetail(userId, map);
	}

	/**
	 * 
	 * saveAccountDetail
	 * 
	 * @param
	 */
	public static void saveAccountDetail(String userId, Map<String, String> map) {
		deleteAccountDetail(userId);
		insertAccountDetail(userId, map);
	}

	/**
	 * 
	 * insertAccountDetail
	 * 
	 * @param
	 */
	public static void insertAccountDetail(String userId,
			Map<String, String> map) {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			cv.clear();
			cv.put(AccountDetailColumns.USER_ID, userId);
			cv.put(AccountDetailColumns.KEY, entry.getKey());
			cv.put(AccountDetailColumns.VALUE, entry.getValue());
			db.insert(DBConst.TABLE_ACCOUNTDETAIL, null, cv);
		}
	}

	/**
	 * 
	 * deleteAccountDetail
	 * 
	 * @param
	 */
	public static void deleteAccountDetail(String userId) {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String sql = AccountDetailColumns.USER_ID + "=" + userId;
		db.delete(DBConst.TABLE_ACCOUNTDETAIL, sql, null);
	}

	/**
	 * join a club. if joined update.if not login, return false.
	 * 
	 * @throw
	 * @return Boolean
	 */
	public static Boolean joinClub(int clubId) {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int userId = AppConfig.getInstance().getUserId();
		if (userId == 0) {
			return false;
		} else {
			ContentValues cv = new ContentValues();
			cv.put(AccountDetailColumns.USER_ID, AppConfig.getInstance()
					.getUserId());
			cv.put(AccountDetailColumns.KEY, "club_id");
			cv.put(AccountDetailColumns.VALUE, clubId);
			db.replace(DBConst.TABLE_ACCOUNTDETAIL, null, cv);
		}
		return true;
	}

	/**
	 * 
	 * exitClub remove club_id in accountdetail Table, clear club table
	 * 
	 * @param
	 */
	public static void exitClub() {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String sql = AccountDetailColumns.KEY + "=" + "'club_id'";
		db.delete(DBConst.TABLE_ACCOUNTDETAIL, sql, null);
		// clear club table
		db.delete("club", null, null);
		db.delete(DBConst.TABLE_COURSE_FAVORITE, null, null);
		AccountDetailUtil.getAccountDetailBean().setClubId(0);
		// clear course table
		db.delete("course", null, null);
	}

	/**
	 * through current login info get his clubId
	 * 
	 * @throw
	 * @return clubId
	 */
	public static int getUserClub() {
		long userId = AppConfig.getInstance().getUserId();
		String sql = AccountDetailColumns.USER_ID + "=" + userId + " and "
				+ AccountDetailColumns.KEY + "='club_id'";
		log.d("userId:" + userId + "");
		Cursor c = null;

		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		c = db.query(DBConst.TABLE_ACCOUNTDETAIL, null, sql, null, null, null,
				null);

		if (c.moveToNext()) {
			return Integer.parseInt(c.getString(c
					.getColumnIndex(AccountDetailColumns.VALUE)));
		} else {
			Toast.makeText(MainApplication.sContext, R.string.join_no_club,
					Toast.LENGTH_SHORT).show();
		}

		if (c != null) {
			c.close();
		}
		return 0;
	}
}
