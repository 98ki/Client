package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class RemovePhone extends HttpTask {
	private static final String URL = "/account/update_sms.json";
	private Logger log = Logger.getLogger("注销手机号");
	private ProtocolHandler handler;

	public RemovePhone(ProtocolHandler handler) {
		super(URL, null, handler);
		this.handler = handler;
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
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST,
				url);
		auth.addParameter("code", "123456");
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
		if (statusCode != HttpTask.SC_OK) {
			handler.sendEmptyMessage(ConstVar.HANDLER_MSG_FAILURE);
		} else {
			ArrayList<String> res = ExtProtocolUtil.parserRemovePhone(input);
			System.out.println("-------------------------返回码----" + res.get(0)
					+ "----" + res.get(1));
			if (res.get(1).equals("ok")) {
				handler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
			}
		}
	}

}
