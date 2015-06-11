package com.shape100.gym.activity;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.RegNotification;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.protocol.VerifyCredentials;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends BaseActivity implements
		AMapLocationListener {
	private static final Logger log = Logger.getLogger("SplashActivity");

	private final int MSG_GOTOMAIN = 1;
	private final int MSG_GOTOGUIDE = 2;
	private LocationManagerProxy mLocationManagerProxy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// 云测信息
		// TestinAgent.init(this, "7948d11e8468e6630f0753bb8c15555c", "云测平台测试");
		// TestinAgent.setLocalDebug(true);

		MobclickAgent.updateOnlineConfig(this); // 发送统计数据
		AnalyticsConfig.setChannel(getChannel()); // 统计渠道 www.shape100.com test
		if (!AppConfig.getInstance().getDeviceToken().equals("null")) {
			ThreadPool.getInstance().execute(
					new RegNotification(null, AppConfig.getInstance()
							.getDeviceToken(), "Android"));
		}

		initGPS(); // 获取经纬度

		if (AppConfig.getInstance().isFirstApp()) {
			mUiHandler.sendEmptyMessageDelayed(MSG_GOTOGUIDE, 1000);
		} else {
			mUiHandler.sendEmptyMessageDelayed(MSG_GOTOMAIN, 1000);
		}
	}

	private String getChannel() {
		ApplicationInfo ai = getApplicationInfo();
		String path = ai.sourceDir;
		String ret = null;
		try {
			ZipFile zipFile = new ZipFile(path);
			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
				String name = zipEntry.getName();
				if (name.startsWith("META-INF/spchannel")) {
					ret = name;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(ret)) {
			String[] sp = ret.split("_");
			if (sp != null) {
				return sp[1];
			}
		}
		return "unknow";
	}

	private void initGPS() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);

		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 20 * 1000, 15, this);

		mLocationManagerProxy.setGpsEnable(true);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_GOTOMAIN:
				doStartMain();
				break;
			case MSG_GOTOGUIDE:
				doStartGuide();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private void doStartMain() {
		if (AppConfig.getInstance().getUserId() != 0) {
			ThreadPool.getInstance().execute(
					new VerifyCredentials(new EventProtocolHandler()));
		} else {
			LoginActivity.ActionStart(SplashActivity.this);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
	}

	private void doStartGuide() {
		Intent intent = new Intent(this, GuideActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	private class EventProtocolHandler extends ProtocolHandler {
		Intent intent;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Event.VERIFYCREDENTAIL:
				MainActivity.ActionStart(SplashActivity.this, 0);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				finish();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				Toast.makeText(SplashActivity.this, "认证失败请重新登录！",
						Toast.LENGTH_SHORT).show();
				LoginActivity.ActionStart(SplashActivity.this);
				finish();
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				MainActivity.ActionStart(SplashActivity.this, 0);
				finish();
				Toast.makeText(SplashActivity.this, "请检查网络是否正确！",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 获取位置信息
			Double geoLat = amapLocation.getLatitude();
			Double geoLng = amapLocation.getLongitude();
			AppConfig.getInstance().setCurrLat(geoLat + "");
			AppConfig.getInstance().setCurrLon(geoLng + "");
		}

	}

}
