/**    
 * file name：FavorityUtil.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-14 
 * @version:    
 * Copyright zpy@98ki.com Corporation 2014         
 *    
 */
package com.shape100.gym.provider;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.provider.DBConst.FavoriteColumns;

/**    
 *     
 * project：shape100    
 * class：FavorityUtil    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-14 下午10:20:28    
 * modify author: zpy    
 * update date：2014-11-14 下午10:20:28    
 * update remark：    
 * @version     
 *     
 */
public class CourseFavoriteUtil {
	private static final Logger log = Logger.getLogger("CourseFavoriteUtil");

	static SQLiteDatabase db;

	/**
	 * 
	 * clearCourseByUserId
	 * for update native database, the favorite course information. when many account login, we can cache.
	 * @param clubId clear course by club id.
	 */
	public static void clearCourseByUserId(int user_schedule_id) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		String sql = FavoriteColumns.ID+"=" + user_schedule_id;
		db.delete(DBConst.TABLE_COURSE_FAVORITE, sql, null);
	}

	/**
	 * clear table
	 */
	public static void clearCourse() {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		db.delete(DBConst.TABLE_COURSE_FAVORITE, null, null);
	}

	public static void saveFavorite(ArrayList<CourseBean> list) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		clearCourse();
		for (int i = 0; i < list.size(); i++) {
			if (isExist(list.get(i).getId())) {
				updateFavorite(list.get(i));
			} else {
				insertFavorite(list.get(i));
			}
		}

	}

	/**
	 * Save Favorite bean, bean from favorite show. PicUrls,
	 * 
	 * @throw
	 * @return void
	 */
	public static void saveFavorite(CourseBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();

		if (isExist(bean.getId())) {
			updateFavorite(bean);
		} else {
			insertFavorite(bean);
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
	 * Save Course information
	 * 
	 * @param bean
	 */
	public static void insertFavorite(CourseBean bean) {
		ContentValues cv = new ContentValues();
			cv.put(FavoriteColumns.ID, bean.getId());
			cv.put(FavoriteColumns.CLUB_ID, bean.getClubId());
			cv.put(FavoriteColumns.CLASS_ID, bean.getClassId());
			cv.put(FavoriteColumns.CLASS_NAME, bean.getClassName());
			cv.put(FavoriteColumns.COACH_ID, bean.getCoachId());
			cv.put(FavoriteColumns.COACH_NAME, bean.getCoachName());
			cv.put(FavoriteColumns.CLASSROOM_ID, bean.getClassRoomId());
			cv.put(FavoriteColumns.CLASSROOM_NAME, bean.getClassRoomName());
			cv.put(FavoriteColumns.BACKGROUND_COLOR, bean.getBackgroundColor());
			cv.put(FavoriteColumns.SCHEDULE_DATE, bean.getScheduleDate());
			cv.put(FavoriteColumns.START_TIME, bean.getStartTime() + "");
			cv.put(FavoriteColumns.END_TIME, bean.getEndTime() + "");
			cv.put(FavoriteColumns.MODIFIED, bean.getModified() + "");
			cv.put(FavoriteColumns.CREATED, bean.getCreated() + "");
			cv.put(FavoriteColumns.YEAR, bean.getYear());
			cv.put(FavoriteColumns.ISALERMED, bean.getIsAlerted());
			cv.put(FavoriteColumns.WEEKOFYEAR, bean.getWeekOfYear());
		
		db.insert(DBConst.TABLE_COURSE_FAVORITE, null, cv);

	}

	/**
	 * Update course information
	 * 
	 * @param bean
	 */
	public static void updateFavorite(CourseBean bean) {
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
		    cv.put(FavoriteColumns.ISALERMED, bean.getIsAlerted());
		db.update(DBConst.TABLE_COURSE_FAVORITE, cv, sql, null);

	}

	/**
	 *  Get Favorite List
	 */
	public static ArrayList<CourseBean> getFavoriteList() {

		ArrayList<CourseBean> list = new ArrayList<CourseBean>();
		CourseBean bean = null;
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String sql = "1=1";
			c = db.query(DBConst.TABLE_COURSE_FAVORITE, null, sql, null, null, null, null);
			while (c.moveToNext()) {
				bean = new CourseBean();
				bean.setId(c.getInt(c.getColumnIndex(FavoriteColumns.ID)));
				bean.setClubId(c.getInt(c.getColumnIndex(FavoriteColumns.CLUB_ID)));
				bean.setClassId(c.getInt(c.getColumnIndex(FavoriteColumns.CLASS_ID)));
				bean.setClassName(c.getString(c.getColumnIndex(FavoriteColumns.CLASS_NAME)));
				bean.setClassRoomId(c.getInt(c.getColumnIndex(FavoriteColumns.CLASSROOM_ID)));
				bean.setClassRoomName(c.getString(c.getColumnIndex(FavoriteColumns.CLASSROOM_NAME)));
				bean.setCoachId(c.getInt(c.getColumnIndex(FavoriteColumns.COACH_ID)));
				bean.setCoachName(c.getString(c.getColumnIndex(FavoriteColumns.COACH_NAME)));
				bean.setStartTime(c.getString(c.getColumnIndex(FavoriteColumns.START_TIME)));
				bean.setBackgroundColor(c.getString(c.getColumnIndex(FavoriteColumns.BACKGROUND_COLOR)));
				bean.setScheduleDate(c.getString(c.getColumnIndex(FavoriteColumns.SCHEDULE_DATE)));
				bean.setEndTime(c.getString(c.getColumnIndex(FavoriteColumns.END_TIME)));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return list;
	}
	
	/**
	 * Get Favorite list by sql
	 * @param sql
	 * @throw
	 * @return CourseBean
	 */
	public static ArrayList<CourseBean> getFavoriteBySql(String sql) {
	
		
		ArrayList<CourseBean> list = new ArrayList<CourseBean>();
		CourseBean bean = null;
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			c = db.query(DBConst.TABLE_COURSE_FAVORITE, null, sql, null, null, null, null);
			log.d("CourseFavoriteUtil  getBySql, sql:" + sql);

			while (c.moveToNext()) {
				bean = new CourseBean();
				bean.setId(c.getInt(c.getColumnIndex(FavoriteColumns.ID)));
				bean.setClubId(c.getInt(c.getColumnIndex(FavoriteColumns.CLUB_ID)));
				bean.setClassId(c.getInt(c.getColumnIndex(FavoriteColumns.CLASS_ID)));
				bean.setClassName(c.getString(c.getColumnIndex(FavoriteColumns.CLASS_NAME)));
				bean.setClassRoomId(c.getInt(c.getColumnIndex(FavoriteColumns.CLASSROOM_ID)));
				bean.setClassRoomName(c.getString(c.getColumnIndex(FavoriteColumns.CLASSROOM_NAME)));
				bean.setCoachId(c.getInt(c.getColumnIndex(FavoriteColumns.COACH_ID)));
				bean.setCoachName(c.getString(c.getColumnIndex(FavoriteColumns.COACH_NAME)));
				bean.setStartTime(c.getString(c.getColumnIndex(FavoriteColumns.START_TIME)));
				bean.setBackgroundColor(c.getString(c.getColumnIndex(FavoriteColumns.BACKGROUND_COLOR)));
				bean.setScheduleDate(c.getString(c.getColumnIndex(FavoriteColumns.SCHEDULE_DATE)));
				bean.setEndTime(c.getString(c.getColumnIndex(FavoriteColumns.END_TIME)));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				log.d("getCourseBySql, listSize = " + list.size());
				c.close();
			}
		}
		log.d("get Course By Sql()  end!");

		return list;
	}

	/**    
	 * isMyFavoriteCourse
	 * @param schedule_id course's schedule id;
	 * @return ismyfavorite 
	*/
	public static boolean isMyFavoriteCourse(CourseBean bean){
		// TODO Auto-generated method stub
		Cursor c = null;

		try {
			String sql = FavoriteColumns.CLASS_ID + "=" + bean.getClassId()+" and "+FavoriteColumns.COACH_ID+"="+bean.getCoachId()+" and "+FavoriteColumns.SCHEDULE_DATE+"='"+bean.getScheduleDate() +"'"+" and "+ FavoriteColumns.START_TIME +"='"+ bean.getStartTime()+"'";
			c = db.query(DBConst.TABLE_COURSE_FAVORITE, null, sql, null, null, null, null);
			if (c.moveToNext()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return false;
	}
}
