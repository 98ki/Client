package com.shape100.gym.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Pack200.Packer;

import com.umeng.message.proguard.bo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 动态信息数据
 * 
 * @author yupu
 * @date 2015年1月19日
 */

// [{"text":"http:\/\/api.dev.shape100.com\/url\/18\n39.916183----116.51708","id":1000000218,"truncated":false,"created_at":"Thu
// Jan 29 11:18:37 +0800 2015","in_reply_to_status_id":"","source":"<a
// href=\"http:\/\/sppkg.oss-cn-beijing.aliyuncs.com\/android\/alpha\/YXClient.apk\"
// rel=\"nofollow\">\u6709\u578b\u5065\u8eab(Android)<\/a>","in_reply_to_user_id":"","in_reply_to_screen_name":"","geo":{"type":"Point","coordinates":[39.916183,116.51708]},"favorited":false,"pic_urls":[{"thumbnail_pic":"http:\/\/img01.shape100.com\/sns\/thumbnail\/1000000285d21632d60f583672.jpg"}],"user":{"user_id":1000000285,"name":"\u5c0f\u9c7c","screen_name":"sp1422434286326245","location":"","certification":"","description":"","profile_image_url":"http:\/\/img01.shape100.com\/sns\/square\/10000002854fc072461f448e24.jpg","url":"","protected":false,"followers_count":0,"friends_count":0,"created_at":"Wed
// Jan 28 16:38:06 +0800
// 2015","favorites_count":0,"utc_offset":"0","time_zone":"UTC","statuses_count":2,"following":true,"notifications":false,"gender":1,"birthday":"1992-11-17","height":185,"weight":75,"club_id":1000080833}},{"text":"http:\/\/api.dev.shape100.com\/url\/16\n0.0----0.0","id":1000000216,"truncated":false,"created_at":"Thu
// Jan 29 10:59:02 +0800 2015","in_reply_to_status_id":"","source":"<a
// href=\"http:\/\/sppkg.oss-cn-beijing.aliyuncs.com\/android\/alpha\/YXClient.apk\"
// rel=\"nofollow\">\u6709\u578b\u5065\u8eab(Android)<\/a>","in_reply_to_user_id":"","in_reply_to_screen_name":"","geo":{"type":"Point","coordinates":[0,0]},"favorited":false,"pic_urls":[{"thumbnail_pic":"http:\/\/img01.shape100.com\/sns\/thumbnail\/1000000285798b550bc9cadd1d.jpg"}],"user":{"user_id":1000000285,"name":"\u5c0f\u9c7c","screen_name":"sp1422434286326245","location":"","certification":"","description":"","profile_image_url":"http:\/\/img01.shape100.com\/sns\/square\/10000002854fc072461f448e24.jpg","url":"","protected":false,"followers_count":0,"friends_count":0,"created_at":"Wed
// Jan 28 16:38:06 +0800
// 2015","favorites_count":0,"utc_offset":"0","time_zone":"UTC","statuses_count":2,"following":true,"notifications":false,"gender":1,"birthday":"1992-11-17","height":185,"weight":75,"club_id":1000080833}}]

public class Dynamic implements Serializable {

	/**
	 * @author yupu
	 * @date 2015年3月13日
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @author yupu
	 * @date 2015年3月9日
	 */

	private String text;
	private boolean truncated;
	private String created_at;
	private long in_reply_to_status_id;
	private String source;
	private long id;
	private long in_reply_to_user_id;
	private String in_reply_to_screen_name;
	private User user;
	private List<String> pic_urls;
	private boolean favorited;
	private Geo geo;
	private int comments_count; // 评论数
	private int likes_count; // 点赞数
	private String in_reply_to_fullname;

