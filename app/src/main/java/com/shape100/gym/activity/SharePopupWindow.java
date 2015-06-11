package com.shape100.gym.activity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.shape100.gym.model.ClubBean;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class SharePopupWindow extends BaseActivity implements OnClickListener {

	private Intent intent;
	private ClubBean mClubBean;
	private File picFile;
	// 首先在您的Activity中添加如下成员变量
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("shape100.shareClub");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pop_window_share);

		intent = getIntent();
		mClubBean = (ClubBean) intent.getSerializableExtra("clubbean");
		findViewById(R.id.btn_pop_share_wx).setOnClickListener(this);
		findViewById(R.id.btn_pop_share_wb).setOnClickListener(this);
		findViewById(R.id.btn_pop_share_friends).setOnClickListener(this);
		findViewById(R.id.pop_share_layout).setOnClickListener(this);
		initShare();
		mController.registerListener(snsPostListener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mController.unregisterListener(snsPostListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		/** 使用新浪SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

		if (data.getExtras() != null)
			intent.putExtras(data.getExtras());
		if (data.getData() != null)
			intent.setData(data.getData());
		setResult(1, intent);
		finish();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_share_layout:
			Toast.makeText(getApplicationContext(), "点击空白处返回！",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_pop_share_wx:
			mController.postShare(SharePopupWindow.this, SHARE_MEDIA.WEIXIN,
					snsPostListener);
			finish();
			break;
		case R.id.btn_pop_share_wb:
			mController.postShare(SharePopupWindow.this, SHARE_MEDIA.SINA,
					snsPostListener);
			finish();
			break;
		case R.id.btn_pop_share_friends:
			mController.postShare(SharePopupWindow.this,
					SHARE_MEDIA.WEIXIN_CIRCLE, snsPostListener);
			finish();
			break;
		default:
			setResult(1, intent);

		}

	}

	private void initShare() {
		picFile = new File(FileUtils.getImagePath() + MainName.LOGO_JPG);
		String url = "http://m.shape100.com"; // mClubBean.getHomepageUrl();

		if (url.equals("")) {
			url = "http://m.shape100.com";
		}

		String content = mClubBean.getName() + "\n" + "电话:"
				+ mClubBean.getPhone() + "\n" + "地址:" + mClubBean.getAddress()
				+ "    " + url;
		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		// 设置分享内容
		mController.setShareContent(content);
		// mController.setShareImage(new UMImage(SharePopupWindow.this,
		// mClubBean.getLogoUrl()));

		// MainApplication.APP_ID_WX; //MainApplication.APP_SECREAT_WX;//

		String appID_WX = MainApplication.APP_ID_WX; // "wxb053d62390ca43c2";
		String appSecret_WX = MainApplication.APP_SECREAT_WX;// "34f1e5fce81ce2b2e00ace3c5ddf6cbf";

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(SharePopupWindow.this,
				appID_WX, appSecret_WX);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(SharePopupWindow.this,
				appID_WX, appSecret_WX);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(content);
		// 设置title
		weixinContent.setTitle(mClubBean.getName());
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(url);
		// 设置分享图片
		weixinContent.setShareImage(new UMImage(this, mClubBean.getLogoUrl()));
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		// 设置朋友圈title
		circleMedia.setTitle(mClubBean.getName());
		// circleMedia.setShareImage(new UMImage(this, picFile));
		circleMedia.setTargetUrl(url);
		mController.setShareMedia(circleMedia);

		// 支持QQ
		String appId_QQ = "1103845476";
		String appKey_QQ = "96AqvTRYBpplXSdp";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(SharePopupWindow.this,
				appId_QQ, appKey_QQ);
		qqSsoHandler.setTargetUrl(url);
		qqSsoHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(content);
		qqShareContent.setTitle("hello, title");
		qqShareContent
				.setShareMedia(new UMImage(SharePopupWindow.this, picFile));
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
	}

	SnsPostListener snsPostListener = new SnsPostListener() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			Toast.makeText(SharePopupWindow.this, "正在打开分享...",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
			// TODO Auto-generated method stub
			if (arg1 == 200) {
				Toast.makeText(SharePopupWindow.this, "分享成功", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(SharePopupWindow.this, "分享失败", Toast.LENGTH_LONG)
						.show();
			}
		}
	};

}
