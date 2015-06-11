package com.shape100.gym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.R;
import com.shape100.widget.ScaleImageView;

/**    
 * file name：ClubInfoDetail.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-18 
 * Copyright shape100.com Corporation 2014         
 *    
 */

/**
 * 
 * project：shape100 class：ClubInfoDetail desc： author：zpy zpy@98ki.com create
 * date：2014-11-18 下午5:44:13 modify author: zpy update date：2014-11-18 下午5:44:13
 * update remark：
 * 
 * @version
 * 
 */
public class ClubInfoDetailActivity extends BaseActivity implements
		OnClickListener {
	private TextView mDetailDescView;
	private ScaleImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_clubinfo_detail);
		((TextView) findViewById(R.id.tv_title)).setText(getResources()
				.getString(R.string.clubdetail));
		Intent intent = getIntent();
		String desc = intent.getStringExtra("desc");
		String url = intent.getStringExtra("url");
		mDetailDescView = (TextView) findViewById(R.id.tv_clubinfo_detail_desc);
		image = (ScaleImageView) findViewById(R.id.iv_clubinfo_detail_img);
		findViewById(R.id.tv_back).setOnClickListener(this);
		mDetailDescView.setText("\t\t" + desc);
		ImageLoader.getInstance().displayImage(url, image);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_back) {
			finish();
		}
	}
}
