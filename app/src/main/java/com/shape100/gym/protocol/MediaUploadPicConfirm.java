/**    
 * file name：MediaUploadPicConfirm.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-2 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;

/**    
 *     
 * project：shape100    
 * class：MediaUploadPicConfirm    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-12-2 下午8:39:26    
 * modify author: zpy    
 * update date：2014-12-2 下午8:39:26    
 * update remark：    
 * @version     
 *     
 */
public class MediaUploadPicConfirm extends HttpTask {
	private static final Logger log = Logger.getLogger("MediaUploadPicConfirm");
	private static final String URL = "/media/upload/pic_confirm.json";
	private AccountDetailBean mAccountDetailBean;

	public MediaUploadPicConfirm(ProtocolHandler handler, AccountDetailBean accountDetailBean) {
		super(URL, null, handler, true);
		mAccountDetailBean = accountDetailBean;
		buildRequestData();
	}

	private void buildRequestData() {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}
		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST, url);
		auth.addParameter("pic_id", mAccountDetailBean.getProfileImagePicId());
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();

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
		log.d("httpRspHandler start!");

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			ArrayList<String> confirmList = ExtProtocolUtil.parseUploadPicConfirm(input);
			log.e("save Confirm pic url to DB");
			mAccountDetailBean.setProfileImageUrl(confirmList.get(1));
			// save to club table
			AccountDetailUtil.saveAccountDetail(mAccountDetailBean);
			EventBus.getDefault().post(new Event.CommendChangeEvent());
			reportSuccess();
		}
	}

	private void reportSuccess() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_CONTINUE2);
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
