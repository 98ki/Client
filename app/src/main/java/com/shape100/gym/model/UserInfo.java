package com.shape100.gym.model;

import java.io.Serializable;

/**
 * 获得用户信息，包含一条动态
 * 
 * @author yupu
 * @date 2015年3月19日
 */
public class UserInfo implements Serializable {
	/**
	 * @author yupu
	 * @date 2015年3月19日
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private Status status;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserInfo [user=" + user + ", status=" + status + "]";
	}

}
