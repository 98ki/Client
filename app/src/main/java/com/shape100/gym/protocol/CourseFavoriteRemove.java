/**    
 * file name：CourseFavoriteRemove.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-20 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.TimeUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;
import com.shape100.gym.provider.FavoriteUtil;

/**
 * 
 * project：shape100 class：CourseFavoriteRemove desc： author：zpy zpy@98ki.com
 * create date：2014-11-20 下午5:31:10 modify author: zpy update date：2014-11-20
 * 下午5:31:10 update remark：
 * 
 * @version
 * 
 */
public class CourseFavoriteRemove extends HttpTask {
	private static final Logger log = Logger.getLogger("CourseFavoriteremove");
	private static final String URL = "/class/favorites/remove.json";

	/**
	 * 
	 * New Instance： CourseFavoriteRemove.
	 * 
	 * @param handler
	 * @param id
	 *            scheduleID
	 */
	public CourseFavoriteRemove(ProtocolHandler handler, int id) {
		super(URL, null, handler);
		buildRequestData(id);
	}

	private void buildRequestData(int id) {
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
		auth.addParameter("user_schedule_id", id + "");
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

	/**
	 * httpRspHandler update database cache
	 */
	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		log.d("httpRspHandler start!");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			StringBuilder sb = new StringBuilder();
			String sa;
			while ((sa = reader.readLine()) != null) {
				sb.append(sa);
			}

			System.out.println("-----------------返回结果--------------" + sb);
			reader.close();
			
		} catch (Exception e) {

		}

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			reportSuccess();
			String[] startEndDate = TimeUtils.getWeekDate();
			new CourseFavorites(mHandler, startEndDate[0], startEndDate[1])
					.start();
		}
	}

	private void reportSuccess() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(Event.ADDORREMOVE);
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
