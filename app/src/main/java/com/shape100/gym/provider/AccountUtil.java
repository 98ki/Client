package com.shape100.gym.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.provider.DBConst.AccountColumns;

public class AccountUtil {
	
	public static final int USERTYPE_MEMBER = 0;
	public static final int USERTYPE_TRAINER = 1;
	private static final Logger log = Logger.getLogger("AccountUtil");

	public static class AccountBean {
		public long mUserId;
		public String mToken;
		public String mTokenSecret;
		public String mScreenName;
		public int mUserType;
	}

	/**
	 * 帐户存在则更新，不存在保存帐户数据
	 * 
	 * @param bean
	 */
	public static void saveAccount(AccountBean bean) {
		if (isExist(bean.mUserId)) {
			updateAccount(bean);
		} else {
			insertAccount(bean);
		}
	}

	/**
	 * 根据user_id， 判断帐户是否存在
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean isExist(long userId) {
		boolean exist = false;
		Cursor c = null;
		try {
			String sql = AccountColumns.USER_ID + "=" + userId;
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			c = db.query(DBConst.TABLE_ACCOUNT, null, sql, null, null, null, null);
			if (c.moveToNext()) {
				exist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return exist;
	}

	/**
	 * 直接保存帐户数据
	 * 
	 * @param bean
	 */
	public static void insertAccount(AccountBean bean) {
		log.d("insertAccount() ->");

		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(AccountColumns.USER_ID, bean.mUserId);
		cv.put(AccountColumns.TOKEN, bean.mToken);
		cv.put(AccountColumns.TOKEN_SECRET, bean.mTokenSecret);
		cv.put(AccountColumns.SCREEN_NAME, bean.mScreenName);
		cv.put(AccountColumns.USER_TYPE, bean.mUserType);
		db.insert(DBConst.TABLE_ACCOUNT, null, cv);
	}

	/**
	 * 直接更新帐户数据
	 * 
	 * @param bean
	 */
	public static void updateAccount(AccountBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String sql = AccountColumns.USER_ID + "=" + bean.mUserId;

		ContentValues cv = new ContentValues();
		cv.put(AccountColumns.TOKEN, bean.mToken);
		cv.put(AccountColumns.TOKEN_SECRET, bean.mTokenSecret);
		cv.put(AccountColumns.SCREEN_NAME, bean.mScreenName);
		cv.put(AccountColumns.USER_TYPE, bean.mUserType);

		db.update(DBConst.TABLE_ACCOUNT, cv, sql, null);
	}

	public static AccountBean getAccount() {
		AccountBean bean = null;
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			c = db.query(DBConst.TABLE_ACCOUNT, null, null, null, null, null, null);
			if (c.moveToNext()) {
				bean = new AccountBean();
				bean.mUserId = c.getLong(c.getColumnIndex(AccountColumns.USER_ID));
				bean.mToken = c.getString(c.getColumnIndex(AccountColumns.TOKEN));
				bean.mTokenSecret = c.getString(c.getColumnIndex(AccountColumns.TOKEN_SECRET));
				bean.mScreenName = c.getString(c.getColumnIndex(AccountColumns.SCREEN_NAME));
				bean.mUserType = c.getInt(c.getColumnIndex(AccountColumns.USER_TYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return bean;
	}

}
