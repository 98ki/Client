package com.shape100.gym.protocol;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com._98ki.util.TimeUtils;
import com.shape100.gym.Logger;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.model.ClassBean;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.model.CommentsData;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.model.Dynamic;
import com.shape100.gym.model.Geo;
import com.shape100.gym.model.Status;
import com.shape100.gym.model.UpdateBean;
import com.shape100.gym.model.User;
import com.shape100.gym.model.UserBean;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.model.UserOtherInfo;
import com.shape100.gym.protocol.ExtProtocolModel.ExtResponse;
import com.shape100.gym.protocol.ExtProtocolModel.ExtTokenResponse;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.ClassUtil;
import com.shape100.gym.provider.CourseFavoriteUtil;
import com.shape100.gym.provider.CourseUtil;
import com.shape100.gym.provider.DBConst.ClassColumns;
import com.shape100.gym.provider.DBConst.ClubColumns;
import com.shape100.gym.provider.DBConst.CourseColumns;
import com.shape100.gym.provider.DBConst.FavoriteColumns;
import com.shape100.gym.provider.DBConst.UpdateColumns;

/**
 * 构造数据模型 Protocol Util
 * 
 * @author zpy
 * @data 2014-11-6 下午8:18:57
 * @version: V1.0
 */
public class ExtProtocolUtil {
	private static final Logger log = Logger.getLogger("ExtProtocolUtil");

	public static ExtResponse parseResponse(InputStream input) throws Exception {
		ExtResponse bean = new ExtResponse();
		JSONObject jsonObj = getJSONObject(input);
		bean.mResult = jsonObj.getInt(ExtResponse.KEY_ERROR);
		bean.mMsg = jsonObj.has(ExtResponse.KEY_MSG) ? jsonObj
				.getString(ExtResponse.KEY_MSG) : "";
		return bean;
	}

	public static ExtTokenResponse parseTokenResponse(InputStream input)
			throws Exception {
		ExtTokenResponse bean = new ExtTokenResponse();
		JSONObject jsonObj = getJSONObject(input);

		bean.mToken = jsonObj.getString(ExtTokenResponse.KEY_TOKEN);
		bean.mTokenSecret = jsonObj.getString(ExtTokenResponse.KEY_TOKENSECRET);
		bean.mUserId = jsonObj.getLong(ExtTokenResponse.KEY_USERID);
		bean.mScreenName = jsonObj.getString(ExtTokenResponse.KEY_SCREENNAME);

		return bean;
	}

	public static AccountDetailBean parseVerifyResponse(InputStream input)
			throws Exception {
		AccountDetailBean accountDetailBean = new AccountDetailBean();
		JSONObject jsonObj = getJSONObject(input);

		accountDetailBean.setUserId(jsonObj.optInt("user_id"));
		accountDetailBean.setName(jsonObj.optString("name"));
		accountDetailBean.setScreenName(jsonObj.optString("screen_name"));
		accountDetailBean.setLocation(jsonObj.optString("location"));
		accountDetailBean.setDescription(jsonObj.optString("description"));
		accountDetailBean.setProfileImageUrl(jsonObj
				.optString("profile_image_url"));
		accountDetailBean.setUrl(jsonObj.optString("url"));
		accountDetailBean.setProtect(jsonObj.optString("protected"));
		accountDetailBean.setFollowersCount(jsonObj.optInt("followers_count"));
		accountDetailBean.setFriendsCount(jsonObj.optInt("friends_count"));
		accountDetailBean.setFavoritesCount(jsonObj.optInt("favorites_count"));
		accountDetailBean.setUtcOffset(jsonObj.optInt("utc_offset"));
		accountDetailBean.setTimeZone(jsonObj.optString("time_zone"));
		accountDetailBean.setStatusesCount(jsonObj.optInt("statuses_count"));
		accountDetailBean.setFollowing(jsonObj.optBoolean("following"));
		accountDetailBean.setNotifications(jsonObj.optBoolean("notifications"));
		accountDetailBean.setGender(jsonObj.optInt("gender"));
		accountDetailBean.setBirthday(jsonObj.optString("birthday"));
		accountDetailBean.setHeight(jsonObj.optInt("height"));
		accountDetailBean.setWeight((float) jsonObj.optDouble("weight"));

		String club_id = jsonObj.optString("club_id");
		if (club_id.equals("")) {
			accountDetailBean.setClubId(0);
		} else {
			accountDetailBean.setClubId(jsonObj.optInt("club_id"));
		}
		return accountDetailBean;
	}

