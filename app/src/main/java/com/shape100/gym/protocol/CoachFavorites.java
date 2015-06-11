/**    
 * file name：CoachFavorites.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-1 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.UserBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**
 * 
 * project：shape100 class：CoachFavorites desc： author：zpy zpy@98ki.com create
 * date：2014-12-1 下午2:02:45 modify author: zpy update date：2014-12-1 下午2:02:45
 * update remark：
 * 
 * @version
 * 
 */
public class CoachFavorites extends HttpTask {
	private static final Logger log = Logger.getLogger("CoachFavorites");
	private static final String URL = "/coach/favorites.json";
	private ArrayList<UserBean> mCoachList;

	public CoachFavorites(ProtocolHandler handler,
			ArrayList<UserBean> coachList, long userId) {
		super(URL + "?user_id=" + userId, null, handler, false);
		mCoachList = coachList;
		buildRequestData(userId);
	}

	private void buildRequestData(long userId) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}
		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("user_id", userId + "");
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		log.d("httpRspHandler start!");

		mCoachList.addAll(ExtProtocolUtil.parseFavoriteCoachResponse(input));

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
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
