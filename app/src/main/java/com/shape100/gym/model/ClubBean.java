package com.shape100.gym.model;

import java.io.Serializable;

/**
 * 
 * TODO Club Bean
 * 
 * @author zpy
 * @data 2014-11-6 下午6:34:30
 * @version: V1.0
 */
public class ClubBean implements Serializable {

	private int id;
	private String name;
	private String address;
	private String city;
	private String phone;
	private String email;
	private String businessHours;
	private String homepageUrl;
	private String startUpPicUrls;
	private String logoUrl;
	private String picUrls;
	private String description;
	private String lon;
	private String lat;

	//
	public int courseStart;
	public int courseEnd;

	public ClubBean() {

	}

	public ClubBean(int id, String name, String address, String city,
			String phone, String email, String businessHours,
			String homepageUrl, String logoUrl, String picUrls,
			String description, String lon, String lat) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.phone = phone;
		this.email = email;
		this.businessHours = businessHours;
		this.homepageUrl = homepageUrl;
		this.logoUrl = logoUrl;
		this.picUrls = picUrls;
		this.description = description;
		this.lon = lon;
		this.lat = lat;
		this.courseStart = courseStart;
		this.courseEnd = courseEnd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStartUpPicUrls() {
		return startUpPicUrls;
	}

	public void setStartUpPicUrls(String startUpPicUrls) {
		this.startUpPicUrls = startUpPicUrls;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public int getCourseEnd() {
		return courseEnd;
	}

	public void setCourseEnd(int courseEnd) {
		this.courseEnd = courseEnd;
	}

	public String getHomepageUrl() {
		return homepageUrl;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
}