	public static AccountDetailBean parseAccountDetailBean(JSONObject jsonObj)
			throws Exception {
		AccountDetailBean accountDetailBean = new AccountDetailBean();
		accountDetailBean.setUserId(jsonObj.getInt("user_id"));
		accountDetailBean.setName(jsonObj.getString("name"));
		accountDetailBean.setScreenName(jsonObj.getString("screen_name"));
		accountDetailBean.setLocation(jsonObj.getString("location"));
		accountDetailBean.setDescription(jsonObj.getString("description"));
		accountDetailBean.setProfileImageUrl(jsonObj
				.getString("profile_image_url"));
		accountDetailBean.setUrl(jsonObj.getString("url"));
		accountDetailBean.setProtect(jsonObj.getString("protected"));
		accountDetailBean.setFollowersCount(jsonObj.getInt("followers_count"));
		accountDetailBean.setFriendsCount(jsonObj.getInt("friends_count"));
		accountDetailBean.setFavoritesCount(jsonObj.getInt("favorites_count"));
		accountDetailBean.setUtcOffset(jsonObj.getInt("utc_offset"));
		accountDetailBean.setTimeZone(jsonObj.getString("time_zone"));
		accountDetailBean.setStatusesCount(jsonObj.getInt("statuses_count"));
		accountDetailBean.setFollowing(jsonObj.getBoolean("following"));
		accountDetailBean.setNotifications(jsonObj.getBoolean("notifications"));
		accountDetailBean.setCertification(jsonObj.getString("certification"));

		try {
			accountDetailBean.setGender(jsonObj.getInt("gender"));
		} catch (Exception e) {
			accountDetailBean.setGender(2);
		}
		try {
			accountDetailBean.setBirthday(jsonObj.getString("birthday"));
		} catch (Exception e) {
			accountDetailBean.setBirthday("");
		}
		try {
			accountDetailBean.setHeight(jsonObj.getInt("height"));
		} catch (Exception e) {
			accountDetailBean.setHeight(0);
		}
		
		try {
			accountDetailBean.setWeight((float) jsonObj.getDouble("weight"));
		} catch (Exception e) {
			accountDetailBean.setWeight(0);
		}
		try {
			String club_id = jsonObj.getString("club_id");
			if (club_id.equals("")) {
				accountDetailBean.setClubId(0);
			} else {
				accountDetailBean.setClubId(jsonObj.getInt("club_id"));
			}
		} catch (Exception e) {

		}
		return accountDetailBean;
	}

