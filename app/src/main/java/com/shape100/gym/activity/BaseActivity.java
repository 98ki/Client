package com.shape100.gym.activity;

import com.shape100.gym.Logger;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * avtivity基类
 * 
 * @author yupu
 * @date 2015年1月5日
 */
public class BaseActivity extends FragmentActivity {
	protected Logger log_y;

	@Override
	protected void onCreate(Bundle arg0) {
		log_y = Logger.getLogger(getClass().getName());
		log_y.i("onCreate", "create");
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		log_y.i("onStart", "start");
		super.onStart();
	}

	@Override
	protected void onResume() {
		log_y.i("onResume", "onResume");
		super.onResume();
		MobclickAgent.onResume(this);  //统计时长
	}

	@Override
	protected void onPause() {
		log_y.i("onPause", "onPause");
		super.onPause();
		MobclickAgent.onPause(this); //统计时长
	}

	@Override
	protected void onStop() {
		log_y.i("onStop", "stop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		log_y.i("onDestroy", "destroy");
		super.onDestroy();
	}
}
