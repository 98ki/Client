/**    
 * file name：CoachPageActivity.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-4 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shape100.gym.BuildConfig;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.R.id;
import com.shape100.gym.adapter.TimeAdapter;
import com.shape100.gym.model.User;
import com.shape100.gym.model.UserOtherInfo;
import com.shape100.gym.protocol.Friendships;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.protocol.UserShow;
import com.shape100.widget.ListviewForScrollview;
import com.shape100.widget.XScrollview;

/**
 * 
 * project：shape100 class：CoachPageActivity desc： author：zpy zpy@98ki.com create
 * date：2014-12-4 下午4:35:57 modify author: zpy update date：2014-12-4 下午4:35:57
 * update remark：
 * 
 * @version
 * 
 */
public class CoachPageActivity extends SlideActivity implements OnClickListener {

	/* Data */
	private static final String USER_KEY = "userid";
	private static final Logger log = Logger.getLogger("CoachPageActivity");
	public static final String COACHBEAN = "coachbean";
	public static final String FROM_FAVORITE = "favorite";
	private DisplayImageOptions options;

	/* View */
	private ImageView mCoachHeadView, headBgView;
	private TextView mNameView;
	private RelativeLayout mFavoriteCoachLyt;
	private RelativeLayout mFavoriteClassLyt;

	private XScrollview XScrollview;
	private ListviewForScrollview listview;
	private TimeAdapter adapter;

	private String headUrl;
	private String name;
	private String certification;
	private long user_id;
	private TextView btn_concern;
	private boolean isConcern;
	private User user;
	private UserOtherInfo userInfo;
	private TextView tv_concern_count, tv_funs_count, tv_status_count;
	private View certifiView;

