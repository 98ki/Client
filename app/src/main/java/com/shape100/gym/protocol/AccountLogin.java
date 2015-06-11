package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.ExtProtocolModel.ExtTokenResponse;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class AccountLogin extends HttpTask {
	private static final Logger log = Logger.getLogger("AccountLogin");
	private static final String URL = "/account/login.json";

	public AccountLogin(ProtocolHandler handler, String screen_name,
			String password) {
		super(URL, null, handler);
		buildRequestData(screen_name, password);
	}

	private void buildRequestData(String screen_name, String password) {
		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(null, null, OAuthConstants.VERB_POST, url);

		auth.addParameter("screen_name", screen_name);
		auth.addParameter("password", password);
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
			log.d("httpRspHandler in AccountLogin");
		}

		if (statusCode != HttpTask.SC_OK) {
			// log.e("---AccountLogin-->" + FileUtils.getError(input));
			reportFailure(statusCode);
		} else {
			ExtTokenResponse rsp = ExtProtocolUtil.parseTokenResponse(input);
			AccountBean bean = new AccountBean();
			bean.mUserId = rsp.mUserId;
			bean.mToken = rsp.mToken;
			bean.mTokenSecret = rsp.mTokenSecret;
			bean.mScreenName = rsp.mScreenName;

			log.e("AccountUtil.saveAccount() Before");
			AccountUtil.saveAccount(bean);

			ThreadPool.getInstance().execute(new VerifyCredentials(mHandler));
			// new VerifyCredentials(mHandler).start();
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
