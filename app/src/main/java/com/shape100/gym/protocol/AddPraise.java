package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com._98ki.util.FileUtils;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class AddPraise extends HttpTask {
	private static final Logger log = Logger.getLogger("AddPraise");
	private static final String URL_ADD = "/like/create.json"; // 点赞接口
	private static final String URL_CANCEL = "/like/destroy.json"; // 取消点赞
	private ProtocolHandler handler;
	private boolean isAdd;

	public AddPraise(long id, ProtocolHandler handler, boolean isAdd) {
		super(buildURL(isAdd), null, handler);
		buildRequestData(id, isAdd);
		this.isAdd = isAdd;
		this.handler = handler;
	}

	private static String buildURL(boolean isAdd) {
		if (isAdd) {
			return URL_ADD;
		} else {
			return URL_CANCEL;
		}
	}

	private void buildRequestData(long id, boolean isAdd) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}
		String url = "http://" + HOST + buildURL(isAdd);
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST,
				url);
		auth.addParameter("id", id + "");
		auth.addParameter("source", OAuth.APP_KEY);

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
		// System.out.println("-----------------------点赞反映-------------"
		// + FileUtils.getError(input));
		if (statusCode != SC_OK) {
			if (isAdd) {
				handler.sendEmptyMessage(Event.PRAISE_ADD_FAILED);
			} else {
				handler.sendEmptyMessage(Event.PRAISE_CANCEL_FAILED);
			}
		} else {
			handler.sendEmptyMessage(Event.PRAISE);
		}
	}
}
