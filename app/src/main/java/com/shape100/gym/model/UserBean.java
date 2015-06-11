/**    
 * file name：UserBean.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-1 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * project：shape100 class：UserBean desc： author：zpy zpy@98ki.com create
 * date：2014-12-1 下午1:50:22 modify author: zpy update date：2014-12-1 下午1:50:22
 * update remark：
 * 
 * @version
 * 
 */
public class UserBean implements Serializable {

	/**
	 * 蛋疼的数据架构设计啊
	 * 
	 * @author yupu
	 * @date 2015年1月15日
	 */
	private static final long serialVersionUID = 1L;

	private long userId;
	private String name;
	private String screenName;
	private String location;
	private String certification;
	private String description;
	private String profileImageUrl;
	private String url;
	private boolean protect;
	private int followersCount;
	private int follwingsCount;
	private int friendsCount;
	private int favoritesCount;
	private String utcOffset;
	private String timeZone;
	private int statusesCount;
	private boolean following;
	private boolean notifications;
	private int gender;
	private String birthday;
	private int height;
	private float weight;
	private int clubId;

	// "gender":2,"birthday":"1980-01-01","height":885,"weight":55,"
	// for key value table
	public HashMap<String, String> mMap;

	public UserBean() {

	}

	public UserBean(int userId, String name, String screenName,
			String location, String description, String profileImageUrl,
			String url, boolean protect, int followersCount, int friendsCount,
			int favoritesCount, String utcOffset, String timeZone,
			int statusesCount, boolean following, boolean notifications,
			int clubId) {
		this.userId = userId;
		this.name = name;
		this.screenName = screenName;
		this.location = location;
		this.description = description;
		this.profileImageUrl = profileImageUrl;
		this.url = url;
		this.protect = protect;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
		this.favoritesCount = favoritesCount;
		this.utcOffset = utcOffset;
		this.timeZone = timeZone;
		this.statusesCount = statusesCount;
		this.following = following;
		this.notifications = notifications;
		this.clubId = clubId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userId + "," + name + "," + screenName + "," + location + ","
				+ description + "," + profileImageUrl + "," + url + ","
				+ protect + "," + followersCount + "," + friendsCount + ","
				+ favoritesCount + "," + utcOffset + "," + timeZone + ","
				+ statusesCount + "," + following + "," + notifications + ","
				+ gender + "," + "birthday" + "," + height + "," + weight + ","
				+ clubId;
	}

	public HashMap<String, String> toHashMap() {
		mMap = new HashMap<String, String>();
		mMap.put(this.getUserIdKey(), userId + "");
		mMap.put(this.getNameKey(), name);
		mMap.put(this.getScreenNameKey(), screenName);
		mMap.put(this.getLocationKey(), location);
		mMap.put(this.getDescriptionKey(), description);
		mMap.put(this.getProfileImageUrlKey(), profileImageUrl);
		mMap.put(this.getUrlKey(), url);
		mMap.put(this.getProtectKey(), protect + "");
		mMap.put(this.getFollowingKey(), following + "");
		mMap.put(this.getFriendsCountKey(), friendsCount + "");
		mMap.put(this.getFavoritesCountKey(), favoritesCount + "");
		mMap.put(this.getUtcOffsetKey(), utcOffset + "");
		mMap.put(this.getTimeZoneKey(), timeZone);
		mMap.put(this.getStatusesCountKey(), statusesCount + "");
		mMap.put(this.getFollowingKey(), following + "");
		mMap.put(this.getNotificationsKey(), notifications + "");
		mMap.put(this.getGenderKey(), gender + "");
		mMap.put(this.getBirthdayKey(), birthday);
		mMap.put(this.getHeightKey(), height + "");
		mMap.put(this.getWeightKey(), weight + "");
		mMap.put(this.getClubIdKey(), clubId + "");

		return mMap;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public long getUserId() {
		return userId;
	}

	public String getUserIdKey() {
		return "user_id";
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public String getNameKey() {
		return "name";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getScreenNameKey() {
		return "screen_name";
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation() {
		return location;
	}

	public String getLocationKey() {
		return "location";
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionKey() {
		return "description";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getProfileImageUrlKey() {
		return "profile_image_url";
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlKey() {
		return "url";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getProtect() {
		return protect;
	}

	public String getProtectKey() {
		return "protected";
	}

	public void setProtect(boolean protect) {
		this.protect = protect;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public String getFollowersCountKey() {
		return "followers_count";
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public String getFriendsCountKey() {
		return "friends_count";
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getFavoritesCount() {
		return favoritesCount;
	}

	public String getFavoritesCountKey() {
		return "favorites_count";
	}

	public void setFavoritesCount(int favoritesCount) {
		this.favoritesCount = favoritesCount;
	}

	public String getUtcOffset() {
		return utcOffset;
	}

	public String getUtcOffsetKey() {
		return "utc_offset";
	}

	public void setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getTimeZoneKey() {
		return "time_zone";
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public String getStatusesCountKey() {
		return "statuses_count";
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public boolean isFollowing() {
		return following;
	}

	public String getFollowingKey() {
		return "following";
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public String getNotificationsKey() {
		return "notifications";
	}

	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
	}

	public int getGender() {
		return gender;
	}

	public String getGenderKey() {
		return "gender";
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getBirthdayKey() {
		return "birthday";
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getHeight() {
		return height;
	}

	public String getHeightKey() {
		return "height";
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public String getWeightKey() {
		return "weight";
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getClubId() {
		return clubId;
	}

	public String getClubIdKey() {
		return "club_id";
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getFollwingsCount() {
		return follwingsCount;
	}

	public void setFollwingsCount(int follwingsCount) {
		this.follwingsCount = follwingsCount;
	}
}