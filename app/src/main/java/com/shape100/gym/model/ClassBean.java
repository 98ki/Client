/**    
 * file name：ClassBean.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-21 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.model;

import java.io.Serializable;

/**
 * 
 * project：shape100 class：ClassBean desc： author：zpy zpy@98ki.com create
 * date：2014-11-21 上午9:31:21 modify author: zpy update date：2014-11-21 上午9:31:21
 * update remark：
 * 
 * @version
 * 
 */
public class ClassBean implements Serializable {

	private int classId;
	private String className;
	private int clubId;
	private String picUrls;
	private String video_thumbnail_pic;
	private String video_url;
	private String description;
	private String preregistration_phone;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public String getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}

	public String getVideo_thumbnail_pic() {
		return video_thumbnail_pic;
	}

	public void setVideo_thumbnail_pic(String video_thumbnail_pic) {
		this.video_thumbnail_pic = video_thumbnail_pic;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPreregistration_phone() {
		return preregistration_phone;
	}

	public void setPreregistration_phone(String preregistration_phone) {
		this.preregistration_phone = preregistration_phone;
	}
}