	public long getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(long in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(long in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getIn_reply_to_fullname() {
		return in_reply_to_fullname;
	}

	public void setIn_reply_to_fullname(String in_reply_to_fullname) {
		this.in_reply_to_fullname = in_reply_to_fullname;
	}

	public int getStrincomments_count() {
		return comments_count;
	}

	public void setStrincomments_count(int strincomments_count) {
		this.comments_count = strincomments_count;
	}

	public int getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(int likes_count) {
		this.likes_count = likes_count;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public List<String> getPic_urls() {
		return pic_urls;
	}

	public void setPic_urls(List<String> pic_urls) {
		this.pic_urls = pic_urls;
	}

	@Override
	public String toString() {
		return "Dynamic [text=" + text + ", truncated=" + truncated
				+ ", created_at=" + created_at + ", in_reply_to_status_id="
				+ in_reply_to_status_id + ", source=" + source + ", id=" + id
				+ ", in_reply_to_user_id=" + in_reply_to_user_id
				+ ", in_reply_to_screen_name=" + in_reply_to_screen_name
				+ ", user=" + user + ", pic_urls=" + pic_urls + ", favorited="
				+ favorited + ", geo=" + geo + ", strincomments_count="
				+ comments_count + ", likes_count=" + likes_count
				+ ", in_reply_to_fullname=" + in_reply_to_fullname + "]";
	}

	// public static final Parcelable.Creator<Dynamic> CREATOR = new
	// Creator<Dynamic>() {
	//
	// @Override
	// public Dynamic createFromParcel(Parcel source) {
	// Dynamic dynamic = new Dynamic();
	// // Bundle bundle = source.readBundle();
	// // dynamic.created_at = bundle.getString(CREATED);
	// // dynamic.favorited = bundle.getBoolean(FAVORITED);
	// // dynamic.geo = (Geo) bundle.getSerializable(GEO);
	// // dynamic.id = bundle.getInt(ID);
	// // dynamic.in_reply_to_screen_name = bundle.getString(SCREEN_NAME);
	// // dynamic.in_reply_to_status_id = bundle.getString(STATUS_ID);
	// // dynamic.in_reply_to_user_id = bundle.getString(USER_ID);
	// // dynamic.pic_urls = bundle.getStringArrayList(PIC_URLS);
	// // dynamic.source = bundle.getString(SOURCE);
	// // dynamic.text = bundle.getString(TEXT);
	// // dynamic.truncated = bundle.getBoolean(TRUNCATED);
	// // dynamic.user = (User) bundle.getSerializable(USER);
	// String[] data = new String[6];
	// source.readStringArray(data);
	// dynamic.text = data[0];
	// dynamic.created_at = data[1];
	// dynamic.in_reply_to_status_id = data[2];
	// dynamic.source = data[3];
	// dynamic.in_reply_to_user_id = data[4];
	// dynamic.in_reply_to_screen_name = data[5];
	// boolean[] bodata = new boolean[2];
	// source.readBooleanArray(bodata);
	// dynamic.truncated = bodata[0];
	// dynamic.favorited = bodata[1];
	// dynamic.id = source.readInt();
	// dynamic.strincomments_count = source.readInt();
	// dynamic.likes_count = source.readInt();
	// dynamic.user = (User) source.readSerializable();
	// dynamic.geo = (Geo) source.readSerializable();
	// dynamic.pic_urls = source.readArrayList(Dynamic.class
	// .getClassLoader());
	// return dynamic;
	// }
	//
	// @Override
	// public Dynamic[] newArray(int size) {
	// return null;
	// }
	// };

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeStringArray(new String[] { text, created_at,
	// in_reply_to_status_id, source, in_reply_to_user_id,
	// in_reply_to_screen_name });
	// dest.writeBooleanArray(new boolean[] { truncated, favorited });
	// dest.writeInt(id);
	// dest.writeInt(strincomments_count);
	// dest.writeInt(likes_count);
	// dest.writeSerializable(user);
	// dest.writeSerializable(geo);
	// dest.writeList(pic_urls);
	// // dest.writeBooleanArray(truncated, favorited);
	// }
}
