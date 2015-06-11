/**    
 * file name：Update.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-26 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.model;

/**
 * 
 * project：shape100 class：Update desc： author：zpy zpy@98ki.com create
 * date：2014-11-26 下午12:23:56 modify author: zpy update date：2014-11-26
 * 下午12:23:56 update remark：
 * 
 * @version
 * 
 */
public class UpdateBean {

	private int hasNewVersion;
	private String NewVersion;
	private int isForceUpdate;
	private String updateDescription;
	private String downloadUrl;

	public int getHasNewVersion() {
		return hasNewVersion;
	}

	public void setHasNewVersion(int hasNewVersion) {
		this.hasNewVersion = hasNewVersion;
	}

	public String getNewVersion() {
		return NewVersion;
	}

	public void setNewVersion(String newVersion) {
		NewVersion = newVersion;
	}

	public int getIsForceUpdate() {
		return isForceUpdate;
	}

	public void setIsForceUpdate(int isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

}
