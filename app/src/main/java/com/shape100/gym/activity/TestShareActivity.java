package com.shape100.gym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.shape100.gym.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class TestShareActivity extends ActionBarActivity {

	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	public static void ActionStart(Activity activity) {
		Intent intent = new Intent(activity, TestShareActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		com.umeng.socialize.utils.Log.LOG = true;
		setContentView(R.layout.act_service);
		init();
		// 设置分享图片, 参数2为图片的url地址
		// mController.setShareMedia(new UMImage(this,
		// "http://www.baidu.com/img/bdlogo.png"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用新浪SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void init() {
		// 设置分享内容
		mController
				.setShareContent("分享在这里");
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		String appID = "wxb053d62390ca43c2";
		String appSecret = "34f1e5fce81ce2b2e00ace3c5ddf6cbf";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(TestShareActivity.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(TestShareActivity.this,
				appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent("我们是shape100，为了你的健身服务");
		// 设置title
		weixinContent.setTitle("shape100哈哈");
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl("http://www.shape100.com");
		// 设置分享图片
		weixinContent.setShareImage(new UMImage(this, R.drawable.ic_launcher));
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("我们是有型100，为了你的健身服务");
		// 设置朋友圈title
		circleMedia.setTitle("给个面子");
		circleMedia.setShareImage(new UMImage(this, R.drawable.ic_launcher));
		circleMedia.setTargetUrl("http://www.shape100.com");
		mController.setShareMedia(circleMedia);

		addQQQZonePlatform();

	}

	/**
	 * 添加QQ以及QQ空间平台
	 * 
	 * @author yupu
	 * @date 2015年1月16日
	 */
	private void addQQQZonePlatform() {
		String appId = "1103845476";
		String appKey = "96AqvTRYBpplXSdp";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
				TestShareActivity.this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.shape100.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				TestShareActivity.this, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("share test");
		qzone.setTargetUrl("http://www.shape100.com");
		qzone.setTitle("QZone");
		qzone.setShareMedia(new UMImage(TestShareActivity.this,
				R.drawable.ic_launcher));
		// qzone.setShareMedia(uMusic);
		mController.setShareMedia(qzone);

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent("来自有型一百的分享内容");
		qqShareContent.setTitle("hello, title");
		qqShareContent.setShareMedia(new UMImage(TestShareActivity.this,
				R.drawable.ic_launcher));
		qqShareContent.setTargetUrl("http://www.shape100.com");
		mController.setShareMedia(qqShareContent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.share1:
			share();
			break;
		case R.id.share2:
			share1();
			break;
		case R.id.share3:
			mController.openShare(TestShareActivity.this, false);
			break;
		case R.id.share4:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void share1() {
		mController.postShare(this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				Toast.makeText(TestShareActivity.this, "正在分享...",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int stCode,
					SocializeEntity entity) {
				if (stCode == StatusCode.ST_CODE_SUCCESSED) {
					Toast.makeText(TestShareActivity.this, "分享成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TestShareActivity.this,
							"分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private void share() {
		mController.postShare(this, SHARE_MEDIA.SINA, new SnsPostListener() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				if (eCode == 200) {
					Toast.makeText(TestShareActivity.this, "分享成功.",
							Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(TestShareActivity.this,
							"分享失败[" + eCode + "] " + eMsg, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

}
