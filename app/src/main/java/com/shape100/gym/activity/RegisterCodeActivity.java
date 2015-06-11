package com.shape100.gym.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shape100.gym.ConstVar;
import com.shape100.gym.R;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.SmsCommit;
import com.shape100.gym.protocol.SmsCommit.UserInfoBean;
import com.shape100.gym.protocol.SmsInvoke;
import com.shape100.gym.protocol.ThreadPool;

/**
 * 验证码验证界面
 * 
 * @author yupu
 * @date 2015年1月30日
 */
public class RegisterCodeActivity extends BaseActivity implements
		OnClickListener {
	private static final String PHONE = "phone";
	private static final String PASSWORD = "password";
	private String phone;
	private String password;
	private TextView phoneView;
	private TextView SmsOK;
	private TextView SmsAgain;
	private EditText codeview;
	boolean flagOK = false, flagAgain = false;
	private int COUNT = 60;

	private Handler timehandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10086) {
				if (COUNT > 0) {
					SmsAgain.setText("重新发送验证码(" + COUNT + "s)");
				} else {
					flagAgain = true;
					changeAgain();
				}
			}
		};
	};

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (COUNT >= 1) {
				COUNT--;
				timehandler.sendEmptyMessage(10086);
				timehandler.postDelayed(runnable, 1000);
			}
		}
	};

	public static void StartAction(Context context, String phone,
			String password) {
		Intent intent = new Intent(context, RegisterCodeActivity.class);
		intent.putExtra(PHONE, phone);
		intent.putExtra(PASSWORD, password);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_reg_code);
		((TextView) findViewById(R.id.tv_title)).setText("验证码");
		findViewById(R.id.tv_back).setVisibility(View.GONE);
		init();
		
		ThreadPool.getInstance().execute(new SmsInvoke(new EventCode(), phone));
		// new SmsInvoke(new EventCode(), phone).start();
		timehandler.postDelayed(runnable, 1000);
	}

	private void init() {
		phone = getIntent().getStringExtra(PHONE);
		password = getIntent().getStringExtra(PASSWORD);
		phoneView = (TextView) findViewById(R.id.tv_act_code_phone);
		phoneView.setText(phone.substring(0, 3) + "  " + phone.substring(3, 7)
				+ "  " + phone.substring(7, 11));
		SmsOK = (TextView) findViewById(R.id.tv_act_code_ok);
		SmsOK.setOnClickListener(this);
		SmsAgain = (TextView) findViewById(R.id.tv_act_code_again);
		SmsAgain.setOnClickListener(this);
		codeview = (EditText) findViewById(R.id.et_act_code);
		changeOK();
		changeAgain();
		codeview.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s.toString())) {
					flagOK = false;
				} else {
					flagOK = true;
				}
				changeOK();
			}
		});
	}

	private void changeOK() {
		if (flagOK) {
			SmsOK.setBackgroundResource(R.drawable.theme_corner);
			SmsOK.setTextColor(getResources().getColor(R.color.color_white));
			SmsOK.setClickable(true);
		} else {
			SmsOK.setBackgroundResource(R.drawable.btn_gray);
			SmsOK.setTextColor(getResources().getColor(R.color.font_light_more));
			SmsOK.setClickable(false);
		}
	}

	private void changeAgain() {
		if (flagAgain) {
			SmsAgain.setText("重新发送验证码");
			SmsAgain.setBackgroundResource(R.drawable.theme_corner);
			SmsAgain.setTextColor(getResources().getColor(R.color.color_white));
			SmsAgain.setClickable(true);
		} else {
			SmsAgain.setBackgroundResource(R.drawable.btn_gray);
			SmsAgain.setTextColor(getResources().getColor(
					R.color.font_light_more));
			SmsAgain.setClickable(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_act_code_ok:
			String code = codeview.getText().toString().replaceAll(" ", "");
			UserInfoBean userInfoBean = new UserInfoBean();
			userInfoBean.mCode = code;
			userInfoBean.mPhone = phone;
			userInfoBean.mPassword = password;
			new SmsCommit(new EventProtrol(), userInfoBean).start();
			break;
		case R.id.tv_act_code_again:
			ThreadPool.getInstance().execute(new SmsInvoke(new EventCode(), phone));
			//new SmsInvoke(new EventCode(), phone).start();
			flagAgain = false;
			COUNT = 60;
			changeAgain();
			timehandler.postDelayed(runnable, 1000);
			break;
		default:
			break;
		}

	}

	class EventProtrol extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_FAILURE:
				if (msg.arg1 == 400) {
					Toast.makeText(RegisterCodeActivity.this, "手机号已注册",
							Toast.LENGTH_SHORT).show();
				} else if (msg.arg1 == 403) {
					Toast.makeText(RegisterCodeActivity.this, "验证码不正确",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(RegisterCodeActivity.this, "请检查网络是否正确",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case ConstVar.HANDLER_MSG_SUCCESS:
				RecommendActivity.ActionStart(RegisterCodeActivity.this, phone,
						password);
				finish();
				break;

			default:
				break;
			}
		}
	}

	class EventCode extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_FAILURE:
				Toast.makeText(RegisterCodeActivity.this, "验证码发送失败!",
						Toast.LENGTH_SHORT).show();
				break;

			case ConstVar.HANDLER_MSG_ERROR:
				Toast.makeText(RegisterCodeActivity.this, "请检查网络是否正确！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
}
