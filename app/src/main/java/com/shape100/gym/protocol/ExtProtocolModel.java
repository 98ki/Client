package com.shape100.gym.protocol;



public class ExtProtocolModel {

	public static final int RESULT_SUCCESS = 0;
	
	public static class ExtResponse {
		public static final String KEY_ERROR = "error";
		public static final String KEY_MSG = "msg";

		public int mResult;
		public String mMsg;
	}
	
	public static class ExtTokenResponse{
		public static final String KEY_TOKEN = "oauth_token";
		public static final String KEY_TOKENSECRET = "oauth_token_secret";
		public static final String KEY_USERID = "user_id";
		public static final String KEY_SCREENNAME = "screen_name";
		
		public String mToken;
		public String mTokenSecret;
		public long mUserId;
		public String mScreenName;		
	}
	

/**
 *   annotation beacuse Schedule info save to DB, use CourseInfo mode directly.	
 */
//	public static class ExtScheduleResponse{
//		public static final String CLASS_ID = "class_id";
//		public static final String CLASS_NAME = "class_name";
//		public static final String CLUB_ID = "club_id";
//		public static final String COACH_ID = "coach_id";
//		public static final String COACH_NAME = "coach_name";	
//		public static final String CLASSROOM_ID = "classroom_id";
//		public static final String CLASSROOM_NAME = "classroom_name";
//		public static final String BACKGROUND_COLOR = "background_color";
//		public static final String DAY_OF_WEEK = "day_of_week";
//		public static final String START_TIME = "start_time";
//		public static final String END_TIME = "end_time";
//		public static final String CREATED = "created";
//		public static final String MODIFIED = "modified";
//		// ...
//		public int classId;
//		public String className;
//		public int clubId;
//		public int coachId;
//		public String coachName;
//		public int classRoomId;
//		public String classRoomName;
//		public String backgroundColor;
//		public int dayOfWeek;
//		public Date startTime;
//		public Date endTime;
//		public Date created;
//		public Date modified;
//	}
	
}
