package com.shape100.gym.model;

/**
 * 拉取评论列表的评论数据模型
 * 
 * @author yupu
 * @date 2015年3月12日
 */
public class CommentsData extends Dynamic {
	/**
	 * @author yupu
	 * @date 2015年3月16日
	 */
	private static final long serialVersionUID = 1L;

	private long club_id;
	private String club_name;
	private long class_id;
	private String class_name;
	private long coach_id;
	private String coach_name;
	private int calorie;
	private int distance;
	private int duration;
	private boolean liked;

	public long getClub_id() {
		return club_id;
	}

	public void setClub_id(long club_id) {
		this.club_id = club_id;
	}

	public String getClub_name() {
		return club_name;
	}

	public void setClub_name(String club_name) {
		this.club_name = club_name;
	}

	public long getClass_id() {
		return class_id;
	}

	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public long getCoach_id() {
		return coach_id;
	}

	public void setCoach_id(long coach_id) {
		this.coach_id = coach_id;
	}

	public String getCoach_name() {
		return coach_name;
	}

	public void setCoach_name(String coach_name) {
		this.coach_name = coach_name;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	@Override
	public String toString() {
		return "CommentsData [club_id=" + club_id + ", club_name=" + club_name
				+ ", class_id=" + class_id + ", class_name=" + class_name
				+ ", coach_id=" + coach_id + ", coach_name=" + coach_name
				+ ", calorie=" + calorie + ", distance=" + distance
				+ ", duration=" + duration + ", liked=" + liked + "]";
	}

}
