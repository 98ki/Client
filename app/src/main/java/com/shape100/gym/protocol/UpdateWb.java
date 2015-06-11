package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.entity.StringEntity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import com._98ki.util.FileUtils;
import com._98ki.util.Utils;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class UpdateWb extends HttpTask {
	private static final String URL = "/statuses/update.json";
	private Logger log = Logger.getLogger("UpdateWb");

	private String status, pic_id, rip, in_reply_to_status_id;
	private int visible;
	private float lat, lon;

	public UpdateWb(String status, int visible, String pic_id,
			String in_reply_to_status_id, float lat, float lon, String rip) {
		super(URL, null, null, true);
		this.in_reply_to_status_id = in_reply_to_status_id;
		this.visible = visible;
		this.lat = lat;
		this.lon = lon;
		this.rip = rip;
		this.pic_id = pic_id;
		this.status = Utils.RemoveMore(status);
		// FileUtils.writeString("发布的动态：" + this.status + "\n" + "pic_id:"
		// + pic_id + "\n rip:" + rip + "\n");
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
		auth.addParameter("source", OAuth.APP_KEY);
		auth.addParameter("status",
				Uri.encode(status, "utf-8").replace("*", "%2A"));

		if (visible != 0) {
			auth.addParameter("visible", visible + "");
		}
		if (pic_id != null) {
			auth.addParameter("pic_id", pic_id);
		}
		if (in_reply_to_status_id != null) {
			auth.addParameter("in_reply_to_status_id", in_reply_to_status_id);
		}
		if (lat != 0) {
			auth.addParameter("lat", lat + "");
		}
		if (lat != 0) {
			auth.addParameter("long", lon + "");
		}
		if (rip != null) {
			auth.addParameter("rip", rip);
		}
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

		if (statusCode != HttpTask.SC_OK) {
			// FileUtils.writeString("发布动态错误信息:statuscode--" + statusCode
			// + "------" + FileUtils.getError(input) + "\n");
			log.e("statuscode" + statusCode);
		} else {
			// FileUtils.writeString("发布动态成功,完全木有问题啊:statuscode--" + statusCode
			// + "\n");
			// StringBuilder sb = new StringBuilder();
			// BufferedReader read = new BufferedReader(new InputStreamReader(
			// input));
			// String st;
			// while ((st = read.readLine()) != null) {
			// sb.append(st);
			// }
			// System.out.println("-------------------发布微博---" + sb);
			// Dynamic dynamic = ExtProtocolUtil.parseFriendsTimeLine(input)
			// .get(0);
			reportSuccess();
		}
	}

	private void reportSuccess() {
		// Message msg = Message.obtain();
		// msg.what = Event.UPDATEWB;
		// //msg.obj = dynamic;
		// handler.sendMessage(msg);
		AppConfig.getInstance().setSaveWb("");
		final NotificationManager nm = (NotificationManager) MainApplication
				.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"发布成功..", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getBroadcast(
				MainApplication.getInstance(), 0, new Intent(),
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(MainApplication.getInstance(), "发布成功",
				"", contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知自动消失
		notification.ledARGB = Color.GREEN; // led闪光颜色

		nm.notify(110, notification);

		// 3秒之后自动取消通知
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(4000);
					nm.cancel(110);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

}
