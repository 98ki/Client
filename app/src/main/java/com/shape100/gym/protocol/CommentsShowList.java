package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.model.CommentsData;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**
 * 拉取评论列表
 * 
 * @author yupu
 * @date 2015年3月12日
 */
public class CommentsShowList extends HttpTask {
	private static final Logger log = Logger.getLogger("CommentsShowList");
	private static final String URL = "/comments/show.json";
	private ProtocolHandler handler;

	public CommentsShowList(long id, int count, int page,
			ProtocolHandler handler) {
		super(URL + "?source=" + OAuth.APP_KEY + "&id=" + id + "&count="
				+ count + "&page=" + page, null, handler, false);
		buildRequestData(id, count, page);
		this.handler = handler;
	}

	private void buildRequestData(long id, int count, int page) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}
		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("id", id + "");
		auth.addParameter("source", OAuth.APP_KEY);
		auth.addParameter("count", count + "");
		auth.addParameter("page", page + "");
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
		if (statusCode != SC_OK) {
		//	log.e("-----错误评论列表信息---" + FileUtils.getError(input));
			handler.sendEmptyMessage(Event.COMMENTSSHOW_FAILED);
		} else {
			ArrayList<CommentsData> data = ExtProtocolUtil.parseComments(input);
			if (data != null) {
				Message msg = handler.obtainMessage();
				msg.what = Event.COMMENTSSHOW;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		}
	}
}