	public static ArrayList<UserBean> parseFavoriteCoachResponse(
			InputStream input) throws Exception {
		ArrayList<UserBean> coachList = new ArrayList<UserBean>();
		log.d(input.toString());
		JSONArray jsonArray = getJSONArray(input);
		JSONObject jsonObj;
		UserBean userBean;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObj = jsonArray.getJSONObject(i);

			userBean = new UserBean();
			userBean.setUserId(jsonObj.getInt("user_id"));
			userBean.setName(jsonObj.getString("name"));
			userBean.setScreenName(jsonObj.getString("screen_name"));
			userBean.setLocation(jsonObj.getString("location"));
			userBean.setCertification(jsonObj.getString("certification"));
			userBean.setDescription(jsonObj.getString("description"));
			userBean.setProfileImageUrl(jsonObj.getString("profile_image_url"));
			userBean.setUrl(jsonObj.getString("url"));
			userBean.setProtect(jsonObj.getBoolean("protected"));
			userBean.setFollowersCount(jsonObj.getInt("followers_count"));
			userBean.setFriendsCount(jsonObj.getInt("friends_count"));
			userBean.setFavoritesCount(jsonObj.getInt("favorites_count"));
			userBean.setUtcOffset(jsonObj.getString("utc_offset"));
			userBean.setTimeZone(jsonObj.getString("time_zone"));
			userBean.setStatusesCount(jsonObj.getInt("statuses_count"));
			userBean.setFollowing(jsonObj.getBoolean("following"));
			userBean.setNotifications(jsonObj.getBoolean("notifications"));
			// userBean.setGender(jsonObj.getInt("gender"));
			// userBean.setBirthday(jsonObj.getString("birthday"));
			// userBean.setHeight(jsonObj.getInt("height"));
			// userBean.setWeight((float) jsonObj.getDouble("weight"));
			// String club_id = jsonObj.getString("club_id");
			// if (club_id.equals("")) {
			// userBean.setClubId(0);
			// } else {
			// userBean.setClubId(jsonObj.getInt("club_id"));
			// }
			coachList.add(userBean);
		}
		return coachList;
	}

	/**
	 * Parse club list information, club info don't cache
	 * 
	 * @throw
	 * @return ArrayList<CourseBean>
	 */
	public static ArrayList<ClubBean> parseClub(InputStream input) {
		ArrayList<ClubBean> list = new ArrayList<ClubBean>();
		JSONArray jsonArray = null;
		try {
			jsonArray = getJSONArray(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = null;
			try {
				jsonObj = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			int id = jsonObj.optInt("id");
			String name = jsonObj.optString("name");
			String address = jsonObj.optString("address");
			String city = jsonObj.optString("city");
			String phone = jsonObj.optString("phone");
			String email = jsonObj.optString("email");
			String businessHours = jsonObj.optString("business_hours");
			String homepageUrl = jsonObj.optString("homepage_url");
			String logoUrl = jsonObj.optString("logo_url");

			JSONArray tempJSONArray = jsonObj.optJSONArray(ClubColumns.PICURLS);
			StringBuilder picUrls = new StringBuilder();
			if (tempJSONArray != null) {
				for (int j = 0; j < tempJSONArray.length(); j++) {
					String temp = tempJSONArray.optJSONObject(j).optString(
							"thumbnail_pic")
							+ ",";
					picUrls.append(temp);
				}
				if (picUrls.length() != 0) {
					picUrls.deleteCharAt(picUrls.length() - 1);
				}
			}
			log.d("picUrls.toString():" + picUrls.toString());
			String description = jsonObj.optString("description");
			String lon = jsonObj.optString("lon");
			String lat = jsonObj.optString("lat");

			ClubBean bean = new ClubBean(id, name, address, city, phone, email,
					businessHours, homepageUrl, logoUrl, picUrls.toString(),
					description, lon, lat);
			list.add(bean);
		}
		return list;

	}

	/**
	 * Parse parseClubSchedule information, save course list to course table.
	 * 
	 * @throw
	 * @return ArrayList<CourseBean>
	 */
	public static ArrayList<CourseBean> parseClubSchedule(InputStream input)
			throws Exception {
		log.e("parse club Schedule start!");
		ArrayList<CourseBean> list = new ArrayList<CourseBean>();

		// parse Json
		JSONArray jsonArray = getJSONArray(input);

		for (int i = 0; i < jsonArray.length(); i++) {
			// get date
			CourseBean bean = new CourseBean();
			JSONObject obj = jsonArray.getJSONObject(i);

			// data formart
			String date = obj.getString(CourseColumns.SCHEDULE_DATE);
			int weekOfYear = TimeUtils.getWeekOfYearByString(date);
			int year = TimeUtils.getYearByString(date);

			// Add the parse course bean to list

			bean.setId(obj.getInt(CourseColumns.CLUB_SCHEDULE_ID));
			bean.setClubId(obj.getInt(CourseColumns.CLUB_ID));
			bean.setClassId(obj.getInt(CourseColumns.CLASS_ID));
			bean.setClassRoomId(obj.getInt(CourseColumns.CLASSROOM_ID));
			bean.setCoachId(obj.getInt(CourseColumns.COACH_ID));
			bean.setClassName(obj.getString(CourseColumns.CLASS_NAME));
			bean.setClassRoomName(obj.getString(CourseColumns.CLASSROOM_NAME));
			bean.setCoachName(obj.getString(CourseColumns.COACH_NAME));
			bean.setScheduleDate(obj.getString(CourseColumns.SCHEDULE_DATE));
			bean.setBackgroundColor(obj
					.getString(CourseColumns.BACKGROUND_COLOR));
			bean.setStartTime(obj.getString(CourseColumns.START_TIME));
			bean.setEndTime(obj.getString(CourseColumns.END_TIME));
			bean.setCreated(obj.getString(CourseColumns.CREATED));
			bean.setModified(obj.getString(CourseColumns.MODIFIED));
			bean.setWeekOfYear(weekOfYear);
			bean.setYear(year);
			list.add(bean);

		}
		CourseUtil.saveCourse(list);
		log.e("parse CLub schedule end!");
		return list;

	}

	/**
	 * Parse favorite information, save favorite list to favorite table.
	 * 
	 * @throw
	 * @return ArrayList<CourseBean>
	 */
	public static ArrayList<CourseBean> parseFavoriteSchedule(InputStream input)
			throws Exception {

		ArrayList<CourseBean> list = new ArrayList<CourseBean>();

		// parse Json
		JSONArray jsonArray = getJSONArray(input);

		for (int i = 0; i < jsonArray.length(); i++) {
			// Add the parse course bean to list
			CourseBean bean = new CourseBean();
			JSONObject obj = jsonArray.getJSONObject(i);

			// data formart
			String date = obj.getString(FavoriteColumns.SCHEDULE_DATE);
			int weekOfYear = TimeUtils.getWeekOfYearByString(date);
			int year = TimeUtils.getYearByString(date);

			bean.setId(obj.getInt(FavoriteColumns.ID));
			// bean.setClubId(obj.getInt(FavoriteColumns.CLUB_ID));
			bean.setClassId(obj.getInt(FavoriteColumns.CLASS_ID));
			bean.setClassRoomId(obj.getInt(FavoriteColumns.CLASSROOM_ID));
			bean.setCoachId(obj.getInt(FavoriteColumns.COACH_ID));
			bean.setClassName(obj.getString(FavoriteColumns.CLASS_NAME));
			bean.setClassRoomName(obj.getString(FavoriteColumns.CLASSROOM_NAME));
			bean.setCoachName(obj.getString(FavoriteColumns.COACH_NAME));
			bean.setScheduleDate(obj.getString(FavoriteColumns.SCHEDULE_DATE));
			bean.setBackgroundColor(obj
					.getString(FavoriteColumns.BACKGROUND_COLOR));
			bean.setStartTime(obj.getString(FavoriteColumns.START_TIME));
			bean.setEndTime(obj.getString(FavoriteColumns.END_TIME));
			bean.setCreated(obj.getString(FavoriteColumns.CREATED));
			bean.setModified(obj.getString(FavoriteColumns.MODIFIED));
			bean.setYear(year);
			bean.setWeekOfYear(weekOfYear);

			list.add(bean);

		}
		CourseFavoriteUtil.saveFavorite(list);
		log.e("parse Favorite schedule end! save to favorite table succeed!,Favorite.size():"
				+ list.size());
		return list;

	}

	/**
	 * Parse Class detail information
	 * 
	 * @throw
	 * @return ArrayList<CourseBean>
	 */
	public static void parseClass(ClassBean bean, InputStream input)
			throws Exception {

		JSONObject obj = getJSONObject(input);
		// Add the parse course bean to list
		JSONArray jsonArray = obj.getJSONArray(ClassColumns.PIC_URLS);
		if (jsonArray.length() != 0) {
			StringBuffer picUrls = new StringBuffer();
			for (int i = 0; i < jsonArray.length(); i++) {
				picUrls.append(jsonArray.getJSONObject(i).getString(
						"thumbnail_pic")
						+ ",");
			}
			picUrls.deleteCharAt(picUrls.length() - 1);
			bean.setPicUrls(picUrls.toString());
		}
		bean.setVideo_thumbnail_pic(obj
				.getString(ClassColumns.VIDEO_THUMBNAIL_PIC));
		bean.setVideo_url(obj.getString(ClassColumns.VIDEO_URL));
		bean.setDescription(obj.getString(ClassColumns.DESCRIPTION));
		bean.setPreregistration_phone(obj
				.getString(ClassColumns.PREREGISTRATION_PHONE));
		ClassUtil.saveClass(bean);

		// parse Coach
		JSONObject coachJsonObject = obj.getJSONObject("coach");
		// save to db;
		AccountDetailBean coachBean = parseAccountDetailBean(coachJsonObject);
		AccountDetailUtil.saveAccountDetail(coachBean);

	}

	/**
	 * Parse Class detail list information
	 * 
	 * @throw
	 * @return ArrayList<CourseBean>
	 */
	public static ArrayList<ClassBean> parseFavoriteClassResponse(
			InputStream input) throws Exception {
		ArrayList<ClassBean> classList = new ArrayList<ClassBean>();
		JSONArray jsonArray = getJSONArray(input);
		JSONObject obj;
		ClassBean classBean;
		for (int i = 0; i < jsonArray.length(); i++) {
			obj = jsonArray.getJSONObject(i);
			ClassBean bean = new ClassBean();

			// Add the parse course bean to list
			JSONArray picJsonArray = obj.getJSONArray(ClassColumns.PIC_URLS);
			StringBuffer picUrls = new StringBuffer();
			for (int j = 0; j < picJsonArray.length(); j++) {
				picUrls.append(picJsonArray.getJSONObject(j).getString(
						"thumbnail_pic")
						+ ",");
			}
			if (picUrls.length() != 0) {
				picUrls.deleteCharAt(picUrls.length() - 1);
			}
			bean.setClassId(obj.getInt(ClassColumns.CLASS_ID));
			bean.setClassName(obj.getString(ClassColumns.CLASS_NAME));
			bean.setPicUrls(picUrls.toString());
			bean.setVideo_thumbnail_pic(obj
					.getString(ClassColumns.VIDEO_THUMBNAIL_PIC));
			bean.setVideo_url(obj.getString(ClassColumns.VIDEO_URL));
			bean.setDescription(obj.getString(ClassColumns.DESCRIPTION));
			bean.setPreregistration_phone(obj
					.getString(ClassColumns.PREREGISTRATION_PHONE));

			classList.add(bean);

		}

		return classList;
	}

	/**
	 * 
	 * parseUpdate
	 * 
	 * @param InputStream
	 */
	public static UpdateBean parseUpdate(InputStream input) throws Exception {
		JSONObject obj = getJSONObject(input);
		UpdateBean bean = new UpdateBean();
		bean.setDownloadUrl(obj.optString(UpdateColumns.DOWNLOAD_URL));
		bean.setHasNewVersion(obj.optInt(UpdateColumns.HAS_NEW_VERSION));
		bean.setIsForceUpdate(obj.optInt(UpdateColumns.IS_FORCE_UPDATE));
		bean.setNewVersion(obj.optString(UpdateColumns.NEW_VERSION));
		bean.setUpdateDescription(obj
				.optString(UpdateColumns.UPDATE_DESCRIPTION));

		return bean;
	}

	/**
	 * 
	 * parseUploadPic
	 * 
	 * @param
	 */
	public static ArrayList<String> parseUploadPic(InputStream input)
			throws Exception {
		JSONObject obj = getJSONObject(input);
		ArrayList<String> uploadPicList = new ArrayList<String>();
		uploadPicList.add(obj.optString("pic_id"));
		uploadPicList.add(obj.optString("put_url"));
		return uploadPicList;
	}

	/**
	 * 
	 * parseUploadPicConfirm
	 * 
	 * @param
	 */
	public static ArrayList<String> parseUploadPicConfirm(InputStream input)
			throws Exception {
		JSONObject obj = getJSONObject(input);
		ArrayList<String> uploadPicList = new ArrayList<String>();
		uploadPicList.add(obj.optString("pic_id"));
		uploadPicList.add(obj.optString("thumbnail_pic"));

		return uploadPicList;
	}

	/**
	 * 注销手机号，测试用
	 * 
	 * @author yupu
	 * @date 2015年1月21日
	 */
	public static ArrayList<String> parserRemovePhone(InputStream input)
			throws Exception {
		JSONObject obj = getJSONObject(input);
		ArrayList<String> res = new ArrayList<String>();
		res.add(obj.optInt("error") + "");
		res.add(obj.optString("msg"));
		return res;
	}

	// [{"text":"http:\/\/api.dev.shape100.com\/url\/16\n0.0----0.0","truncated":false,
	// "created_at":"Wed Jan 21 23:05:50 +0800 2015","in_reply_to_status_id":"",
	// "source":"<a
	// href=\"http:\/\/sppkg.oss-cn-beijing.aliyuncs.com\/android\/alpha\/YXClient.apk\"
	// rel=\"nofollow\">\u6709\u578b\u5065\u8eab(Android)<\/a>","id":1000000114,"in_reply_to_user_id":"","in_reply_to_screen_name":"",
	// "geo":{"type":"Point","coordinates":[0,0]},"favorited":false,"attachments":[],

	// "user":{"user_id":1000000268,"name":"\u5c0f\u9c7c","screen_name":"sp1421810247984747",
	// "location":"","certification":"","description":"","profile_image_url":"",
	// "url":"","protected":false,"followers_count":0,"friends_count":0,
	// "created_at":"Wed Jan 21 11:17:27 +0800 2015","favorites_count":0,
	// "utc_offset":"0","time_zone":"UTC","statuses_count":2,"following":true,
	// "notifications":false,"gender":2,"birthday":"1996-01-01","height":185,"weight":75,
	// "club_id":1000080833}},{"text":"http:\/\/api.dev.shape100.com\/url\/16\n0.0----0.0","truncated":false,"created_at":"Wed
	// Jan 21 23:01:43 +0800 2015","in_reply_to_status_id":"","source":"<a
	// href=\"http:\/\/sppkg.oss-cn-beijing.aliyuncs.com\/android\/alpha\/YXClient.apk\"
	// rel=\"nofollow\">\u6709\u578b\u5065\u8eab(Android)<\/a>","id":1000000113,"in_reply_to_user_id":"","in_reply_to_screen_name":"","geo":{"type":"Point","coordinates":[0,0]},"favorited":false,"attachments":[],"user":{"user_id":1000000268,"name":"\u5c0f\u9c7c","screen_name":"sp1421810247984747","location":"","certification":"","description":"","profile_image_url":"","url":"","protected":false,"followers_count":0,"friends_count":0,"created_at":"Wed
	// Jan 21 11:17:27 +0800
	// 2015","favorites_count":0,"utc_offset":"0","time_zone":"UTC","statuses_count":2,"following":true,"notifications":false,"gender":2,"birthday":"1996-01-01","height":185,"weight":75,"club_id":1000080833}}]

	public static ArrayList<CommentsData> parseFriendsTimeLine(InputStream input)
			throws Exception {
		ArrayList<CommentsData> dynamics = new ArrayList<CommentsData>();
		JSONArray array = getJSONArray(input);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			CommentsData dynamic = new CommentsData();
			dynamic.setText(obj.optString("text"));
			dynamic.setTruncated(obj.optBoolean("truncated"));
			dynamic.setCreated_at(obj.optString("created_at"));
			dynamic.setIn_reply_to_status_id(obj
					.optLong("in_reply_to_status_id"));
			dynamic.setSource(obj.optString("source"));
			dynamic.setIn_reply_to_user_id(obj.optLong("in_reply_to_user_id"));
			dynamic.setIn_reply_to_screen_name(obj
					.optString("in_reply_to_screen_name"));
			dynamic.setId(obj.optInt("id"));
			dynamic.setFavorited(obj.optBoolean("favorited"));
			dynamic.setClub_id(obj.optLong("club_id"));
			dynamic.setClub_name(obj.optString("club_name"));
			dynamic.setClass_id(obj.optLong("class_id"));
			dynamic.setClass_name(obj.optString("class_name"));
			dynamic.setCoach_id(obj.optLong("coach_id"));
			dynamic.setCoach_name(obj.optString("coach_name"));
			dynamic.setCalorie(obj.optInt("calorie"));
			dynamic.setDistance(obj.optInt("distance"));
			dynamic.setDuration(obj.optInt("duration"));
			dynamic.setStrincomments_count(obj.optInt("comments_count"));
			dynamic.setLikes_count(obj.optInt("likes_count"));
			dynamic.setLiked(obj.optBoolean("liked"));

			JSONArray arrayPic = obj.optJSONArray("pic_urls");
			if (arrayPic != null) {
				List<String> pics = new ArrayList<String>();
				for (int j = 0; j < arrayPic.length(); j++) {
					JSONObject ob = arrayPic.getJSONObject(j);
					String pic = ob.getString("thumbnail_pic");
					pics.add(pic);
				}
				dynamic.setPic_urls(pics);
			}

			JSONObject objGeo = obj.optJSONObject("geo");
			if (objGeo != null) {
				Geo geo = new Geo();
				geo.setType(objGeo.getString("type"));
				JSONArray arrayCoodinates = objGeo.getJSONArray("coordinates");
				Float[] fl = new Float[2];
				for (int j = 0; j < arrayCoodinates.length(); j++) {
					fl[j] = (float) arrayCoodinates.getDouble(j);
				}
				geo.setCoordinates(fl);
				dynamic.setGeo(geo);
			}

			JSONObject objUser = obj.optJSONObject("user");
			dynamic.setUser(getUser(objUser));
			dynamics.add(dynamic);
		}

		return dynamics;
		// "location":"","certification":"","description":"","profile_image_url":"",
		// "url":"","protected":false,"followers_count":0,"friends_count":0,
		// "created_at":"Wed Jan 21 11:17:27 +0800 2015","favorites_count":0,
		// "utc_offset":"0","time_zone":"UTC","statuses_count":2,"following":true,
		// "notifications":false,"gender":2,"birthday":"1996-01-01","height":185,"weight":75,
		// "club_id":1000080833}},
	}

	public static ArrayList<User> parseUsers(InputStream input)
			throws Exception {
		ArrayList<User> users = new ArrayList<User>();
		JSONArray array = getJSONArray(input);
		for (int i = 0; i < array.length(); i++) {
			User user = getUser(array.getJSONObject(i));
			users.add(user);
		}
		return users;
	}

	/**
	 * 解析user
	 * 
	 * @author yupu
	 * @date 2015年3月12日
	 */
	public static User getUser(JSONObject objUser) {
		User user = new User();
		user.setUserId(objUser.optInt("user_id"));
		user.setName(objUser.optString("name"));
		user.setScreenName(objUser.optString("screen_name"));
		user.setLocation(objUser.optString("location"));
		user.setCreated_at(objUser.optString("created_at"));
		user.setCertification(objUser.optString("certification"));
		user.setDescription(objUser.optString("description"));
		user.setProfileImageUrl(objUser.optString("profile_image_url"));
		user.setUrl(objUser.optString("url"));
		user.setProtect(objUser.optBoolean("protected"));
		user.setFollowersCount(objUser.optInt("followers_count"));
		user.setFriendsCount(objUser.optInt("friends_count"));
		user.setFollwingsCount(objUser.optInt("followings_count"));
		user.setUtcOffset(objUser.optString("utc_offset"));
		user.setTimeZone(objUser.optString("time_zone"));
		user.setStatusesCount(objUser.optInt("statuses_count"));
		user.setFollowing(objUser.optBoolean("following"));
		user.setNotifications(objUser.optBoolean("notifications"));
		user.setGender(objUser.optInt("gender"));
		user.setBirthday(objUser.optString("birthday"));
		user.setHeight(objUser.optInt("height"));
		user.setWeight((float) objUser.optDouble("weight"));
		user.setClubId(objUser.optInt("club_id"));
		return user;
	}

	public static Status getStatus(JSONObject object) {
		Status status = new Status();
		status.setText(object.optString("text"));
		status.setId(object.optInt("id"));
		status.setTruncated(object.optBoolean("truncated"));
		status.setCreated_at(object.optString("created_at"));
		status.setIn_reply_to_status_id(object.optLong("in_reply_to_status_id"));
		status.setSource(object.optString("source"));
		status.setIn_reply_to_user_id(object.optLong("in_reply_to_user_id"));
		status.setIn_reply_to_screen_name(object
				.optString("in_reply_to_screen_name"));
		status.setIn_reply_to_fullname(object.optString("in_reply_to_fullname"));
		status.setFavorited(object.optBoolean("favorited"));
		status.setClub_id(object.optLong("club_id"));
		status.setClub_name(object.optString("club_name"));
		status.setClass_id(object.optLong("class_id"));
		status.setClass_name(object.optString("class_name"));
		status.setCoach_id(object.optLong("coach_id"));
		status.setCoach_name(object.optString("coach_name"));
		status.setCalorie(object.optInt("calorie"));
		status.setDistance(object.optInt("distance"));
		status.setDuration(object.optInt("duration"));
		status.setComments_count(object.optInt("strincomments_count"));
		status.setLikes_count(object.optInt("likes_count"));
		status.setLiked(object.optBoolean("liked"));
		return status;
	}

	// [{"text":"\u4f60\u597d","id":1000000686,"truncated":false,"created_at":"Thu Mar 12 10:49:05 +0800 2015",
	// "in_reply_to_status_id":1000000685,"source":"<a
	// href=\"http:\/\/sppkg.oss-cn-beijing.aliyuncs.com\/android\/alpha\/YXClient.apk\"
	// rel=\"nofollow\">\u6709\u578b\u5065\u8eab(Android)<\/a>","in_reply_to_user_id":1000000326,
	// "in_reply_to_screen_name":"yx142597747592","favorited":false,"club_id":0,"club_name":"",
	// "class_id":0,"class_name":"","coach_id":0,"coach_name":"","calorie":0,
	// "distance":0,"duration":0,"user":{"user_id":1000000326,"name":"\u5927\u54e5",
	// "screen_name":"yx142597747592","location":"","certification":"","description":"",
	// "profile_image_url":"http:\/\/img01.shape100.com\/sns\/square\/1000000326b9d6d580de89dbe7.jpg",
	// "url":"","protected":false,"followers_count":0,"friends_count":0,"created_at":"Tue
	// Mar 10 16:51:15 +0800
	// 2015","favorites_count":0,"utc_offset":"0","time_zone":"UTC","statuses_count":10,
	// "following":true,"notifications":false,"gender":1,"birthday":
	// "1992-11-17","height":185,"weight":78,"club_id":1000080833}}]
	public static ArrayList<CommentsData> parseComments(InputStream input)
			throws Exception {
		ArrayList<CommentsData> commentsDatas = new ArrayList<CommentsData>();
		JSONArray arrays = getJSONArray(input);
		for (int i = 0; i < arrays.length(); i++) {
			CommentsData commentsData = new CommentsData();
			JSONObject object = arrays.getJSONObject(i);
			commentsData.setText(object.optString("text"));
			commentsData.setId(object.optInt("id"));
			commentsData.setTruncated(object.optBoolean("truncated"));
			commentsData.setCreated_at(object.optString("created_at"));
			commentsData.setIn_reply_to_status_id(object
					.optLong("in_reply_to_status_id"));
			commentsData.setSource(object.optString("source"));
			commentsData.setIn_reply_to_user_id(object
					.optLong("in_reply_to_user_id"));
			commentsData.setIn_reply_to_screen_name(object
					.optString("in_reply_to_screen_name"));
			commentsData.setIn_reply_to_fullname(object
					.optString("in_reply_to_fullname"));
			commentsData.setFavorited(object.optBoolean("favorited"));
			commentsData.setClub_id(object.optLong("club_id"));
			commentsData.setClub_name(object.optString("club_name"));
			commentsData.setClass_id(object.optLong("class_id"));
			commentsData.setClass_name(object.optString("class_name"));
			commentsData.setCoach_id(object.optLong("coach_id"));
			commentsData.setCoach_name(object.optString("coach_name"));
			commentsData.setCalorie(object.optInt("calorie"));
			commentsData.setDistance(object.optInt("distance"));
			commentsData.setDuration(object.optInt("duration"));
			commentsData.setStrincomments_count(object
					.optInt("strincomments_count"));
			commentsData.setLikes_count(object.optInt("likes_count"));
			commentsData.setLiked(object.optBoolean("liked"));

			JSONObject objUser = object.optJSONObject("user");
			commentsData.setUser(getUser(objUser));
			commentsDatas.add(commentsData);
		}
		return commentsDatas;
		// "class_id":0,"class_name":"","coach_id":0,"coach_name":"","calorie":0,
		// "distance":0,"duration":0,"user":{"user_id":1000000326,"name":"\u5927\u54e5",
		// "screen_name":"yx142597747592","location":"","certification":"","description":"",
		// "profile_image_url":"http:\/\/img01.shape100.com\/sns\/square\/1000000326b9d6d580de89dbe7.jpg",
		// "url":"","protected":false,"followers_count":0,"friends_count":0,"created_at":"Tue
	}

	/**
	 * 解析自己用户信息
	 * 
	 * @author yupu
	 * @throws Exception
	 * @date 2015年3月19日
	 */
	public static UserInfo parseUserShowSelf(InputStream input)
			throws Exception {
		UserInfo userInfo = new UserInfo();
		JSONObject obj = getJSONObject(input);
		User user = getUser(obj);
		userInfo.setUser(user);
		JSONObject object = obj.optJSONObject("status");
		Status status = getStatus(object);
		userInfo.setStatus(status);
		return userInfo;
	}

	public static UserInfo parseUserShowSelf(JSONObject obj) throws Exception {
		UserInfo userInfo = new UserInfo();
		User user = getUser(obj);
		userInfo.setUser(user);
		JSONObject object = obj.optJSONObject("status");
		if (object != null) {
			Status status = getStatus(object);
			userInfo.setStatus(status);
		}
		return userInfo;
	}

	/**
	 * 解析关注；列表
	 * 
	 * @author yupu
	 * @date 2015年3月20日
	 */
	public static ArrayList<UserInfo> parseUserInfoSelfList(InputStream input)
			throws Exception {
		ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
		JSONArray array = getJSONArray(input);
		for (int i = 0; i < array.length(); i++) {
			UserInfo info = parseUserShowSelf(array.optJSONObject(i));
			userInfos.add(info);
		}
		return userInfos;
	}

	/**
	 * 解析t他人信息
	 * 
	 * @author yupu
	 * @throws Exception
	 * @date 2015年3月19日
	 */
	// {"location":"Beijing,Beijing,China","statuses_count":0,"url":""
	// ,"utc_offset":"0","time_zone":"UTC","favorites_count":0,
	// "certification":"拉丁舞|普拉提","following":true,"protected":false,
	// "notifications":false,"friends_count":0,"description":"","name":"李龙",

	public static UserOtherInfo parseUserShowOther(InputStream input)
			throws Exception {
		UserOtherInfo info = new UserOtherInfo();
		JSONObject object = getJSONObject(input);
		info.setLocation(object.optString("location"));
		info.setStatuses_count(object.optInt("statuses_count"));
		info.setUrl(object.optString("url"));
		info.setUtcOffset(object.optInt("utc_offset"));
		info.setTimeZone(object.optString("time_zone"));
		info.setFollowers_count(object.optInt("followers_count"));
		info.setCertification(object.optString("certification"));
		info.setFollowing(object.optBoolean("following"));
		info.setProtect(object.optBoolean("protected"));
		info.setNotifications(object.optBoolean("notifications"));
		info.setFriendsCount(object.optInt("friends_count"));
		info.setDescription(object.optString("description"));
		info.setName(object.optString("name"));
		info.setCreated_at(object.optString("created_at"));
		info.setScreenName(object.optString("screen_name"));
		info.setUserId(object.optLong("user_id"));
		info.setFavoritesCount(object.optInt("favorites_count"));
		info.setProfileImageUrl(object.optString("profile_image_url"));
		info.setFollowings_count(object.optInt("followings_count"));
		return info;

	}

	// "created_at":"Thu Oct 30 20:50:20 +0800 2014","screen_name":"lilong",
	// "user_id":1000000068,"followers_count":3,
	// "profile_image_url":"http:\/\/img01.shape100.com\/sns\/square\/1000000068bb97920243f623c7.jpg"}
	/**
	 * 
	 * getJSONObject
	 * 
	 * @param
	 */
	public static JSONObject getJSONObject(InputStream input) throws Exception {

		JSONObject jsonObject = null;

		StringBuilder builder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(input));

		for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
				.readLine()) {
			builder.append(s);
		}

		if (Logger.DBG) {
			log.d("getJSONObject, the http response content: " + builder);
		}

		jsonObject = new JSONObject(builder.toString());

		return jsonObject;
	}

	/**
	 * 
	 * getJSONArray
	 * 
	 * @param
	 */
	public static JSONArray getJSONArray(InputStream input) throws Exception {
		JSONArray jsonArray = null;
		
		StringBuilder builder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(input));

		for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
				.readLine()) {
			builder.append(s);
		}

		if (Logger.DBG) {
			log.d("getJSONArray, the http response content: " + builder);
		}
		jsonArray = new JSONArray(builder.toString());
		return jsonArray;
	}

}
