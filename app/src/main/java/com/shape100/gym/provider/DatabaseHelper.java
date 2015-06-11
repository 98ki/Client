package com.shape100.gym.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shape100.gym.provider.DBConst.AccountColumns;
import com.shape100.gym.provider.DBConst.AccountDetailColumns;
import com.shape100.gym.provider.DBConst.ClassColumns;
import com.shape100.gym.provider.DBConst.ClubColumns;
import com.shape100.gym.provider.DBConst.CourseColumns;
import com.shape100.gym.provider.DBConst.FavoriteColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "gym.db";
	private static final int DB_VERSION = 2;
	private static DatabaseHelper sInstance;

	public static synchronized DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context);
		}
		return sInstance;
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	private void createTables(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + DBConst.TABLE_ACCOUNT + " ("
				+ AccountColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ AccountColumns.USER_ID + " INTEGER," + AccountColumns.TOKEN
				+ " TEXT," + AccountColumns.TOKEN_SECRET + " TEXT,"
				+ AccountColumns.SCREEN_NAME + " TEXT,"
				+ AccountColumns.USER_TYPE + " INTEGER" + ");");

		db.execSQL("CREATE TABLE " + DBConst.TABLE_ACCOUNTDETAIL + " ("
				+ AccountDetailColumns.USER_ID + " INTEGER,"
				+ AccountDetailColumns.KEY + " TEXT,"
				+ AccountDetailColumns.VALUE + " TEXT,"
				+ " constraint pk_t2 primary key ("
				+ AccountDetailColumns.USER_ID + "," + AccountDetailColumns.KEY
				+ ")" + ");");

		db.execSQL("CREATE TABLE " + DBConst.TABLE_CLUB + " ("
				+ ClubColumns.CLUB_ID + " INTEGER PRIMARY KEY,"
				+ ClubColumns.NAME + " TEXT," + ClubColumns.ADDRESS + " TEXT,"
				+ ClubColumns.CITY + " TEXT," + ClubColumns.PHONE + " TEXT,"
				+ ClubColumns.EMAIL + " TEXT," + ClubColumns.BUSINESSHOURS
				+ " TEXT," + ClubColumns.HOMEPAGEURL + " TEXT,"
				+ ClubColumns.LOGOURL + " TEXT," + ClubColumns.STARTUPICURLS
				+ " TEXT," + ClubColumns.PICURLS + " TEXT,"
				+ ClubColumns.DESCRIPTION + " TEXT," + ClubColumns.LON
				+ " TEXT," + ClubColumns.LAT + " TEXT" + ");");
		db.execSQL("CREATE TABLE " + DBConst.TABLE_COURSE + " ("
				+ CourseColumns.CLUB_SCHEDULE_ID + " INTEGER PRIMARY KEY,"
				+ CourseColumns.CLUB_ID + " INTEGER," + CourseColumns.CLASS_ID
				+ " INTEGER," + CourseColumns.CLASS_NAME + " TEXT,"
				+ CourseColumns.CLASSROOM_ID + " INTEGER,"
				+ CourseColumns.CLASSROOM_NAME + " TEXT,"
				+ CourseColumns.ISALERMED + " INTEGER,"
				+ CourseColumns.COACH_ID + " INTEGER,"
				+ CourseColumns.COACH_NAME + " TEXT,"
				+ CourseColumns.BACKGROUND_COLOR + " TEXT,"
				+ CourseColumns.SCHEDULE_DATE + " TEXT,"
				+ CourseColumns.START_TIME + " TEXT," + CourseColumns.END_TIME
				+ " TEXT," + CourseColumns.CREATED + " TEXT,"
				+ CourseColumns.MODIFIED + " TEXT," + CourseColumns.YEAR
				+ " INTEGER," + CourseColumns.WEEKOFYEAR + " INTEGER" + ");");
		db.execSQL("CREATE TABLE " + DBConst.TABLE_COURSE_FAVORITE + " ("
				+ FavoriteColumns.ID + " INTEGER PRIMARY KEY,"
				+ FavoriteColumns.CLUB_ID + " INTEGER,"
				+ FavoriteColumns.CLASS_ID + " INTEGER,"
				+ FavoriteColumns.CLASS_NAME + " TEXT,"
				+ FavoriteColumns.CLASSROOM_ID + " INTEGER,"
				+ FavoriteColumns.CLASSROOM_NAME + " TEXT,"
				+ FavoriteColumns.ISALERMED + " INTEGER,"
				+ FavoriteColumns.COACH_ID + " INTEGER,"
				+ FavoriteColumns.COACH_NAME + " TEXT,"
				+ FavoriteColumns.BACKGROUND_COLOR + " TEXT,"
				+ FavoriteColumns.SCHEDULE_DATE + " TEXT,"
				+ FavoriteColumns.START_TIME + " TEXT,"
				+ FavoriteColumns.END_TIME + " TEXT," + FavoriteColumns.CREATED
				+ " TEXT," + FavoriteColumns.MODIFIED + " TEXT,"
				+ FavoriteColumns.YEAR + " INTEGER,"
				+ FavoriteColumns.WEEKOFYEAR + " INTEGER" + ");");
		db.execSQL("CREATE TABLE " + DBConst.TABLE_CLASS + " ("
				+ ClassColumns.CLASS_ID + " INTEGER PRIMARY KEY,"
				+ ClassColumns.CLASS_NAME + " INTEGER," + ClassColumns.CLUB_ID
				+ " INTEGER," + ClassColumns.PIC_URLS + " TEXT,"
				+ ClassColumns.VIDEO_THUMBNAIL_PIC + " TEXT,"
				+ ClassColumns.VIDEO_URL + " TEXT," + ClassColumns.DESCRIPTION
				+ " TEXT," + ClassColumns.PREREGISTRATION_PHONE + " TEXT"
				+ ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE " + DBConst.TABLE_COURSE + " ADD "
				+ CourseColumns.ISALERMED + " INTEGER");
		db.execSQL("ALTER TABLE " + DBConst.TABLE_COURSE_FAVORITE + " ADD "
				+ FavoriteColumns.ISALERMED + " INTEGER");
	}

}
