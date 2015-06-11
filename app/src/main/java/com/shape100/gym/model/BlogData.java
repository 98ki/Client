package com.shape100.gym.model;

import java.util.ArrayList;

public class BlogData {
	// ProtocolHandler handler, String status, int visible, String pic_id,
	// String in_reply_to_status_id, float lat, float lon, String rip
	private float lat; // 纬度
	private float lon; // 经度
	private String status; // 要转发的微博消息内容
	private int visible; // 微博的可见性，0：所有人能看，1：仅自己可见，2：朋友可见，默认为0。
	private ArrayList<String> pic_ids; // 上传的图片你的pid
	private ArrayList<PicInfo> upLoaded; // 上传的图片的信息
	private int[] firstPosition; // 上传图片第一步
	private int[] secondPosition; // 上传图片第二步
	private int[] thirdPosition; // 上传图片第三步
	private String picids;

	private String in_reply_to_status_id; //
	private String rip; //

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public ArrayList<String> getPic_ids() {
		return pic_ids;
	}

	public void setPic_ids(ArrayList<String> pic_ids) {
		this.pic_ids = pic_ids;
	}

	public String getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getRip() {
		return rip;
	}

	public void setRip(String rip) {
		this.rip = rip;
	}

	public ArrayList<PicInfo> getUpLoaded() {
		return upLoaded;
	}

	public void setUpLoaded(ArrayList<PicInfo> upLoaded) {
		this.upLoaded = upLoaded;
	}

	public int[] getFirstPosition() {
		return firstPosition;
	}

	public void setFirstPosition(int[] firstPosition) {
		this.firstPosition = firstPosition;
	}

	public int[] getSecondPosition() {
		return secondPosition;
	}

	public void setSecondPosition(int[] secondPosition) {
		this.secondPosition = secondPosition;
	}

	public int[] getThirdPosition() {
		return thirdPosition;
	}

	public void setThirdPosition(int[] thirdPosition) {
		this.thirdPosition = thirdPosition;
	}

	public String getPicids() {
		return picids;
	}

	public void setPicids(String picids) {
		this.picids = picids;
	}
}
