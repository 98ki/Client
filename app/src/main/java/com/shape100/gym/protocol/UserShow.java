package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.model.UserOtherInfo;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class UserShow extends HttpTask {
	private static Logger log = Logger.getLogger("UserShow");
	private ProtocolHandler handler;
	private static final String URL = "/users/show.json";
	private int flag; // 0 自己 ，1其他人

	public UserShow(long user_id, ProtocolHandler handler, int flag) {
		super(URL + "?user_id=" + user_id, null, handler, false);
		this.handler = handler;
		this.flag = flag;
		buildRequestData(user_id);
	}

	private void buildRequestData(long user_id) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("user_id", user_id + "");
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		// log.e("----------------------数据--" + FileUtils.getError(input));

		if (statusCode != SC_OK) {

		} else {
			if (flag == 1) {
				UserOtherInfo userInfo = ExtProtocolUtil
						.parseUserShowOther(input);
				Message msg = handler.obtainMessage();
				msg.what = Event.USERINFO;
				msg.obj = userInfo;
				handler.sendMessage(msg);
			}
			if (flag == 0) {
               UserInfo userInfo=ExtProtocolUtil.parseUserShowSelf(input);
               Message msg=handler.obtainMessage();
               msg.what=Event.USERINFO;
               msg.obj=userInfo;
               handler.sendMessage(msg);
			}
		}
	}
}
