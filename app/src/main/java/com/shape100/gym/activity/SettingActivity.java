/**    
 * file name：SettingActivity.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-24 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.RemovePhone;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.DatabaseHelper;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.UserInfo;
import com.zcw.togglebutton.ToggleButton;

/**
 * 
 * project：shape100 class：SettingActivity desc： author：zpy zpy@98ki.com create
 * date：2014-11-24 下午12:13:21 modify author: zpy update date：2014-11-24
 * 下午12:13:21 update remark：
 * 
 * @version
 * 
 */
public class SettingActivity extends SlideActivity implements OnClickListener {
	private ToggleButton toggleBtn;
	private Button mLogoutBtn;
	private RelativeLayout mAboutLyt;
	private RelativeLayout mPasswordLyt;

	private static final Logger log = Logger.getLogger("SettingActivity");

	public static void ActionStart(Activity activity) {
		Intent intent = new Intent(activity, SettingActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		log.d("onCreate!");

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		((TextView) findViewById(R.id.tv_title)).setText(getResources()
				.getString(R.string.setting));
		mLogoutBtn = (Button) findViewById(R.id.btn_setting_logout);
		mAboutLyt = (RelativeLayout) findViewById(R.id.lyt_frag_owner_setting_about);
		mPasswordLyt = (RelativeLayout) findViewById(R.id.lyt_frag_owner_setting_password);
		mLogoutBtn.setOnClickListener(this);
		mPasswordLyt.setOnClickListener(this);
		mAboutLyt.setOnClickListener(this);
		findViewById(R.id.tv_back).setOnClickListener(this);
		findViewById(R.id.lyt_frag_owner_setting_service).setOnClickListener(
				this);
		findViewById(R.id.lyt_frag_owner_setting_notification)
				.setOnClickListener(this);
		findViewById(R.id.lyt_frag_owner_setting_feedback).setOnClickListener(
				this);
		findViewById(R.id.btn_test).setOnClickListener(this);

		//
		// //切换开关
		// toggleBtn.toggle();
		// //开关切换事件
		// toggleBtn.setOnToggleChanged(new OnToggleChanged(){
		// @Override
		// public void onToggle(boolean on) {
		// }
		// });
		//
		// toggleBtn.setToggleOn();
		// toggleBtn.setToggleOff();
	}

	@Override
	protected void onStart() {
		log.d("onStart!");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		log.d("onRestart!");
		super.onRestart();
	}

	@Override
	protected void onPause() {
		log.d("onPause!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		log.d("onStop!");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		log.d("onDestroy!");
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting_logout:
			// clear SP and DB
			AppConfig.getInstance().setUserId(0);
			DatabaseHelper dbHelper = DatabaseHelper
					.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.close();
			MainApplication.sContext.deleteDatabase("gym.db");
			// jump jump jump
			Intent intent = new Intent(this, LoginActivity.class);
			Bundle bundle = intent.getExtras();
			startActivity(intent);
			MainApplication.activityActivity.pop().finish();
			finish();
			break;
		case R.id.lyt_frag_owner_setting_about:
			intent = new Intent(this, SettingAboutActivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_frag_owner_setting_password:
			intent = new Intent(this, SettingPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.lyt_frag_owner_setting_service:
			ServiceAndSecretActicvty.ActionStart(this);
			break;
		case R.id.lyt_frag_owner_setting_notification:
			// RecommendActivity.ActionStart(SettingActivity.this);
			new RemovePhone(new EventResult()).start();
			break;
		case R.id.lyt_frag_owner_setting_feedback:
			setUserMode();
			break;
		case R.id.btn_test:
			TestActivity.Start(this);
			break;
		}
	}

	/**
	 * 设置用户反馈模型
	 * 
	 * @author yupu
	 * @date 2015年3月11日
	 */
	private void setUserMode() {
		final FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
		UserInfo user = agent.getUserInfo();
		Map<String, String> contact = user.getContact();
		if (contact == null) {
			contact = new HashMap<String, String>();
		}

		contact.put("userid", AccountUtil.getAccount().mUserId + "");
		contact.put("昵称", AccountDetailUtil.getAccountDetailBean().getName());
		int gender = AccountDetailUtil.getAccountDetailBean().getGender();
		contact.put("性别", gender == 0 ? "女" : gender == 1 ? "男" : "未知");
		contact.put("手机号", AppConfig.getInstance().getMobile());

		user.setContact(contact);
		agent.setUserInfo(user);
		new Thread(new Runnable() {

			@Override
			public void run() {
				agent.updateUserInfo();
			}
		}).start();
		agent.startFeedbackActivity();
	}

	class EventResult extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				Toast.makeText(SettingActivity.this, "注销成功", Toast.LENGTH_LONG)
						.show();
				AppConfig.getInstance().setUserId(0);
				if (MainApplication.sContext.deleteDatabase("gym.db")) {
					LoginActivity.ActionStart(SettingActivity.this);
					finish();
				}
				MainApplication.activityActivity.pop().finish();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				Toast.makeText(SettingActivity.this, "注销失败", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		}
	}

}
