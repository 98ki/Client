package com.shape100.gym.model;

public class User extends UserBean {
	/**
	 * @author yupu
	 * @date 2015年1月22日
	 */
	private static final long serialVersionUID = 1L;
	private String created_at;

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "User [created_at=" + created_at + ", getCreated_at()="
				+ getCreated_at() + ", toString()=" + super.toString()
				+ ", toHashMap()=" + toHashMap() + ", getCertification()="
				+ getCertification() + ", getUserId()=" + getUserId()
				+ ", getUserIdKey()=" + getUserIdKey() + ", getName()="
				+ getName() + ", getNameKey()=" + getNameKey()
				+ ", getScreenName()=" + getScreenName()
				+ ", getScreenNameKey()=" + getScreenNameKey()
				+ ", getLocation()=" + getLocation() + ", getLocationKey()="
				+ getLocationKey() + ", getDescription()=" + getDescription()
				+ ", getDescriptionKey()=" + getDescriptionKey()
				+ ", getProfileImageUrl()=" + getProfileImageUrl()
				+ ", getProfileImageUrlKey()=" + getProfileImageUrlKey()
				+ ", getUrl()=" + getUrl() + ", getUrlKey()=" + getUrlKey()
				+ ", getProtect()=" + getProtect() + ", getProtectKey()="
				+ getProtectKey() + ", getFollowersCount()="
				+ getFollowersCount() + ", getFollowersCountKey()="
				+ getFollowersCountKey() + ", getFriendsCount()="
				+ getFriendsCount() + ", getFriendsCountKey()="
				+ getFriendsCountKey() + ", getFavoritesCount()="
				+ getFavoritesCount() + ", getFavoritesCountKey()="
				+ getFavoritesCountKey() + ", getUtcOffset()=" + getUtcOffset()
				+ ", getUtcOffsetKey()=" + getUtcOffsetKey()
				+ ", getTimeZone()=" + getTimeZone() + ", getTimeZoneKey()="
				+ getTimeZoneKey() + ", getStatusesCount()="
				+ getStatusesCount() + ", getStatusesCountKey()="
				+ getStatusesCountKey() + ", isFollowing()=" + isFollowing()
				+ ", getFollowingKey()=" + getFollowingKey()
				+ ", isNotifications()=" + isNotifications()
				+ ", getNotificationsKey()=" + getNotificationsKey()
				+ ", getGender()=" + getGender() + ", getGenderKey()="
				+ getGenderKey() + ", getBirthday()=" + getBirthday()
				+ ", getBirthdayKey()=" + getBirthdayKey() + ", getHeight()="
				+ getHeight() + ", getHeightKey()=" + getHeightKey()
				+ ", getWeight()=" + getWeight() + ", getWeightKey()="
				+ getWeightKey() + ", getClubId()=" + getClubId()
				+ ", getClubIdKey()=" + getClubIdKey() + "]";
	}

}
