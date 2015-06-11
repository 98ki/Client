package com.shape100.gym.protocol;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Looper;
import android.widget.Toast;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class RegNotification extends HttpTask {
	private static final Logger log = Logger.getLogger("RegNotification");
	private static final String URL = "/account/register_notification.json";
	private ProtocolHandler handler;
	private String token, os;

	public RegNotification(ProtocolHandler handler, String token, String os) {
		super(URL, null, handler, false);
		this.handler = handler;
		this.token = token;
		this.os = os;
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
		auth.addParameter("token", token);
		auth.addParameter("os", os);
		auth.doSign();

		mOAuthHeader = auth.getAuthHeader();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		ArrayList<String> res = ExtProtocolUtil.parserRemovePhone(input);
		if (statusCode != HttpTask.SC_OK && !res.get(0).equals("0")) {
//			System.out.println("----------------" + res.get(0) + "--"
//					+ res.get(1));
			// reportFailure();
		} else {
			// reportSuccse();
		}

	}

	private void reportSuccse() {
		// handler.sendEmptyMessage(Event.REGNOTIFICATION);
		Looper.prepare();
		Toast.makeText(MainApplication.sContext, "注册成功", Toast.LENGTH_LONG)
				.show();
		Looper.loop();
	}

	private void reportFailure() {
		// handler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
		Looper.prepare();
		Toast.makeText(MainApplication.sContext, "注册失败", Toast.LENGTH_LONG)
				.show();
		Looper.loop();
		log.e("---------regester notification failed.");
	}
}
