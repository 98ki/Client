/**    
 * file name：CheckUpdate.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-26 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com._98ki.util.AppUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.model.UpdateBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;

/**    
 *     
 * project：shape100    
 * class：CheckUpdate    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-26 下午12:07:29    
 * modify author: zpy    
 * update date：2014-11-26 下午12:07:29    
 * update remark：    
 * @version     
 *     
 */
public class CheckUpdate extends HttpTask {
	private static final Logger log = Logger.getLogger("CheckUpdate");
	private static final String URL = "/system/checkupdate.json";
	
	/**
	 * In the system design, CheckUpdate.getId is only used in club schedule activity to unique course. 
	 * 
	 * CheckUpdate.getId is not in schedule/club.json, So I pass ID here to make a right connect.
	 * It's primary key, ExtProtocolUtil.parseCourse(input) use ID update TABLE course. 
	 */
	private CheckUpdate mCheckUpdate;
	/**
	 * 
	 * New Instance: CourseShow.
	 * 
	 * @param handler
	 * 
	 */
	public CheckUpdate(ProtocolHandler handler) {
		super(buildUrl(), null, handler, false);
	}
	/**
	 * 
	 * buildUrl
	 * need app_key and version
	 * 
	 */
	private static String buildUrl() {
		log.d("builderUrl start");
		// app_key
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("?");
		sb.append("app_key=").append(OAuth.APP_KEY).append("&version=").append(AppUtils.getVersion(MainApplication.sContext));
		return sb.toString();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
		log.d("httpRspHandler in CheckUpdate");
		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		}else {
			
			UpdateBean bean = ExtProtocolUtil.parseUpdate(input);
			reportSuccess(bean);

		}
	}

	private void reportFailure(int result) {
		if (mHandler != null) {
			Message msg = new Message();
			msg.what = ConstVar.HANDLER_MSG_FAILURE;
			msg.arg1 = result;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 
	 * report HANDLER_MSG_SUCCESS
	 * 
	 * @throw
	 * @return void
	 */
	private void reportSuccess(UpdateBean bean) {
		Message msg = new Message();
		msg.obj = bean;
		msg.what = ConstVar.HANDLER_MSG_SUCCESS;
		mHandler.sendMessage(msg);
	}

	@Override
	protected void doError() {
		super.doError();
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
		}
	}

}
