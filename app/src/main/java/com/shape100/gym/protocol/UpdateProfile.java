/**    
 * file name：UpdateProfile.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-28 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**
 * 
 * project：shape100 class：UpdateProfile desc： author：zpy zpy@98ki.com create
 * date：2014-11-28 下午4:38:23 modify author: zpy update date：2014-11-28 下午4:38:23
 * update remark：
 * 
 * @version
 * 
 */
public class UpdateProfile extends HttpTask {
	private static final Logger log = Logger.getLogger("UpdateProfile");
	private static final String URL = "/account/update_profile.json";
	private AccountDetailBean mAccountDetailBean;

	/**
	 * 
	 * New Instance： UpdateProfile.
	 * 
	 * @param handler
	 * @param ClubBean
	 * 
	 */
	public UpdateProfile(ProtocolHandler handler,
			AccountDetailBean accountDetailBean, boolean isUpdateHead) {
		super(URL, null, handler);
		buildRequestData(accountDetailBean, isUpdateHead);
		mAccountDetailBean = accountDetailBean;
	}

	private void buildRequestData(AccountDetailBean accountDetailBean,
			boolean isFlag) {
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
		auth.addParameter(accountDetailBean.getNameKey(),
				accountDetailBean.getName());
		auth.addParameter(accountDetailBean.getGenderKey(),
				accountDetailBean.getGender() + "");
		auth.addParameter(accountDetailBean.getBirthdayKey(),
				accountDetailBean.getBirthday());
		auth.addParameter(accountDetailBean.getHeightKey(),
				accountDetailBean.getHeight() + "");
		auth.addParameter(accountDetailBean.getWeightKey(),
				accountDetailBean.getWeight() + "");
		auth.addParameter(accountDetailBean.getDescriptionKey(),
				accountDetailBean.getDescription());
		if (isFlag) {
			auth.addParameter(accountDetailBean.getProfileImagePicIdKey(),
					accountDetailBean.getProfileImagePicId() + "");
		}
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
		log.d("httpRspHandler start!");
		
		if (statusCode != HttpTask.SC_OK) {
			log.e("updateProfile出现错误" + FileUtils.getError(input));
			reportFailure(statusCode);
		} else {
			
//			log.d("---------------更新返回信息-----------"
//					+ FileUtils.getError(input));
			
			// save to club table
			mAccountDetailBean=ExtProtocolUtil.parseVerifyResponse(input);
			AccountDetailUtil.saveAccountDetail(mAccountDetailBean);
			AccountBean bean = AccountUtil.getAccount();
			bean.mScreenName = mAccountDetailBean.getName();
			AccountUtil.updateAccount(bean); // 更新一下账户表的名字
			reportSuccess();
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
}