/**
 * file name：FindPasswordPActivity.java    
 *
 * @author zpy zpy@98ki.com   
 * @date：2014-12-1
 * Copyright shape100.com Corporation 2014         
 *
 */
package com.shape100.gym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.protocol.AccountPasswordReset;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.SmsInvoke;

/**
 * project：shape100 class：FindPasswordPActivity desc： author：zpy zpy@98ki.com
 * create date：2014-12-1 下午5:28:35 modify author: zpy update date：2014-12-1
 * 下午5:28:35 update remark：
 */
public class FindPasswordActivity extends SlideActivity implements
        OnClickListener {
    /* Data */
    private static final Logger log = Logger
            .getLogger("find password activity");

    /* View */
    private EditText mPhoneText;
    private EditText mVerifyText;
    private EditText mPasswordText;
    private EditText mConfirmText;
    private Button mConfirmBtn;
    private Button mVerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_password);
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
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
            case R.id.btn_findpwd_confirm:
                if (mPasswordText.getText().toString()
                        .equals(mConfirmText.getText().toString())) {
                    String phone = mPhoneText.getText().toString();
                    String code = mVerifyText.getText().toString();
                    String password = mPasswordText.getText().toString();
                    if (password.length() < 6) {
                        Toast.makeText(FindPasswordActivity.this,
                                "密码长度应大于6，请重新输入!", Toast.LENGTH_SHORT).show();
                    } else {
                        new AccountPasswordReset(new EventProtocolHandler(), phone,
                                code, password).start();
                    }
                } else {
                    Toast.makeText(FindPasswordActivity.this, "两次密码输入不一致!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_findpwd_code:
                if (!mPhoneText.getText().toString().equals("")) {
                    doAcquireCode();
                } else {
                    Toast.makeText(FindPasswordActivity.this, "请输入手机号!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_back:
                finish();
                break;

        }

    }

    public void initData() {

    }

    public void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        mPhoneText = (EditText) findViewById(R.id.et_findpwd_phone);
        mVerifyText = (EditText) findViewById(R.id.et_findpwd_code);
        mPasswordText = (EditText) findViewById(R.id.et_findpwd_password);
        mConfirmText = (EditText) findViewById(R.id.et_findpwd_confirm);
        mConfirmBtn = (Button) findViewById(R.id.btn_findpwd_confirm);
        mVerifyBtn = (Button) findViewById(R.id.btn_findpwd_code);
        ((TextView) findViewById(R.id.tv_title)).setText("找回密码");

        mVerifyBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

    }

    private void doAcquireCode() {
        String phone = mPhoneText.getText().toString();

        if (phone.length() != 11) {
            Toast.makeText(FindPasswordActivity.this, "请输入正确的手机号码!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "正在获取，请等待短信通知...", Toast.LENGTH_SHORT).show();
            new SmsInvoke(null, phone).start();
        }
    }

    /**
     * EventProtocolHandler
     *
     * @author zpy zpy@98ki.com
     * @date 2014-11-12 下午8:14:20
     * @version: V1.0
     */
    private class EventProtocolHandler extends ProtocolHandler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstVar.HANDLER_MSG_SUCCESS:
                    Toast.makeText(MainApplication.sContext, "密码修改成功！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindPasswordActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    Toast.makeText(MainApplication.sContext, "密码修改失败！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ConstVar.HANDLER_MSG_ERROR:
                    Toast.makeText(MainApplication.sContext, "请检查网络是否正确！",
                            Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

}
