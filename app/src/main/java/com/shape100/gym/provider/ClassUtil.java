/**    
 * file name：ClassUtil.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-21 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.model.ClassBean;
import com.shape100.gym.provider.DBConst.ClassColumns;

/**    
 *     
 * project：shape100    
 * class：ClassUtil    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-21 上午9:29:43    
 * modify author: zpy    
 * update date：2014-11-21 上午9:29:43    
 * update remark：    
 * @version     
 *     
 */
public class ClassUtil {
	
	private static final Logger log = Logger.getLogger("ClassUtil");

	/**
	 * Get class by class id
	 * 
	 * @throw
	 * @return ClassBean
	 */
	public static ClassBean getClassById(int classId) {

		String sql = ClassColumns.CLASS_ID + "=" + classId;

		ClassBean bean = new ClassBean();
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			c = db.query(DBConst.TABLE_CLASS, null, sql, null, null, null, null);
			if (c.moveToNext()) {
				bean.setClassId(classId);
				bean.setClassName(c.getString(c.getColumnIndex(ClassColumns.CLASS_NAME)));
				bean.setClubId(c.getInt(c.getColumnIndex(ClassColumns.CLUB_ID)));

				bean.setPicUrls(c.getString(c.getColumnIndex(ClassColumns.PIC_URLS)));
				bean.setVideo_thumbnail_pic(c.getString(c.getColumnIndex(ClassColumns.VIDEO_THUMBNAIL_PIC)));
				bean.setVideo_url(c.getString(c.getColumnIndex(ClassColumns.VIDEO_URL)));
				bean.setDescription(c.getString(c.getColumnIndex(ClassColumns.DESCRIPTION)));
				bean.setPreregistration_phone(c.getString(c.getColumnIndex(ClassColumns.PREREGISTRATION_PHONE)));

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

	
	/**
	 * Save Course information list, if courseId is execited
	 * 
	 * @throw
	 * @return void
	 */
	public static void saveClass(ClassBean bean) {
		if (isExist(bean.getClassId())) {
				updateClass(bean);
			} else {
				insertClass(bean);
			}

	}
	/**
	 * Save Class information
	 * 
	 * @param bean
	 */
	public static void insertClass(ClassBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(ClassColumns.CLASS_ID, bean.getClassId());
		cv.put(ClassColumns.CLASS_NAME, bean.getClassName());
		cv.put(ClassColumns.CLUB_ID, bean.getClubId());
		cv.put(ClassColumns.PIC_URLS, bean.getPicUrls());
		cv.put(ClassColumns.VIDEO_THUMBNAIL_PIC, bean.getVideo_thumbnail_pic());
		cv.put(ClassColumns.VIDEO_URL, bean.getVideo_url());
		cv.put(ClassColumns.DESCRIPTION, bean.getDescription());
		cv.put(ClassColumns.PREREGISTRATION_PHONE, bean.getPreregistration_phone());

		db.insert(DBConst.TABLE_CLASS, null, cv);

	}

	/**
	 * Update class information
	 * 
	 * @param ClassBean
	 */
	public static void updateClass(ClassBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String sql = ClassColumns.CLASS_ID + "=" + bean.getClassId();
		ContentValues cv = new ContentValues();
		cv.put(ClassColumns.CLUB_ID, bean.getClubId());
		cv.put(ClassColumns.CLASS_NAME, bean.getClassName());
		cv.put(ClassColumns.PIC_URLS, bean.getPicUrls());
		cv.put(ClassColumns.VIDEO_THUMBNAIL_PIC, bean.getVideo_thumbnail_pic());
		cv.put(ClassColumns.VIDEO_URL, bean.getVideo_url());
		cv.put(ClassColumns.DESCRIPTION, bean.getDescription());
		cv.put(ClassColumns.PREREGISTRATION_PHONE, bean.getPreregistration_phone());

		db.update(DBConst.TABLE_CLASS, cv, sql, null);

	}
	/**
	 * if user_id isExist, return true
	 * 
	 * @param id
	 * @return boolean
	 */
	public static boolean isExist(long id) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		boolean exist = false;
		Cursor c = null;
		try {
			String sql = ClassColumns.CLASS_ID + "=" + id;

			c = db.query(DBConst.TABLE_CLASS, null, sql, null, null, null, null);
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
	 * clear table
	 */
	public static void clearClass() {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.delete("course", null, null);
	}
}
