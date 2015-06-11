/**    
 * file name：MediaUploadPic.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-2 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**
 * 
 * project：shape100 class：MediaUploadPic desc： author：zpy zpy@98ki.com create
 * date：2014-12-2 下午1:59:27 modify author: zpy update date：2014-12-2 下午1:59:27
 * update remark：
 * 
 * @version
 * 
 */
public class MediaUploadPic extends HttpTask {
	private static final Logger log = Logger.getLogger("Media upload pic");
	private static final String URL = "/media/upload/pic.json";
	private AccountDetailBean mAccountDetailBean;

	public MediaUploadPic(ProtocolHandler handler,
			AccountDetailBean accountDetailBean) {
		super(URL + "?pic=" + accountDetailBean.getProfileImageUrl(), null,
				handler, false);
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
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("pic", mAccountDetailBean.getProfileImageUrl());
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		log.d("httpRspHandler start!");
		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			ArrayList<String> list = ExtProtocolUtil.parseUploadPic(input);
			mAccountDetailBean.setProfileImageUrl(list.get(1));
			mAccountDetailBean.setProfileImagePicId(list.get(0));
			new ImagePut(mHandler, mAccountDetailBean).start();
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
