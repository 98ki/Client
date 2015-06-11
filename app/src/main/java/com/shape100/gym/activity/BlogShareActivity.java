package com.shape100.gym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.shape100.gym.R;

/**
 * 设置博客公开程度界面
 * 
 * @author yupu
 * @date 2015年2月6日
 */
public class BlogShareActivity extends SlideActivity implements OnClickListener {
	private static final String CHECKED = "checked";
	public static final int REQUESTCODE = 2;
	private int checked;
	private ImageView checkImg1, checkImg2, checkImg3;

	public static void ActionStart(Activity activity, int check) {
		Intent intent = new Intent(activity, BlogShareActivity.class);
		intent.putExtra(CHECKED, check);
		activity.startActivityForResult(intent, REQUESTCODE);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_blog_sahre);
		initView();
	}

	private void initView() {
		checked = getIntent().getIntExtra(CHECKED, 0);
		findViewById(R.id.layout_blog_public_check1).setOnClickListener(this);
		findViewById(R.id.layout_blog_public_check2).setOnClickListener(this);
		findViewById(R.id.layout_blog_public_check3).setOnClickListener(this);
		checkImg1 = (ImageView) findViewById(R.id.iv_blog_share_check1);
		checkImg2 = (ImageView) findViewById(R.id.iv_blog_share_check2);
		checkImg3 = (ImageView) findViewById(R.id.iv_blog_share_check3);
		checkedPosition();
	}

	private void checkedPosition() {
		checkImg1
				.setImageResource(R.drawable.message_group_creat_check_default);
		checkImg2
				.setImageResource(R.drawable.message_group_creat_check_default);
		checkImg3
				.setImageResource(R.drawable.message_group_creat_check_default);
		switch (checked) {
		case 0:
			checkImg1.setImageResource(R.drawable.message_group_creat_check);
			((TextView) findViewById(R.id.tv_title)).setText("所有人可见");
			break;
		case 2:
			checkImg2.setImageResource(R.drawable.message_group_creat_check);
			((TextView) findViewById(R.id.tv_title)).setText("好友圈可见");
			break;
		case 1:
			checkImg3.setImageResource(R.drawable.message_group_creat_check);
			((TextView) findViewById(R.id.tv_title)).setText("仅自己可见");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_blog_public_check1:
			checked = 0;
			break;
		case R.id.layout_blog_public_check2:
			checked = 2;
			break;
		case R.id.layout_blog_public_check3:
			checked = 1;
			break;
		default:
			break;
		}
		Intent intent = new Intent();
		intent.putExtra("share", checked);
		setResult(REQUESTCODE, intent);
		checkedPosition();
		finish();
	}
}
