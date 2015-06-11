package com.shape100.gym.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.Utils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.protocol.AccountLogin;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private TextView mFindPasswordView;
    private EditText mNameView;
    private EditText mPasswordView;
    private TextView mCommitView;
    private ProgressBar mProgressBar;
    private long curTime;

    public static void ActionStart(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainApplication.activityActivity.add(LoginActivity.this); // 加入任务栈
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_login);
        mNameView = (EditText) findViewById(R.id.login_username);
        mNameView.setText(AppConfig.getInstance().getUserName());
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mCommitView = (TextView) findViewById(R.id.login_commit);
        mFindPasswordView = (TextView) findViewById(R.id.tv_login_findpwd);
        findViewById(R.id.tv_login_register).setOnClickListener(this);
        mCommitView.setOnClickListener(this);
        mFindPasswordView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_commit:
                doCommit();
                break;
            case R.id.tv_login_register:
                RegisterActivity.ActionStart(LoginActivity.this);
                break;
            case R.id.tv_login_findpwd:
                Intent intent = new Intent(this, FindPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (System.currentTimeMillis() - curTime > 2000) {
            curTime = System.currentTimeMillis();
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_exit),
                    Toast.LENGTH_LONG).show();
        } else {
            if (MainApplication.activityActivity.size() != 0) {
                MainApplication.activityActivity.pop().finish();
            }
            super.onBackPressed();
        }
    }

    private void doCommit() {
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (!Utils.isValidMobile(name)) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_phone_nopass), Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_phone_nopass), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mProgressBar.setVisibility(mProgressBar.VISIBLE);
        AppConfig.getInstance().setUserName(mNameView.getText().toString());
        ThreadPool.getInstance().execute(
                new AccountLogin(new EventProtocolHandler(), name, password));
    }

    @SuppressLint("HandlerLeak")
    private class EventProtocolHandler extends ProtocolHandler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Event.VERIFYCREDENTAIL:
                    if (MainApplication.activityActivity.size() != 0) {
                        for (int i = 0; i < MainApplication.activityActivity.size(); i++) {
                            MainApplication.activityActivity.pop().finish();
                        }
                    }

                    // change FragmentClub
                    MainActivity.ActionStart(LoginActivity.this, 3);
                    mProgressBar.setVisibility(mProgressBar.INVISIBLE);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    finish();
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    mProgressBar.setVisibility(mProgressBar.INVISIBLE);
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_check_phone),
                            Toast.LENGTH_SHORT).show();
                    break;
                case ConstVar.HANDLER_MSG_ERROR:
                    mProgressBar.setVisibility(mProgressBar.INVISIBLE);
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_no_net),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
