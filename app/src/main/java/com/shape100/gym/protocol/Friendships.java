package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.model.User;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**
 * 关注与取消关注
 * 
 * @author yupu
 * @date 2015年3月17日
 */
public class Friendships extends HttpTask {
	private ProtocolHandler handler;
	private static final Logger log = Logger.getLogger("Friendships");
	private static final String URL_CREATE = "/friendships/create.json";
	private static final String URL_DESTORY = "/friendships/destroy.json";
	public static final int CREAT = 1;
	public static final int DESTORY = 2;
	private int flag;

	public Friendships(long user_id, ProtocolHandler handler, int flag) {
		super(buildUrl(flag), null, handler);
		this.handler = handler;
		this.flag = flag;
		buildRequestData(user_id, flag);
	}

	private static String buildUrl(int flag) {
		if (flag == CREAT) {
			return URL_CREATE;
		} else {
			return URL_DESTORY;
		}
	}

	private void buildRequestData(long id, int flag) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST + buildUrl(flag);
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST,
				url);
		auth.addParameter("user_id", id + "");
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();

		StringEntity se;
		try {
			String data = auth.getRequestData();
			log.d("post data : " + data);
			se = new StringEntity(data);
			setEntity(se);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		// System.out.println("--------------------关注接口返回数据----"
		// + FileUtils.getError(input));
		if (statusCode != SC_OK) {
			handler.sendEmptyMessage(Event.CONCERN_FAILED);
		} else {
			User user = ExtProtocolUtil.getUser(ExtProtocolUtil
					.getJSONObject(input));
			if (user != null && user.getUserId() != 0) {
				Message msg = handler.obtainMessage();
				msg.obj = user;
				if (flag == CREAT) {
					msg.what = Event.CREAT_CONCERN;
				} else {
					msg.what = Event.CANCEL_CONCERN;
				}
				handler.sendMessage(msg);
			}
		}
	}
}
