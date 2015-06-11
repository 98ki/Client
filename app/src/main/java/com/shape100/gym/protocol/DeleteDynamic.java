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

public class DeleteDynamic extends HttpTask {
	private static final Logger log = Logger.getLogger("DeleteDynamic");
	private static final String URL = "/statuses/destroy.json";
	private ProtocolHandler handler;

	public DeleteDynamic(ProtocolHandler handler, long id) {
		super(URL, null, handler, true);
		this.handler = handler;
		buildRequestData(id);
	}

	private void buildRequestData(long id) {
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
		auth.addParameter("id", String.valueOf(id));
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
		// System.out.println("-----------返回数据--------"+FileUtils.getError(input));
		String info = FileUtils.getError(input);
		// System.out.println("----返回数据-----" + info);
		if (statusCode != SC_OK) {
			handler.sendEmptyMessage(Event.DELETEDYNAMIC_FAILED);
		} else {
			if (!info.contains("\"error\":") && !info.contains("\"msg\":")) {
				handler.sendEmptyMessage(Event.DELETEDYNAMIC);
			}

		}
	}

}