	public static void ActionStart(Context context, long user_id) {
		Intent intent = new Intent(context, CoachPageActivity.class);
		intent.putExtra(USER_KEY, user_id);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coachpage);
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lyt_coachpage_favorite_coach:
			if (user_id != 0) {
				FavoriteCoachActivity.ActionStart(CoachPageActivity.this,
						user_id);
			}
			break;
		case R.id.lyt_coachpage_favorite_class:
			if (user_id != 0) {
				FavoriteClassActivity.ActionStart(CoachPageActivity.this,
						user_id);
			}
			break;
		case R.id.iv_coachpage_head:
			if (!TextUtils.isEmpty(headUrl)) {
				BigPictureActivity.ActionStart(this, headUrl);
			}
			break;
		case R.id.lyt_coachpage_certify:
			showlist();
			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_concern_btn:
			btn_concern.setClickable(false);
			if (!isConcern) {
				ThreadPool.getInstance().execute(
						new Friendships(user_id, new ResultHandler(),
								Friendships.CREAT));
			} else {
				ThreadPool.getInstance().execute(
						new Friendships(user_id, new ResultHandler(),
								Friendships.DESTORY));
			}
			break;
		case R.id.lyt_coachpage_owner_dynamic:
			DynamicActivity.ActionStart(CoachPageActivity.this, user_id);
			break;
		case R.id.view_concern_count:
			FunsOrConcernActivity.StartAction(CoachPageActivity.this, user_id,
					FunsOrConcernActivity.FOLLOWINGS);
			break;
		case R.id.view_funs_count:
			FunsOrConcernActivity.StartAction(CoachPageActivity.this, user_id,
					FunsOrConcernActivity.FOLLOWS);
			break;
		case R.id.view_dynamic:
			DynamicActivity.ActionStart(CoachPageActivity.this, user_id);
			break;
		}
	}

	private void showlist() {
		if (listview.getVisibility() == View.VISIBLE) {
			listview.setVisibility(View.GONE);
		} else {
			listview.setVisibility(View.VISIBLE);
		}
	}

	public void initData() {
		name = userInfo.getName();
		headUrl = userInfo.getProfileImageUrl();
		certification = userInfo.getCertification().replace("|", "yupu");
		if (!TextUtils.isEmpty(certification)) {
			certifiView.setVisibility(View.VISIBLE);
		}
		isConcern = userInfo.isFollowing();
		changeBtn();
		setData();
	}

	public void initView() {
		listview = (ListviewForScrollview) findViewById(R.id.list_zizhi);
		// 初始化阻尼效果
		headBgView = (ImageView) findViewById(R.id.img_head_bg);
		XScrollview = (XScrollview) findViewById(R.id.xscrollview);
		XScrollview.setImageView(headBgView);

		mNameView = (TextView) findViewById(R.id.tv_coachpage_name);
		mCoachHeadView = (ImageView) findViewById(R.id.iv_coachpage_head);
		mFavoriteCoachLyt = (RelativeLayout) findViewById(R.id.lyt_coachpage_favorite_coach);
		mFavoriteClassLyt = (RelativeLayout) findViewById(R.id.lyt_coachpage_favorite_class);
		findViewById(R.id.lyt_coachpage_owner_dynamic).setOnClickListener(this);
		tv_status_count = (TextView) findViewById(R.id.tv_dynamic);
		tv_concern_count = (TextView) findViewById(R.id.tv_concern_count);
		tv_funs_count = (TextView) findViewById(R.id.tv_funs_count);

		mFavoriteCoachLyt.setOnClickListener(this);
		mFavoriteClassLyt.setOnClickListener(this);
		mCoachHeadView.setOnClickListener(this);
		findViewById(R.id.view_concern_count).setOnClickListener(this);
		findViewById(R.id.view_dynamic).setOnClickListener(this);
		findViewById(R.id.view_funs_count).setOnClickListener(this);

		certifiView = findViewById(R.id.lyt_coachpage_certify);
		certifiView.setOnClickListener(this);
		findViewById(R.id.tv_back).setOnClickListener(this);

		btn_concern = (TextView) findViewById(R.id.tv_concern_btn);

		btn_concern.setOnClickListener(this);
		mNameView.setOnClickListener(this);

		user_id = getIntent().getLongExtra(USER_KEY, 0);
		// init cache image options
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_unknown)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ThreadPool.getInstance().execute(
				new UserShow(user_id, new ResultHandler(), 1));
	}

	/**
	 * 改变关注按钮的状态
	 * 
	 * @author yupu
	 * @date 2015年3月19日
	 */
	private void changeBtn() {
		if (!isConcern) {
			btn_concern.setBackgroundResource(R.drawable.theme_corner);
			btn_concern.setText(getResources().getString(R.string.concern_str));
		} else {
			btn_concern.setBackgroundResource(R.drawable.gray_corner);
			btn_concern.setText(getResources().getString(
					R.string.concern_cancle_str));
		}
	}

	private void setData() {
		mNameView.setText(name);
		tv_concern_count.setText(userInfo.getFollowings_count() + "");
		tv_funs_count.setText(userInfo.getFollowers_count() + "");
		tv_status_count.setText(userInfo.getStatuses_count() + "");
		loadImage();
		String[] zizhi = certification.split("yupu");
		adapter = new TimeAdapter(CoachPageActivity.this, zizhi, true);
		listview.setAdapter(adapter);
	}

	private void loadImage() {
		// set Coach Head
		ImageLoader.getInstance().displayImage(headUrl, mCoachHeadView,
				options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						mCoachHeadView.setImageResource(R.drawable.ic_unknown);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
					}
				});
	}

	class ResultHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Event.CREAT_CONCERN:
				user = (User) msg.obj;
				isConcern = !isConcern;
				btn_concern.setClickable(true);
				changeBtn();
				ThreadPool.getInstance().execute(
						new UserShow(user_id, new ResultHandler(), 1));
				break;
			case Event.CANCEL_CONCERN:
				user = (User) msg.obj;
				isConcern = !isConcern;
				btn_concern.setClickable(true);
				changeBtn();
				ThreadPool.getInstance().execute(
						new UserShow(user_id, new ResultHandler(), 1));
				break;
			case Event.USERINFO:
				userInfo = (UserOtherInfo) msg.obj;
				if (userInfo != null) {
					initData();
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

}
