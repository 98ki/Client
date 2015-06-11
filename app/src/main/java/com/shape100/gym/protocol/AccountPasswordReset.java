/**    
 * file name：AccountPasswordReset.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-1 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;

/**    
 *     
 * project：shape100    
 * class：AccountPasswordReset    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-12-1 下午8:43:18    
 * modify author: zpy    
 * update date：2014-12-1 下午8:43:18    
 * update remark：    
 * @version     
 *     
 */
public class AccountPasswordReset extends HttpTask {
	private static final Logger log = Logger.getLogger("AccountPasswordReset");
	private static final String URL = "/account/reset_password.json";

	public AccountPasswordReset(ProtocolHandler handler, String phone, String code, String password) {
		super(URL, null, handler, true, true);
		buildRequestData(phone, code, password);
	}

	private void buildRequestData(String phone, String code, String password) {
		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(null, null, OAuthConstants.VERB_POST, url);
		auth.addParameter("phone", phone);
		auth.addParameter("token", code);
		auth.addParameter("new_password", password);
	


		StringEntity se;
		try {
			String data = auth.getRequestData();
			if (Logger.DBG) {
				log.d("post data : " + data);
			}
			se = new StringEntity(data);
			setEntity(se);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
		log.d("httpRspHandler in AccountPasswordReset");

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			reportSuccess();
		}
	}
	
	private void reportSuccess() {
		if (mHandler != null) {
			Message msg = new Message();
			msg.what = ConstVar.HANDLER_MSG_SUCCESS;
			mHandler.sendMessage(msg);
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
