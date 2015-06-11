package com.shape100.gym.protocol;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class FollowsOrFollowings extends HttpTask {
	public static final int FOLLOWS = 12;
	public static final int FOLLOWING = 13;

	private static final Logger log = Logger.getLogger("FollowsOrFollowings");
	private ProtocolHandler handler;
	private static final String URL_FOLLOWS = "/friendships/followers.json";
	private static final String URL_FOLLOWING = "/friendships/followings.json";

	public FollowsOrFollowings(long user_id, int count, int page,
			ProtocolHandler handler, int flag) {
		super(buildUrl(user_id, count, page, flag), null, handler, false);
		this.handler = handler;
		buildRequestData(user_id, count, page, flag);
	}

	private static String buildUrl(long user_id, int count, int page, int flag) {
		StringBuffer sb = new StringBuffer();
		if (flag == FOLLOWS) {
			sb.append(URL_FOLLOWS);
		}
		if (flag == FOLLOWING) {
			sb.append(URL_FOLLOWING);
		}
		sb.append("?user_id=").append(user_id).append("&count=").append(count)
				.append("&page=").append(page);
		return sb.toString();
	}

	private void buildRequestData(long user_id, int count, int page, int flag) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST;

		if (flag == FOLLOWING) {
			url = url + URL_FOLLOWING;
		}
		if (flag == FOLLOWS) {
			url = url + URL_FOLLOWS;
		}

		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("user_id", user_id + "");
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
			//log.e("---关注粉丝返回数据-" + FileUtils.getError(input));
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
