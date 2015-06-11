package com.shape100.gym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.Utils;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	/* View */
	private EditText mPhoneView;
	private EditText mPasswordView;
	private TextView mRegisterView;
	/* Data */
	private String phone;
	private String password;

	public static void ActionStart(Activity activity) {
		Intent intent = new Intent(activity, RegisterActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		mPhoneView = (EditText) findViewById(R.id.reg_phonenumber);
		mPasswordView = (EditText) findViewById(R.id.reg_password);

		mRegisterView = (TextView) findViewById(R.id.reg_register);

		mRegisterView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_register:
			AppConfig.getInstance()
					.setUserName(mPhoneView.getText().toString());
			doRegister();
			break;
		}
	}

	/**
	 * 验证码
	 * 
	 * @author yupu
	 * @date 2015年1月30日
	 */

	private void doRegister() {
		phone = mPhoneView.getText().toString();
		password = mPasswordView.getText().toString();

		if (!Utils.isValidMobile(phone)) {
			Toast.makeText(RegisterActivity.this, "请输入正确的手机号码!",
					Toast.LENGTH_SHORT).show();
		} else if (!Utils.isValidPassword(password)) {
			Toast.makeText(RegisterActivity.this, "密码长度应大于6位！",
					Toast.LENGTH_SHORT).show();
		} else {
			RegisterCodeActivity.StartAction(RegisterActivity.this, phone,
					password);
			finish();
		}
	}
}
