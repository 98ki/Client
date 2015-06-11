package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.net.Uri;

import com._98ki.util.Utils;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**
 * 评论接口
 * 
 * @author yupu
 * @date 2015年3月11日
 */
public class CreateComments extends HttpTask {
	private static final Logger log = Logger.getLogger("CreateComments");
	private static final String URL = "/comments/create.json";
	private ProtocolHandler handler;

	public CreateComments(String content, long id, ProtocolHandler handler) {
		super(URL, null, handler);
		this.handler = handler;
		String con = Utils.RemoveMore(content);
		buildRequestData(Uri.encode(con, "utf-8").replace("*", "%2A"), id);
	}

	private void buildRequestData(String content, long id) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST,
				url);

		auth.addParameter("id", id + "");
		auth.addParameter("source", OAuth.APP_KEY);
		auth.addParameter("comment", content);

		// try {
		// auth.addParameter("comment", URLEncoder.encode(content, "utf-8"));
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

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
		/*
		 * System.out.println("-----------返回的评论数据------" +
		 * FileUtils.getError(input));
		 */
		if (statusCode != SC_OK) {
			handler.sendEmptyMessage(Event.CREATECOMMENTS_FAILED);
		} else {
			handler.sendEmptyMessage(Event.CREATECOMMENTS);
		}
	}

}
