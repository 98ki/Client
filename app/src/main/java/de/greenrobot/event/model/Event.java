/**    
 * file name：Even.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-16 
 * @version:    
 * Copyright zpy@98ki.com Corporation 2014         
 *    
 */
package de.greenrobot.event.model;

/**
 * 
 * project：shape100 class：Event desc： author：zpy zpy@98ki.com create
 * date：2014-11-16 下午1:53:44 modify author: zpy update date：2014-11-17
 * 下午11:17:12 update remark：
 * 
 * @version
 * 
 */

public class Event {
	/**
	 * 
	 * 
	 * project：shape100 class：RefreshScheduleEvent desc： refresh schedule
	 * author：zpy zpy@98ki.com create date：2014-11-16 下午2:00:31 modify author:
	 * zpy update date：2014-11-16 下午2:00:31 update remark：
	 * 
	 * @version
	 *
	 */
	public static class RefreshScheduleEvent {
		private String mString;

		public RefreshScheduleEvent(String string) {
			mString = string;
		}

		public String getString() {
			return mString;
		}
	}

	/**
	 * 
	 * 
	 * project：shape100 class：RefreshFavoriteEvent desc： author：zpy zpy@98ki.com
	 * create date：2014-11-17 下午2:23:15 modify author: zpy update
	 * date：2014-11-17 下午2:23:15 update remark：
	 * 
	 * @version
	 *
	 */
	public static class RefreshFavoriteEvent {
		private String mString;

		public RefreshFavoriteEvent(String string) {
			mString = string;
		}

		public String getString() {
			return mString;
		}
	}

	/**
	 * 
	 * 
	 * project：shape100 class：CourseFilterEvent desc： course filter on schedule
	 * author：zpy zpy@98ki.com create date：2014-11-16 下午2:00:50 modify author:
	 * zpy update date：2014-11-16 下午2:00:50 update remark：
	 * 
	 * @version
	 *
	 */
	public static class CourseFilterEvent {
		private String mSql;
		private String mClassName;
		/**
		 * kind of filter, such as "date" and "keyword"
		 */
		private String mFilter;
		private int mYear;
		private int mWeekOfYear;
		private String[] mStartEndDate;

		public CourseFilterEvent(String filter, String sql) {
			mFilter = filter;
			mSql = sql;
		}

		public CourseFilterEvent(String filter, String sql,
				String[] startEndDate) {
			mFilter = filter;
			mSql = sql;
			mStartEndDate = startEndDate;
		}

		public String[] getStartEndDate() {
			return mStartEndDate;
		}

		public void setStartEndDate(String[] startEndDate) {
			mStartEndDate = startEndDate;
		}

		public int getmYear() {
			return mYear;
		}

		public void setmYear(int year) {
			this.mYear = year;
		}

		public int getmWeekOfYear() {
			return mWeekOfYear;
		}

		public void setmWeekOfYear(int mWeekOfYear) {
			this.mWeekOfYear = mWeekOfYear;
		}

		public String getmSql() {
			return mSql;
		}

		public void setmSql(String mSql) {
			this.mSql = mSql;
		}

		public String getmFilter() {
			return mFilter;
		}

		public void setmFilter(String mFilter) {
			this.mFilter = mFilter;
		}

		public String getSql() {
			return mSql;
		}

		public String getmClassName() {
			return mClassName;
		}

		public void setmClassName(String mClassName) {
			this.mClassName = mClassName;
		}
	}

	/**
	 * 
	 * 
	 * project：shape100 class：FavoriteFilterEvent desc： author：zpy zpy@98ki.com
	 * create date：2014-11-28 上午10:14:10 modify author: zpy update
	 * date：2014-11-28 上午10:14:10 update remark：
	 * 
	 * @version
	 *
	 */
	public static class FavoriteFilterEvent {
		private String mSql;
		/**
		 * kind of filter, such as "date" and "keyword"
		 */
		private String mFilter;
		private int mYear;
		private int mWeekOfYear;
		private String[] mStartEndDate;

		public FavoriteFilterEvent(String filter, String sql) {
			mFilter = filter;
			mSql = sql;
		}

		public FavoriteFilterEvent(String filter, String sql,
				String[] startEndDate) {
			mFilter = filter;
			mSql = sql;
			mStartEndDate = startEndDate;
		}

		public String[] getStartEndDate() {
			return mStartEndDate;
		}

		public void setStartEndDate(String[] startEndDate) {
			mStartEndDate = startEndDate;
		}

		public int getmYear() {
			return mYear;
		}

		public void setmYear(int year) {
			this.mYear = year;
		}

		public int getmWeekOfYear() {
			return mWeekOfYear;
		}

		public void setmWeekOfYear(int mWeekOfYear) {
			this.mWeekOfYear = mWeekOfYear;
		}

		public String getmSql() {
			return mSql;
		}

		public void setmSql(String mSql) {
			this.mSql = mSql;
		}

		public String getmFilter() {
			return mFilter;
		}

		public void setmFilter(String mFilter) {
			this.mFilter = mFilter;
		}

		public String getSql() {
			return mSql;
		}
	}

	/**
	 * 
	 * 
	 * project：shape100 class：ChageFragmentEvent desc： author：zpy zpy@98ki.com
	 * create date：2014-11-18 下午4:29:45 modify author: zpy update
	 * date：2014-11-18 下午4:29:45 update remark：
	 * 
	 * @version 1.0
	 *
	 */
	public static class ChangeFragmentEvent {
		private String mFragmentName;

		public ChangeFragmentEvent(String fragmentName) {
			mFragmentName = fragmentName;
		}

		public String getFragmentName() {
			return mFragmentName;
		}
	}

	/**
	 * 
	 * 
	 * project：shape100 class：CourseShowEvent desc： course show author：zpy
	 * zpy@98ki.com create date：2014-11-20 下午4:15:44 modify author: zpy update
	 * date：2014-11-20 下午4:15:44 update remark：
	 * 
	 * @version
	 *
	 */
	public static class CourseShowEvent {
		private String mCourseShowName;

		public CourseShowEvent(String courseShowName) {
			mCourseShowName = courseShowName;
		}

		public String getCourseShowName() {
			return mCourseShowName;
		}
	}

	public static class CommendChangeEvent {

	}

	// 备用
	public static class ChangeDynamicEvent {
		private int event;

		public int getEvent() {
			return event;
		}

		public void setEvent(int event) {
			this.event = event;
		}

	}
}