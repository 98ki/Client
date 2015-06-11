/**    
 * file name：FavoriteUtil.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-20 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shape100.gym.MainApplication;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.provider.DBConst.CourseColumns;
import com.shape100.gym.provider.DBConst.FavoriteColumns;

/**    
 *     
 * project：shape100    
 * class：FavoriteUtil    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-20 下午10:44:51    
 * modify author: zpy    
 * update date：2014-11-20 下午10:44:51    
 * update remark：    
 * @version     
 *     
 */
public class FavoriteUtil {

	static SQLiteDatabase db;

	/**
	 * Save Favorite bean, bean from favorite show. PicUrls,
	 * 
	 * 
	 * @throw
	 * @return void
	 */
	public static void saveFavorite(CourseBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		if (isExist(bean.getId())) {
			updateCourse(bean);
		} else {
			//fuck the different schedule id.
			//insertCourse(bean, ConstVar.COURSE_SHOW_INFO);
		}

	}
	
	/**
	 * if user_id isExist, return true
	 * 
	 * @param id
	 * @return boolean
	 */
	public static boolean isExist(long id) {
		boolean exist = false;
		Cursor c = null;
		try {
			String sql = FavoriteColumns.ID + "=" + id;

			c = db.query(DBConst.TABLE_COURSE_FAVORITE, null, sql, null, null, null, null);
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
	 * Update course information
	 * 
	 * @param bean
	 */
	public static void updateCourse(CourseBean bean) {
		String sql = FavoriteColumns.ID + "=" + bean.getId();
		ContentValues cv = new ContentValues();
			cv.put(FavoriteColumns.CLUB_ID, bean.getClubId());
			cv.put(FavoriteColumns.CLASS_ID, bean.getClassId());
			cv.put(FavoriteColumns.CLASS_NAME, bean.getClassName());
			cv.put(FavoriteColumns.COACH_ID, bean.getCoachId());
			cv.put(FavoriteColumns.COACH_NAME, bean.getCoachName());
			cv.put(FavoriteColumns.CLASSROOM_ID, bean.getClassRoomId());
			cv.put(FavoriteColumns.CLASSROOM_NAME, bean.getClassRoomName());
			cv.put(FavoriteColumns.START_TIME, bean.getStartTime() + "");
			cv.put(FavoriteColumns.END_TIME, bean.getEndTime() + "");
			cv.put(FavoriteColumns.BACKGROUND_COLOR, bean.getBackgroundColor());
			cv.put(FavoriteColumns.SCHEDULE_DATE, bean.getScheduleDate());
			cv.put(FavoriteColumns.MODIFIED, bean.getModified() + "");
		
		db.update(DBConst.TABLE_COURSE_FAVORITE, cv, sql, null);

	}
	
	/**
	 * Get Favorite by favorite course's schedule id
	 * 
	 * @throw
	 * @return CourseBean
	 */
	public static CourseBean getCourseById(int scheduleId) {

		String sql = FavoriteColumns.ID + "=" + scheduleId;

		CourseBean bean = new CourseBean();
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			c = db.query(DBConst.TABLE_COURSE_FAVORITE, null, sql, null, null, null, null);
			if (c.moveToNext()) {
				bean.setId(scheduleId);
				bean.setClubId(c.getInt(c.getColumnIndex(CourseColumns.CLUB_ID)));
				bean.setClassId(c.getInt(c.getColumnIndex(CourseColumns.CLASS_ID)));
				bean.setClassName(c.getString(c.getColumnIndex(CourseColumns.CLASS_NAME)));
				bean.setClassRoomId(c.getInt(c.getColumnIndex(CourseColumns.CLASSROOM_ID)));
				bean.setClassRoomName(c.getString(c.getColumnIndex(CourseColumns.CLASSROOM_NAME)));
				bean.setCoachId(c.getInt(c.getColumnIndex(CourseColumns.COACH_ID)));
				bean.setCoachName(c.getString(c.getColumnIndex(CourseColumns.COACH_NAME)));
				bean.setStartTime(c.getString(c.getColumnIndex(CourseColumns.START_TIME)));
				bean.setBackgroundColor(c.getString(c.getColumnIndex(CourseColumns.BACKGROUND_COLOR)));
				bean.setScheduleDate(c.getString(c.getColumnIndex(CourseColumns.SCHEDULE_DATE)));
				bean.setEndTime(c.getString(c.getColumnIndex(CourseColumns.END_TIME)));
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
