/**    
 * file name：SettingPasswordActivity.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-26 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shape100.gym.ConstVar;
import com.shape100.gym.R;
import com.shape100.gym.protocol.ChangePassword;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

/**
 * 
 * project：shape100 class：SettingPasswordActivity desc： author：zpy zpy@98ki.com
 * create date：2014-11-26 下午4:41:45 modify author: zpy update date：2014-11-26
 * 下午4:41:45 update remark：
 * 
 * @version
 * 
 */
public class SettingPasswordActivity extends SlideActivity implements
		OnClickListener {
	private EditText mOldPasswdEdit;
	private EditText mNewPasswdEdit;
	private EditText mConfirmPasswdEdit;

	// private Button mConfirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_password);
		mOldPasswdEdit = (EditText) findViewById(R.id.et_setting_password_old);
		mNewPasswdEdit = (EditText) findViewById(R.id.et_setting_password_new);
		mConfirmPasswdEdit = (EditText) findViewById(R.id.et_setting_password_confirm);
		findViewById(R.id.btn_setting_password_confirm)
				.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(R.string.str_change_password));
		findViewById(R.id.tv_back).setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting_password_confirm:
			String oldPassword = mOldPasswdEdit.getText() + "";
			String newPassword = mNewPasswdEdit.getText() + "";
			String confirmPassword = mConfirmPasswdEdit.getText() + "";

			if (TextUtils.isEmpty(oldPassword)) {
				Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
			} else if (!mNewPasswdEdit.getText().toString()
					.equals(mConfirmPasswdEdit.getText().toString())) {
				Toast.makeText(this, "新密码不一致，请重新输入！", Toast.LENGTH_SHORT)
						.show();
			} else {
				ThreadPool.getInstance().execute(
						new ChangePassword(new EventProtocolHandler(),
								oldPassword, newPassword));
			}
			break;
		case R.id.tv_back:
			finish();
			break;
		}
	}

	private class EventProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:

				Toast.makeText(SettingPasswordActivity.this, "修改成功！",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SettingPasswordActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				Toast.makeText(SettingPasswordActivity.this, "修改失败",
						Toast.LENGTH_SHORT).show();
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				break;

			}
		}
	}

}
