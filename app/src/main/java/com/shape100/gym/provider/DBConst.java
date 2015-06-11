package com.shape100.gym.provider;

import android.provider.BaseColumns;

public final class DBConst {

	public static final String TABLE_ACCOUNT = "account";
	public static final String TABLE_ACCOUNTDETAIL = "accountdetail";
	public static final String TABLE_CLUB = "club";
	public static final String TABLE_COURSE = "course";
	public static final String TABLE_COURSE_FAVORITE = "coursefavorite";
	public static final String TABLE_CLASS = "clazz";

	public interface AccountColumns extends BaseColumns {
		public static final String USER_ID = "user_id";
		public static final String TOKEN = "token";
		public static final String TOKEN_SECRET = "token_secret";
		public static final String SCREEN_NAME = "screen_name";
		// public static final String PASSWORD = "password";
		public static final String USER_TYPE = "user_type";
	}

	public interface AccountDetailColumns extends BaseColumns {
		public static final String USER_ID = "user_id";
		public static final String KEY = "key";
		public static final String VALUE = "value";
	}

	public interface ClubColumns extends BaseColumns {
		public static final String CLUB_ID = "club_id";
		public static final String NAME = "name";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String PHONE = "phone";
		public static final String EMAIL = "email";
		public static final String BUSINESSHOURS = "business_hours";
		public static final String HOMEPAGEURL = "homepage_url";
		public static final String LOGOURL = "logo_url";
		public static final String STARTUPICURLS = "startup_pic_urls";
		public static final String PICURLS = "pic_urls";
		public static final String DESCRIPTION = "description";
		public static final String LON = "lon";
		public static final String LAT = "lat";
	}

	public interface CourseColumns extends BaseColumns {
		public static final String CLUB_SCHEDULE_ID = "club_schedule_id";
		public static final String USER_SCHEDULE_ID = "user_schedule_id";
		public static final String CLASS_ID = "class_id";
		public static final String CLASS_NAME = "class_name";
		public static final String CLUB_ID = "club_id";
		public static final String COACH_ID = "coach_id";
		public static final String COACH_NAME = "coach_name";
		public static final String CLASSROOM_ID = "classroom_id";
		public static final String CLASSROOM_NAME = "classroom_name";
		public static final String BACKGROUND_COLOR = "background_color";
		public static final String SCHEDULE_DATE = "schedule_date";
		public static final String START_TIME = "start_time";
		public static final String END_TIME = "end_time";
		public static final String CREATED = "created";
		public static final String MODIFIED = "modified";
		public static final String YEAR = "year";
		public static final String ISALERMED = "is_alermed";
		public static final String WEEKOFYEAR = "week_of_year";
	}

	public interface ClassColumns extends BaseColumns {
		public static final String CLASS_ID = "class_id";
		public static final String CLASS_NAME = "class_name";
		public static final String CLUB_ID = "club_id";
		public static final String PIC_URLS = "pic_urls";
		public static final String VIDEO_THUMBNAIL_PIC = "video_thumbnail_pic";
		public static final String VIDEO_URL = "video_url";
		public static final String DESCRIPTION = "description";
		public static final String PREREGISTRATION_PHONE = "preregistration_phone";
		public static final String YEAR = "year";
		public static final String WEEKOFYEAR = "week_of_year";
	}

	public interface UpdateColumns extends BaseColumns {
		public static final String HAS_NEW_VERSION = "has_new_version";
		public static final String NEW_VERSION = "new_version";
		public static final String IS_FORCE_UPDATE = "is_force_update";
		public static final String UPDATE_DESCRIPTION = "update_description";
		public static final String DOWNLOAD_URL = "download_url";
	}

	public interface FavoriteColumns extends BaseColumns {
		public static final String ID = "user_schedule_id";
		public static final String CLASS_ID = "class_id";
		public static final String CLASS_NAME = "class_name";
		public static final String CLUB_ID = "club_id";
		public static final String COACH_ID = "coach_id";
		public static final String COACH_NAME = "coach_name";
		public static final String CLASSROOM_ID = "classroom_id";
		public static final String CLASSROOM_NAME = "classroom_name";
		public static final String BACKGROUND_COLOR = "background_color";
		public static final String SCHEDULE_DATE = "schedule_date";
		public static final String START_TIME = "start_time";
		public static final String END_TIME = "end_time";
		public static final String ISALERMED = "is_alermed";
		public static final String CREATED = "created";
		public static final String MODIFIED = "modified";

		public static final String YEAR = "year";
		public static final String WEEKOFYEAR = "week_of_year";

	}

}
