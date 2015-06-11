package com.shape100.gym.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.provider.DBConst.CourseColumns;

/**
 * Course Table
 * 
 * @author zpy zpy@98ki.com
 * @date 2014-10-28 下午1:16:48
 * @version: V1.01
 */
public class CourseUtil {
	private static final Logger log = Logger.getLogger("CourseUtil");

	static SQLiteDatabase db;

	/**
	 * 
	 * clearCourseByClubId for update native database, the course information.
	 * 
	 * @param clubId
	 *            clear course by club id.
	 */
	public static void clearCourseByClubId(int clubId) {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		String sql = "club_id=" + clubId;
		db.delete(DBConst.TABLE_COURSE, sql, null);
	}

	/**
	 * Save Course information list, if courseId is execited
	 * 
	 * @throw
	 * @return void
	 */
	public static void saveCourse(ArrayList<CourseBean> list) {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		// clearCourse();
		for (int i = 0; i < list.size(); i++) {
			if (isExist(list.get(i).getId())) {
				updateCourse(list.get(i));
			} else {
				insertCourse(list.get(i));
			}
		}

	}

	/**
	 * Save Course bean, bean from course show. PicUrls,
	 * 
	 * 
	 * @throw
	 * @return void
	 */
	public static void saveCourse(CourseBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		if (isExist(bean.getId())) {
			updateCourse(bean);
		} else {
			insertCourse(bean);
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
			String sql = CourseColumns.CLUB_SCHEDULE_ID + "=" + id;

			c = db.query(DBConst.TABLE_COURSE, null, sql, null, null, null,
					null);
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
	public static void insertCourse(CourseBean bean) {
		ContentValues cv = new ContentValues();
		cv.put(CourseColumns.CLUB_SCHEDULE_ID, bean.getId());
		cv.put(CourseColumns.CLUB_ID, bean.getClubId());
		cv.put(CourseColumns.CLASS_ID, bean.getClassId());
		cv.put(CourseColumns.CLASS_NAME, bean.getClassName());
		cv.put(CourseColumns.COACH_ID, bean.getCoachId());
		cv.put(CourseColumns.COACH_NAME, bean.getCoachName());
		cv.put(CourseColumns.CLASSROOM_ID, bean.getClassRoomId());
		cv.put(CourseColumns.CLASSROOM_NAME, bean.getClassRoomName());
		cv.put(CourseColumns.BACKGROUND_COLOR, bean.getBackgroundColor());
		cv.put(CourseColumns.SCHEDULE_DATE, bean.getScheduleDate());
		cv.put(CourseColumns.START_TIME, bean.getStartTime() + "");
		cv.put(CourseColumns.END_TIME, bean.getEndTime() + "");
		cv.put(CourseColumns.MODIFIED, bean.getModified() + "");
		cv.put(CourseColumns.CREATED, bean.getCreated() + "");
		cv.put(CourseColumns.WEEKOFYEAR, bean.getWeekOfYear());
		cv.put(CourseColumns.YEAR, bean.getYear());
		cv.put(CourseColumns.ISALERMED, bean.getIsAlerted());
		db.insert(DBConst.TABLE_COURSE, null, cv);
	}

	/**
	 * clear table
	 */
	public static void clearCourse() {
		DatabaseHelper dbHelper = DatabaseHelper
				.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		db.delete("course", null, null);
	}

	/**
	 * Update course information
	 * 
	 * @param bean
	 */
	public static void updateCourse(CourseBean bean) {
		String sql = CourseColumns.CLUB_SCHEDULE_ID + "=" + bean.getId();
		ContentValues cv = new ContentValues();
		cv.put(CourseColumns.CLUB_ID, bean.getClubId());
		cv.put(CourseColumns.CLASS_ID, bean.getClassId());
		cv.put(CourseColumns.CLASS_NAME, bean.getClassName());
		cv.put(CourseColumns.COACH_ID, bean.getCoachId());
		cv.put(CourseColumns.COACH_NAME, bean.getCoachName());
		cv.put(CourseColumns.CLASSROOM_ID, bean.getClassRoomId());
		cv.put(CourseColumns.CLASSROOM_NAME, bean.getClassRoomName());
		cv.put(CourseColumns.START_TIME, bean.getStartTime() + "");
		cv.put(CourseColumns.END_TIME, bean.getEndTime() + "");
		cv.put(CourseColumns.BACKGROUND_COLOR, bean.getBackgroundColor());
		cv.put(CourseColumns.SCHEDULE_DATE, bean.getScheduleDate());
		cv.put(CourseColumns.MODIFIED, bean.getModified() + "");
		cv.put(CourseColumns.CREATED, bean.getCreated() + "");
		cv.put(CourseColumns.ISALERMED, bean.getIsAlerted());
		cv.put(CourseColumns.WEEKOFYEAR, bean.getWeekOfYear());
		cv.put(CourseColumns.YEAR, bean.getYear());

		db.update(DBConst.TABLE_COURSE, cv, sql, null);

	}

	/**
	 * Get Course by schedule id
	 * 
	 * @throw
	 * @return CourseBean
	 */
	public static CourseBean getCourseById(int scheduleId) {

		String sql = CourseColumns.CLUB_SCHEDULE_ID + "=" + scheduleId;
		CourseBean bean = new CourseBean();
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper
					.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			c = db.query(DBConst.TABLE_COURSE, null, sql, null, null, null,
					null);
			if (c.moveToNext()) {
				bean.setId(scheduleId);
				bean.setClubId(c.getInt(c.getColumnIndex(CourseColumns.CLUB_ID)));
				bean.setClassId(c.getInt(c
						.getColumnIndex(CourseColumns.CLASS_ID)));
				bean.setClassName(c.getString(c
						.getColumnIndex(CourseColumns.CLASS_NAME)));
				bean.setClassRoomId(c.getInt(c
						.getColumnIndex(CourseColumns.CLASSROOM_ID)));
				bean.setClassRoomName(c.getString(c
						.getColumnIndex(CourseColumns.CLASSROOM_NAME)));
				bean.setCoachId(c.getInt(c
						.getColumnIndex(CourseColumns.COACH_ID)));
				bean.setCoachName(c.getString(c
						.getColumnIndex(CourseColumns.COACH_NAME)));
				bean.setStartTime(c.getString(c
						.getColumnIndex(CourseColumns.START_TIME)));
				bean.setBackgroundColor(c.getString(c
						.getColumnIndex(CourseColumns.BACKGROUND_COLOR)));
				bean.setScheduleDate(c.getString(c
						.getColumnIndex(CourseColumns.SCHEDULE_DATE)));
				bean.setEndTime(c.getString(c
						.getColumnIndex(CourseColumns.END_TIME)));
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
	 * Get Course list by sql
	 * 
	 * @param sql
	 * @throw
	 * @return CourseBean
	 */
	public static ArrayList<CourseBean> getCourseBySql(String sql) {

		ArrayList<CourseBean> list = new ArrayList<CourseBean>();
		CourseBean bean = null;
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper
					.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			c = db.query(DBConst.TABLE_COURSE, null, sql, null, null, null,
					null);
			log.d("CourseUtil  getBySql, sql:" + sql);

			while (c.moveToNext()) {
				bean = new CourseBean();
				bean.setId(c.getInt(c
						.getColumnIndex(CourseColumns.CLUB_SCHEDULE_ID)));
				bean.setClubId(c.getInt(c.getColumnIndex(CourseColumns.CLUB_ID)));
				bean.setClassId(c.getInt(c
						.getColumnIndex(CourseColumns.CLASS_ID)));
				bean.setClassName(c.getString(c
						.getColumnIndex(CourseColumns.CLASS_NAME)));
				bean.setClassRoomId(c.getInt(c
						.getColumnIndex(CourseColumns.CLASSROOM_ID)));
				bean.setClassRoomName(c.getString(c
						.getColumnIndex(CourseColumns.CLASSROOM_NAME)));
				bean.setCoachId(c.getInt(c
						.getColumnIndex(CourseColumns.COACH_ID)));
				bean.setCoachName(c.getString(c
						.getColumnIndex(CourseColumns.COACH_NAME)));
				bean.setStartTime(c.getString(c
						.getColumnIndex(CourseColumns.START_TIME)));
				bean.setBackgroundColor(c.getString(c
						.getColumnIndex(CourseColumns.BACKGROUND_COLOR)));
				bean.setScheduleDate(c.getString(c
						.getColumnIndex(CourseColumns.SCHEDULE_DATE)));
				bean.setEndTime(c.getString(c
						.getColumnIndex(CourseColumns.END_TIME)));
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
	 * Get the club's first classroom name
	 * 
	 * @throw
	 * @return classRoomName first class room's name
	 */
	public static String getFirstClassRoomName() {
		String sql = CourseColumns.CLUB_ID + "="
				+ AccountDetailUtil.getUserClub();

		String classRoomName = "";
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper
					.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			c = db.query(DBConst.TABLE_COURSE, null, sql, null, null, null,
					null);
			if (c.moveToNext()) {
				classRoomName = c.getString(c
						.getColumnIndex(CourseColumns.CLASSROOM_NAME));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				log.d("getFirstClassRoomName(), name = " + classRoomName);
				c.close();
			}
		}
		return classRoomName;
	}

	/**
	 * 
	 * getCourseFilterDataModel classroom_name class_name coach_name
	 * 
	 * @param name
	 */
	public static List getCourseFilterDataModel() {
		log.d("get getCourseFilterDataModel start!");
		// sql
		String sql = 1 + "=" + 1;
		//
		ArrayList<CourseBean> list = new ArrayList<CourseBean>();
		CourseBean bean = null;
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper
					.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			c = db.query(DBConst.TABLE_COURSE, new String[] { "classroom_name", // 修改之后查询所有课程，不去重
					"class_name", "coach_name" }, sql, null, null, null,
					"classroom_id", null);
			while (c.moveToNext()) {
				bean = new CourseBean();
				bean.setClassRoomName(c.getString(c
						.getColumnIndex(CourseColumns.CLASSROOM_NAME)));
				bean.setClassName(c.getString(c
						.getColumnIndex(CourseColumns.CLASS_NAME)));
				bean.setCoachName(c.getString(c
						.getColumnIndex(CourseColumns.COACH_NAME)));
				log.d("getCourseFilterDataModel: " + bean.getClassRoomName()
						+ "," + bean.getClassName() + "," + bean.getCoachName()
						+ "," + list.size());
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		log.d("get getCourseFilterDataModel end!");
		return list;

	}

}