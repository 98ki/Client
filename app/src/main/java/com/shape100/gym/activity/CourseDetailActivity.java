package com.shape100.gym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.model.ClassBean;

public class CourseDetailActivity extends SlideActivity implements
		OnClickListener {
	private ClassBean mClassBean;
	private TextView mDetailDescView;
	private TextView courseName;

	public static void ActionStart(Activity activity, ClassBean mClassBean) {
		Intent intent = new Intent(activity, CourseDetailActivity.class);
		intent.putExtra("classbean", mClassBean);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coursedetail);
		initData();
		initView();
	}

	public void initData() {
		Bundle bundle = getIntent().getExtras();
		mClassBean = (ClassBean) bundle.getSerializable("classbean");
	}

	public void initView() {
		courseName = (TextView) findViewById(R.id.tv_title);
		findViewById(R.id.tv_back).setOnClickListener(this);
		mDetailDescView = (TextView) findViewById(R.id.tv_coursepage_detail_desc);
		courseName.setText(mClassBean.getClassName().replace("\n", ""));
		String text = mClassBean.getDescription() != null ? mClassBean
				.getDescription() : "这门课程还没有详细介绍哦。。";
		mDetailDescView.setText("\t\t" + text);
		if (mClassBean != null && mClassBean.getPicUrls() != null) {
			int count = (int) (mClassBean.getPicUrls().split(",").length * Math
					.random());
			String url = mClassBean.getPicUrls().split(",")[0].replace(
					"thumbnail", "large");
			ImageLoader.getInstance().displayImage(
					url,
					(ImageView) findViewById(R.id.img_coursepage),
					MainApplication.getInstance().getDisplayImageOptions(
							R.drawable.ic_empty));
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_back) {
			finish();
		}
	}
}
