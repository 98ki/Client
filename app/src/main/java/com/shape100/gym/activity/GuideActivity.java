package com.shape100.gym.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;

public class GuideActivity extends BaseActivity implements OnPageChangeListener,
		View.OnClickListener {
	private static final Logger log = Logger.getLogger("GuideActivity");

	private List<ImageView> mDotViews = new ArrayList<ImageView>();
	private LinearLayout mNavView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		if (AppConfig.getInstance().isFirstApp()) {
			AppConfig.getInstance().setFirstApp(false);
		}

		initDotViews();
		initViewPager();
		initButtons();
	}

	private void initDotViews() {
		ImageView iv = (ImageView) findViewById(R.id.guide_dot1);
		mDotViews.add(iv);
		iv = (ImageView) findViewById(R.id.guide_dot2);
		mDotViews.add(iv);
		iv = (ImageView) findViewById(R.id.guide_dot3);
		mDotViews.add(iv);
	}

	private void initViewPager() {
		List<View> views = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(this);
		views.add(inflater.inflate(R.layout.guide01, null));
		views.add(inflater.inflate(R.layout.guide02, null));
		views.add(inflater.inflate(R.layout.guide03, null));
		ViewPagerAdapter adapter = new ViewPagerAdapter(views);

		ViewPager vp = (ViewPager) findViewById(R.id.guide_viewpager);
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(this);
	}

	private void initButtons() {
		mNavView = (LinearLayout) findViewById(R.id.guide_nav);
		findViewById(R.id.tv_preview).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);
	}

	@Override
	public void onPageSelected(int index) {
		int pageSize = mDotViews.size();

		if (index >= 0 && index < pageSize) {
			for (int i = 0; i < pageSize; i++) {
				ImageView iv = mDotViews.get(i);
				iv.setBackgroundResource(R.drawable.dot_white);
			}

			ImageView iv = mDotViews.get(index);
			iv.setBackgroundResource(R.drawable.dot_black);
		}

		if (index == (pageSize - 1)) {
			mNavView.setVisibility(View.VISIBLE);
		} else {
			mNavView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	private class ViewPagerAdapter extends PagerAdapter {

		private List<View> mViews;

		public ViewPagerAdapter(List<View> views) {
			mViews = views;
		}

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View view, int position, Object object) {
			((ViewPager) view).removeView(mViews.get(position));
		}

		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(mViews.get(position), 0);
			return mViews.get(position);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_preview:
			gotoMainPage();
			break;

		case R.id.btn_login:
			gotoLoginPage();
			break;
		case R.id.btn_register:
			gotoRegiserPage();
			// test_copydata();
			break;
		}
	}

	private void test_copydata() {
		File sdRoot = Environment.getExternalStorageDirectory();
		if (Logger.DBG) {
			log.d(sdRoot.getPath());
		}

		String destPath = sdRoot.getPath() + "/gym_test";

		File srcFile = new File("/data/data/com.shape100.gym");
		File destFile = new File(destPath);
		FileUtils.deleteQuietly(destFile);
		try {
			FileUtils.copyDirectory(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gotoMainPage() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void gotoRegiserPage() {
		RegisterActivity.ActionStart(GuideActivity.this);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	private void gotoLoginPage() {
		LoginActivity.ActionStart(GuideActivity.this);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}
}
