package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.CommentsData;
import com.shape100.gym.model.Dynamic;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class FriendsTimeline extends HttpTask {
	private static final Logger log = Logger.getLogger("FriendsTimeline");
	private static final String URL = "/statuses/friends_timeline.json";
	private ProtocolHandler handler;

	public FriendsTimeline(ProtocolHandler handler, int count, int page) {
		super(buildUrl(count, page), null, handler, false);
		this.handler = handler;
		buildRequestData(count, page);
	}

	private static String buildUrl(int count, int page) {
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("?").append("count=").append(count).append("&")
				.append("page=").append(page);
		return sb.toString();
	}

	private void buildRequestData(int count, int page) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("count", count + "");
		auth.addParameter("page", page + "");
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
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in FriendsTimeline");
		}

		if (statusCode != HttpTask.SC_OK) {
			reportFailure();
		} else {
			ArrayList<CommentsData> dynamics = ExtProtocolUtil
					.parseFriendsTimeLine(input);
			reportSuccess(dynamics);
		}
	}

	private void reportSuccess(ArrayList<CommentsData> result) {
		if (handler != null) {
			Message msg = Message.obtain();
			msg.what = ConstVar.HANDLER_MSG_SUCCESS;
			msg.obj = result;
			handler.sendMessage(msg);
		}
	}

	private void reportFailure() {
		if (handler != null) {
			handler.sendEmptyMessage(ConstVar.HANDLER_MSG_FAILURE);
		}
	}

}
