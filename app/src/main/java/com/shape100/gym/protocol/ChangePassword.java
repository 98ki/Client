/**    
 * file name：ChangePassword.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-26 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**    
 *     
 * project：shape100    
 * class：ChangePassword    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-26 下午5:00:03    
 * modify author: zpy    
 * update date：2014-11-26 下午5:00:03    
 * update remark：    
 * @version     
 *     
 */
public class ChangePassword extends HttpTask {
	private static final Logger log = Logger.getLogger("ChangePassword");
	private static final String URL = "/account/change_password.json";
	/**
	 * 
	 * New Instance： ChangePassword.    
	 *    
	 * @param handler
	 * @param oldPassword
	 * @param newPassword
	 *          
	 */
	public ChangePassword(ProtocolHandler handler, String oldPassword, String newPassword) {
		super(URL, null, handler);
		buildRequestData(oldPassword,newPassword);
	}

	private void buildRequestData(String oldPassword, String newPassword) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}
		String url = "http://" + HOST + URL ;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST, url);
		auth.addParameter("current_password",oldPassword );
		auth.addParameter("new_password",newPassword );

		auth.doSign();
		mOAuthHeader = auth.getAuthHeader(); 

		StringEntity se;
		try {
			String data = auth.getRequestData();
			log.d( "post data : " + data);
			se = new StringEntity(data);
			setEntity(se);		
		} catch (UnsupportedEncodingException e) {	
			e.printStackTrace();
		}
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
		log.d("httpRspHandler start!");

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			//TODO    根据返回值设置提示信息
			System.out.println("------修改密码--------------"+FileUtils.getError(input));
			reportSuccess();
		}
	}

	private void reportSuccess() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
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

	@Override
	protected void doError() {
		super.doError();

		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
		}
	}
}