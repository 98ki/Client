package com.shape100.gym.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com._98ki.util.LogoUtils;
import com._98ki.util.Utils;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.shape100.gym.adapter.ImagePagerAdapter;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.model.ClassBean;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.ClassShow;
import com.shape100.gym.protocol.CourseFavoriteAdd;
import com.shape100.gym.protocol.CourseFavoriteRemove;
import com.shape100.gym.protocol.FollowsClass;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.ClassUtil;
import com.shape100.gym.provider.ClubUtil;
import com.shape100.gym.provider.CourseFavoriteUtil;
import com.shape100.gym.provider.CourseUtil;
import com.viewpagerindicator.CirclePageIndicator;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;

/**
 * 
 * @author yupu
 * @date 2015年3月20日
 */
public class CoursePageActivity extends SlideActivity implements
		OnClickListener {
	private SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final int COUNT = 20;

	/* Data */
	private static final Logger log = Logger.getLogger("CoursePageActivity");
	private static final String COURSEBEAN = "coursebean";
	private static final String DATE = "date";
	private DisplayImageOptions options;

	private CourseBean mCourseBean;
	private ClassBean mClassBean;
	private AccountDetailBean mCoachBean;

	/**
	 * How to judge whether the course is my favorite course, It's a question...
	 * now, we according to classId、Coach Id、day of week、 start time. fuck the
	 * four params...
	 */

	private boolean mIsMyFavoriteCourse;
	private boolean mIsAlarmed = false;
	/* View */
	private AutoScrollViewPager viewPager;
	private CirclePageIndicator indicator;

	private ImageView mCoachHeadView;
	private TextView mCoachNameView;
	private TextView mClassNameView;
	private TextView mDescView, yuyue;
	private Button mFavoriteCourseBtn;
	private ImageButton mAlarmBtn;

	private long startTime;
	private long endTime;

	private StringBuilder shareText = new StringBuilder();
	private String date;

	private LinearLayout contentLayout;
	private CircleImageView circleImageView;
	private ArrayList<UserInfo> userInfos;
	private TextView concernCount;
	private ImageView moreConcernView;

	public static void ActionStart(Context activity, CourseBean bean) {
		Intent intent = new Intent(activity, CoursePageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(COURSEBEAN, bean);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coursepage);
		// init data
		initData();
		// init View
		initView();

		// cache data first
		// fillDataFromDB();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_coursepage_setalarm) {
			setAlarm();

		} else if (i == R.id.layout_coach_detail) {
			if (mCoachBean != null) {
				log.e("________________"+mCoachBean.getUserId());
				CoachPageActivity.ActionStart(this, mCoachBean.getUserId());
			}

		} else if (i == R.id.layout_coursepage_more) {
			if (mClassBean != null) {
				CourseDetailActivity.ActionStart(this, mClassBean);
			}

		} else if (i == R.id.btn_coursepage_favorite_course) {
			if (mIsMyFavoriteCourse) {
				ThreadPool.getInstance().execute(
						new CourseFavoriteRemove(
								new FavoritesProtocolHandler(), mCourseBean
								.getId()));
			} else {
				ThreadPool.getInstance().execute(
						new CourseFavoriteAdd(new FavoritesProtocolHandler(),
								mCourseBean.getId()));
			}

		} else if (i == R.id.coursepage_call) {
			callClub();

		} else if (i == R.id.coursepage_invite) {
			ShareCoursePop.StartAction(CoursePageActivity.this, mCourseBean,
					shareText.toString());

		} else if (i == R.id.tv_back) {
			finish();

		}
	}

	public void initData() {
		// init cache image options
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_unknown)
				.showImageForEmptyUri(R.drawable.ic_unknown)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// get Bundle
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		mCourseBean = (CourseBean) bundle.getSerializable(COURSEBEAN);
		mIsMyFavoriteCourse = mCourseBean.isFavorite();

		// init mIsMyFavoriteCourse
		// mIsMyFavoriteCourse = CourseFavoriteUtil
		// .isMyFavoriteCourse(mCourseBean);
		// log.e("is my favorite course?" + mIsMyFavoriteCourse);
		// start Task find information
		// log.e("mCourseBean.getSchedleId():" + mCourseBean.getId()
		// + ", mCourseBean.getClubId():" + mCourseBean.getClubId()
		// + ", mCourseBean.getClassId():" + mCourseBean.getClassId());
		ThreadPool.getInstance().execute(
				new ClassShow(new CourseShowProtocolHandler(), mCourseBean));
		ThreadPool.getInstance().execute(
				new FollowsClass(mCourseBean.getClassId(), COUNT, 1,
						new CourseShowProtocolHandler()));
	}

	public void initView() {
		contentLayout = (LinearLayout) findViewById(R.id.layout_concern_list);
		viewPager = (AutoScrollViewPager) findViewById(R.id.vp_coursepage);
		indicator = (CirclePageIndicator) findViewById(R.id.cpi_coursepage);
		mCoachHeadView = (ImageView) findViewById(R.id.iv_coursepage_coach_head);
		mCoachNameView = (TextView) findViewById(R.id.tv_coursepage_coachname);
		mClassNameView = (TextView) findViewById(R.id.tv_title);
		mDescView = (TextView) findViewById(R.id.tv_coursepage_desc);
		mAlarmBtn = (ImageButton) findViewById(R.id.btn_coursepage_setalarm);
		mFavoriteCourseBtn = (Button) findViewById(R.id.btn_coursepage_favorite_course);
		yuyue = (TextView) findViewById(R.id.coursepage_call);
		concernCount = (TextView) findViewById(R.id.tv_follow_people);
		moreConcernView = (ImageView) findViewById(R.id.tv_more_concern_list);

		findViewById(R.id.layout_coach_detail).setOnClickListener(this);
		yuyue.setOnClickListener(this);
		findViewById(R.id.layout_coursepage_more).setOnClickListener(this);
		findViewById(R.id.coursepage_invite).setOnClickListener(this);
		findViewById(R.id.tv_back).setOnClickListener(this);
		mFavoriteCourseBtn.setOnClickListener(this);
		mAlarmBtn.setOnClickListener(this);
		mCoachNameView.setOnClickListener(this);
	}

	public void fillDataFromDB() {
		// set add/remove button
		if (mIsMyFavoriteCourse) {
			mFavoriteCourseBtn
					.setBackgroundResource(R.drawable.btn_coursepage_remove);
			mFavoriteCourseBtn.setText(R.string.favorite_course_remove);
		}

		// get course bean from DB
		// TODO
		/*
		 * if (mIsMyFavoriteCourse) { mCourseBean =
		 * FavoriteUtil.getCourseById(mCourseBean.getId()); } else { mCourseBean
		 * = CourseUtil.getCourseById(mCourseBean.getId()); }
		 */

		// get Coach Bean
		mCoachBean = AccountDetailUtil.getAccountDetailBean(mCourseBean
				.getCoachId());
		// get class bean, according to class id
		mClassBean = ClassUtil.getClassById(mCourseBean.getClassId());
		// set TextView
		mCoachNameView.setText(mCourseBean.getCoachName());
		// set Coach Head
		ImageLoader.getInstance().displayImage(mCoachBean.getProfileImageUrl(),
				mCoachHeadView, options);
		// set Class Name
		mClassNameView.setText(mCourseBean.getClassName().replace("\n", ""));
		if (mClassBean.getDescription() != null) {
			if (mClassBean.getDescription().length() >= 80) {
				mDescView.setText("   "
						+ mClassBean.getDescription().substring(0, 80) + "……");
			} else {
				mDescView.setText(mClassBean.getDescription());
			}
		}

		// 设置分享内容
		shareText

		.append("课程   " + mClassBean.getClassName().replace("\n", "") + "\n")
				.append("时间  " + mCourseBean.getScheduleDate() + " ")
				.append(mCourseBean.getStartTime() + "~")
				.append(mCourseBean.getEndTime() + "\n")
				.append("教练  " + mCoachBean.getName());

		// 设置闹钟提醒时间
		try {
			Date dt = simp.parse(mCourseBean.getScheduleDate() + " "
					+ mCourseBean.getStartTime());
			startTime = dt.getTime();

			// 设置不能预约与打电话的情况
			if (startTime < System.currentTimeMillis()) {
				mAlarmBtn
						.setBackgroundResource(R.drawable.btn_coursepage_clock_focus);
				mIsAlarmed = true;
				yuyue.setBackgroundResource(R.drawable.btn_gray);
			}

			if (mCourseBean.getIsAlerted() == 1) {
				mIsAlarmed = true;
			}

			dt = simp.parse(mCourseBean.getScheduleDate() + " "
					+ mCourseBean.getEndTime());
			endTime = dt.getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// set scroll imgs
		String[] picArr = { "drawable://" + R.drawable.hometab_bg };
		try {
			picArr = mClassBean.getPicUrls().split(",");
			if (!TextUtils.isEmpty(picArr[0])) {
				LogoUtils.saveLogo(picArr[0], MainName.COURSE_JPG);
			}
		} catch (Exception e) {
			log.e("CoursePageActivity:course picUrls is null!");
		}

		// set Adapter
		viewPager.setAdapter(new ImagePagerAdapter(this, picArr));
		viewPager.setInterval(3000);
		viewPager.startAutoScroll();
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% picArr.length);
		// set indicator
		indicator.setViewPager(viewPager);
	}

	public void setAlarm() {
		if (!mIsAlarmed) {
			Utils.setCalendarEvent(CoursePageActivity.this,
					mClassNameView.getText() + "", mDescView.getText() + "",
					mCourseBean.getClassRoomName() + "", startTime, endTime, 0,
					1, 10, 1);
			mAlarmBtn
					.setBackgroundResource(R.drawable.btn_coursepage_clock_focus);
			mCourseBean.setIsAlerted(1);

			if (CourseFavoriteUtil.isExist(mCourseBean.getId())) {
				CourseFavoriteUtil.updateFavorite(mCourseBean);
			}

			if (CourseUtil.isExist(mCourseBean.getId())) {
				CourseUtil.updateCourse(mCourseBean);
			}

			// mIsAlarmed = !mIsAlarmed;
		} else {
			// now we don't offer delete calendar event
			// Utils.deleteCalendarEvent(this, fuck);
			// mAlarmBtn
			// .setBackgroundResource(R.drawable.btn_coursepage_clock_normal);
			// mIsAlarmed = !mIsAlarmed;
		}
	}

	public void callClub() {

		ClubBean club = ClubUtil.getClubById(AccountDetailUtil.getUserClub());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String call = club.getPhone();
		if (!TextUtils.isEmpty(call)) {
			builder.setMessage(call);
			builder.setPositiveButton(
					getResources().getString(R.string.club_call),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + call));
							startActivity(intent);
						}
					});
		} else {
			builder.setMessage(getResources().getString(
					R.string.club_notime_value));
		}

		builder.setNegativeButton(getResources().getString(R.string.cancle),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		if (startTime > System.currentTimeMillis()) {
			builder.show();
		}
	}

	public void share(String content) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);

		shareIntent.setType("image/*");

		shareIntent.putExtra("sms_body", content);

		shareIntent.setType("text/plain");

		shareIntent.putExtra(Intent.EXTRA_TEXT, content);

		startActivity(Intent.createChooser(shareIntent,
				this.getString(R.string.clubinfo_share)));
	}

	/**
	 * 设置共同关注列表
	 * 
	 * @author yupu
	 * @date 2015年3月19日
	 */
	private void setConcern() {
		if (userInfos == null) {
			concernCount.setText(getResources().getString(
					R.string.mutual_concern));
		} else {
			concernCount.setText(getResources().getString(
					R.string.mutual_concern));
			if (userInfos.size() <= 4) {
				moreConcernView.setVisibility(View.GONE);
				setHeadView(userInfos.size());
			} else {
				setHeadView(4);
				moreConcernView.setVisibility(View.VISIBLE);
			}

			moreConcernView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ConcernActivity.StartAction(CoursePageActivity.this,
							mCourseBean.getClassId());
				}
			});
		}
	}

	private void setHeadView(int size) {
		for (int i = 0; i < size; i++) {
			circleImageView = new CircleImageView(this);
			int height = (int) getResources().getDimension(
					R.dimen.search_view_margin_left);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					height, height);
			params.setMargins(20, 0, 0, 0);
			circleImageView.setLayoutParams(params);
			circleImageView.setScaleType(ScaleType.CENTER_CROP);
			ImageLoader.getInstance().displayImage(
					userInfos.get(i).getUser().getProfileImageUrl(),
					circleImageView, options);
			circleImageView.setTag(i);
			circleImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CoachPageActivity.ActionStart(CoursePageActivity.this,
							userInfos.get((int) v.getTag()).getUser()
									.getUserId());
				}
			});
			contentLayout.addView(circleImageView);
		}
	}

	/**
	 * FavoritesProtocolHandler
	 * 
	 * @author zpy zpy@98ki.com
	 * @date 2014-11-12 下午8:14:20
	 * @version: V1.0
	 */
	private class FavoritesProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case com.shape100.gym.Event.ADDORREMOVE:
				// EventBus post
				EventBus.getDefault().post(
						new Event.RefreshScheduleEvent("refresh"));
				mIsMyFavoriteCourse = !mIsMyFavoriteCourse;
				mFavoriteCourseBtn.setVisibility(View.INVISIBLE);
				if (mIsMyFavoriteCourse) {
					mFavoriteCourseBtn.setText(R.string.favorite_course_remove);
					mFavoriteCourseBtn
							.setBackgroundResource(R.drawable.btn_coursepage_remove);
					Toast.makeText(MainApplication.sContext,
							R.string.favorite_course_add, Toast.LENGTH_SHORT)
							.show();
				} else {
					mFavoriteCourseBtn.setText(R.string.favorite_course_add);
					mFavoriteCourseBtn
							.setBackgroundResource(R.drawable.btn_coursepage_add);
					Toast.makeText(MainApplication.sContext,
							R.string.favorite_course_remove, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				if (mIsMyFavoriteCourse) {
					Toast.makeText(MainApplication.sContext, "取消失败！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainApplication.sContext, "关注课程失败！",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case ConstVar.HANDLER_MSG_ERROR:
				Toast.makeText(MainApplication.sContext,
						getResources().getString(R.string.toast_no_net),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	private class CourseShowProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				log.e("courseShow json successed!");
				fillDataFromDB();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				break;
			case com.shape100.gym.Event.CONCERN_LIST:
				userInfos = (ArrayList<UserInfo>) msg.obj;
				setConcern();
				break;
			case com.shape100.gym.Event.CONCERN_LIST_FAILED:
				break;
			}
		}
	}

}
