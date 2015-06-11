package com.shape100.gym.protocol;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.model.User;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class FollowsClass extends HttpTask {
	private static final Logger log = Logger.getLogger("FollowsClass");
	private ProtocolHandler handler;
	private static final String URL = "/class/followers.json";

	public FollowsClass(long class_id, int count, int page,
			ProtocolHandler handler) {
		super(URL + "?class_id=" + class_id + "&count=" + count + "&page="
				+ page, null, handler, false);
		this.handler = handler;
		buildRequestData(class_id, count, page);
	}

	private void buildRequestData(long class_id, int count, int page) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("class_id", class_id + "");
		auth.addParameter("count", count + "");
		auth.addParameter("page", page + "");
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		
		if (statusCode != SC_OK) {
			handler.sendEmptyMessage(Event.CONCERN_LIST_FAILED);
		} else {
			ArrayList<UserInfo> userInfos = ExtProtocolUtil
					.parseUserInfoSelfList(input);
			Message msg = handler.obtainMessage();
			msg.what = Event.CONCERN_LIST;
			msg.obj = userInfos;
			handler.sendMessage(msg);
		}
	}
}
