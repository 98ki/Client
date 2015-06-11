package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;
import android.text.TextUtils;

import com._98ki.util.FileUtils;
import com._98ki.util.Utils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.ExtProtocolModel.ExtTokenResponse;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class SmsCommit extends HttpTask {
	private static final Logger log = Logger.getLogger("SmsCommit");
	private static final String URL = "/account/register/sms/commit.json";

	public SmsCommit(ProtocolHandler handler, UserInfoBean bean) {
		super(URL, null, handler);
		buildRequestData(bean);
	}

	private void buildRequestData(UserInfoBean bean) {
		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(null, null, OAuthConstants.VERB_POST, url);

		auth.addParameter("phone", bean.mPhone);
		auth.addParameter("password", bean.mPassword);
		auth.addParameter("code", bean.mCode);
		log.d("auth.getAuthHeader():" + auth.getAuthHeader());
		log.d("auth.getRequestData():" + auth.getRequestData());

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
			log.d("httpRspHandler in SmsCommit");
		}
		// log.d("----smsCommit--->" + FileUtils.getError(input));
		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			ExtTokenResponse rsp = ExtProtocolUtil.parseTokenResponse(input);
			AccountBean bean = new AccountBean();
			bean.mUserId = rsp.mUserId;
			bean.mToken = rsp.mToken;
			bean.mTokenSecret = rsp.mTokenSecret;
			bean.mScreenName = rsp.mScreenName;

			AccountUtil.saveAccount(bean);
			reportSuccess();
			// new VerifyCredentials(mHandler).start();
		}
	}

	private void reportSuccess() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
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

	public static class UserInfoBean {
		public String mPhone;
		public String mCode;
		public String mPassword;
	}
}
