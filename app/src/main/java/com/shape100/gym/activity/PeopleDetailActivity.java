package com.shape100.gym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.SysSettings;
import com.shape100.widget.XScrollview;

/**
 * 个人中心展示
 * 
 * @author yupu
 * @date 2015年3月18日
 */
public class PeopleDetailActivity extends SlideActivity implements
		OnClickListener {
	private static final String KEY_DATA = "account";
	private AccountDetailBean mAccountDetailBean;
	private static final Logger log = Logger.getLogger("FragmentOwner");
	private TextView mNameView;
	private TextView mBMIView;
	private RelativeLayout mFavoriteCoachLyt;
	private RelativeLayout mFavoriteClassLyt;
	private RelativeLayout mSettingLyt;
	private ImageView mHeadView, headBgView;
	private XScrollview XScrollview;

	public static void StartAction(Activity activity, AccountDetailBean account) {
		Intent intent = new Intent(activity, PeopleDetailActivity.class);
		intent.putExtra(KEY_DATA, account);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_owner);
		mAccountDetailBean = (AccountDetailBean) getIntent()
				.getSerializableExtra(KEY_DATA);
		init();
	}

	/**
	 * 初始化个人数据
	 * 
	 * @author yupu
	 * @date 2015年3月18日
	 */
	private void init() {
		headBgView = (ImageView) findViewById(R.id.img_head_bg);
		// 设置阻尼效果
		XScrollview = (XScrollview) findViewById(R.id.xscrollview);
		XScrollview.setImageView(headBgView);

		mSettingLyt = (RelativeLayout) findViewById(R.id.lyt_frag_owner_setting);
		mNameView = (TextView) findViewById(R.id.tv_frag_owner_name);
		mBMIView = (TextView) findViewById(R.id.tv_frag_owner_bmi);
		mHeadView = (ImageView) findViewById(R.id.iv_frag_owner_head);
		mFavoriteCoachLyt = (RelativeLayout) findViewById(R.id.lyt_frag_owner_favorite_coach);
		mFavoriteClassLyt = (RelativeLayout) findViewById(R.id.lyt_frag_owner_favorite_class);

		findViewById(R.id.tv_frag_owner_mod).setOnClickListener(this);
		mFavoriteCoachLyt.setOnClickListener(this);
		mFavoriteClassLyt.setOnClickListener(this);
		mSettingLyt.setOnClickListener(this);
		mNameView.setText(mAccountDetailBean.getName());
		mBMIView.setText("BMI:"
				+ SysSettings.countBMI(mAccountDetailBean.getHeight(),
						mAccountDetailBean.getWeight()) + "");

		mNameView.setOnClickListener(this);
		mBMIView.setOnClickListener(this);
		mHeadView.setOnClickListener(this);

		loadImage();

	}

	@Override
	public void onResume() {
		log.d("onResume!");
		if (AppConfig.getInstance().getUserId() != 0) {
			mAccountDetailBean = AccountDetailUtil.getAccountDetailBean();
			mAccountDetailBean.setProfileImageUrl(mAccountDetailBean
					.getProfileImageUrl().replace("thumbnail", "square"));
			mNameView.setText(mAccountDetailBean.getName());
			mBMIView.setText("BMI:"
					+ SysSettings.countBMI(mAccountDetailBean.getHeight(),
							mAccountDetailBean.getWeight()) + "");
			loadImage();
		}
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lyt_frag_owner_setting:
			SettingActivity.ActionStart(PeopleDetailActivity.this);
			break;
		case R.id.iv_frag_owner_head:
			BigPictureActivity.ActionStart(PeopleDetailActivity.this,
					mAccountDetailBean.getProfileImageUrl());
			break;
		case R.id.tv_frag_owner_mod:
			OwnerSettingActivity.ActionStart(PeopleDetailActivity.this);
			break;
		case R.id.lyt_frag_owner_favorite_coach:
			FavoriteCoachActivity.ActionStart(PeopleDetailActivity.this,
					mAccountDetailBean.getUserId());
			break;
		case R.id.lyt_frag_owner_favorite_class:
			FavoriteClassActivity.ActionStart(PeopleDetailActivity.this,
					mAccountDetailBean.getUserId());
			break;
		}
	}

	public void loadImage() {
		ImageLoader.getInstance().displayImage(
				mAccountDetailBean.getProfileImageUrl(),
				mHeadView,
				MainApplication.getInstance().getDisplayImageOptions(
						R.drawable.ic_unknown));
	}
}
