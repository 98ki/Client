/**    
 * file name：SettingAboutActivity.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-24 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com._98ki.util.AppUtils;
import com.shape100.gym.R;

/**
 * 
 * project：shape100 class：SettingAboutActivity desc： author：zpy zpy@98ki.com
 * create date：2014-11-24 下午1:50:32 modify author: zpy update date：2014-11-24
 * 下午1:50:32 update remark：
 * 
 * @version
 * 
 */
public class SettingAboutActivity extends SlideActivity implements
		OnClickListener {
	private TextView mVersionNameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_about);
		((TextView) findViewById(R.id.tv_title)).setText(getResources()
                .getString(R.string.about));
		findViewById(R.id.tv_back).setOnClickListener(this);
		mVersionNameView = (TextView) findViewById(R.id.tv_frag_owner_setting_about_version_name);
		mVersionNameView.setText(AppUtils.getVersion(this) + "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_back) {
			finish();
		}
	}

}
