package com.shape100.gym.protocol;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com._98ki.util.FileUtils;
import com._98ki.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;
import com.shape100.gym.provider.SysSettings;

public class VerifyCredentials extends HttpTask {
	private static final Logger log = Logger.getLogger("VerifyCredentials");
	private static final String URL = "/account/verify_credentials.json";

	public VerifyCredentials(ProtocolHandler handler) {
		super(URL, null, handler, false);
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
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in VerifyCredentials");
		}

		// System.out.println("-------返回数据测试-------"+FileUtils.test(input)+"------"+statusCode);;

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			/* init Application */
			AccountDetailBean bean = ExtProtocolUtil.parseVerifyResponse(input);
			// save to db;
			AccountDetailUtil.saveAccountDetail(bean);

			// Save userId to SharedPreferenced to note who login
			AppConfig.getInstance().setUserId(bean.getUserId());
			log.e("Verify setSP, userId= " + bean.getUserId());
			mHandler.sendEmptyMessage(Event.VERIFYCREDENTAIL);

			// get club info if joined club
			if (AccountDetailUtil.getUserClub() != 0) {
				ThreadPool.getInstance()
						.execute(
								new ClubShow(mHandler, AccountDetailUtil
										.getUserClub()));
			}

			// if (AccountDetailUtil.getUserClub() == 0) {
			// mHandler.sendEmptyMessage(Event.VERIFYCREDENTAIL);
			// } else {
			// new ClubShow(mHandler, AccountDetailUtil.getUserClub()).start();
			// }

			// [long time] set /shape100/mHeadIcon.jpg
			savePic(bean.getProfileImageUrl());
		}
	}

	/**
	 * 保存头像图片，大图和小图
	 * 
	 * @author yupu
	 * @date 2015年1月8日
	 */
	private void savePic(String url) {
		if (!TextUtils.isEmpty(url)) {
			ImageLoader.getInstance().loadImage(url,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							File file = ImageUtils.bitmapToFile(loadedImage,
									MainApplication.getInstance()
											.getImageCacheDir()
											+ MainName.HEAD_JPG);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
						}
					});
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
