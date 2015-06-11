package com.shape100.gym.activity;

import java.net.URISyntaxException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com._98ki.util.LogoUtils;
import com._98ki.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.shape100.gym.adapter.ImagePagerAdapter;
import com.shape100.gym.adapter.TimeAdapter;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.protocol.ClubJoin;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.widget.ListviewForScrollview;
import com.viewpagerindicator.CirclePageIndicator;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;

public class ClubInfoActivity extends SlideActivity implements OnClickListener {
	/* Data */
	private static final Logger log = Logger.getLogger("clubInfoActivity");
	private String[] picArr = { "drawable://" + R.drawable.hometab_bg };
	private ClubBean clubBean;

	/* View */
	private AutoScrollViewPager viewPager;
	private CirclePageIndicator indicator;
	private TextView tvClubName;
	private TextView tvClubAddress;
	private TextView mClubPhoneView;
	private TextView mClubHomePageUrlView;
	private TextView tvClubBusinessHours;
	private TextView tvClubDescription;
	private ImageView mClubShareView;
	private Button mJoinBtn;

	private ListviewForScrollview listview;
	private TimeAdapter adapter;
	private RotateAnimation rightAnimation;
	private ImageView rightView;
	private ImageView club_logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_clubinfo);
		initData();
		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 切换系统已安装地图导航，仅支持百度地图，高德地图
	 * 
	 * @author yupu
	 * @date 2015年1月27日
	 */
	private void switchMap() {
		String curr_lat = AppConfig.getInstance().getCurrLat();
		String curr_lon = AppConfig.getInstance().getCurrLon();
		String club_lat = clubBean.getLat();
		String club_lon = clubBean.getLon();

		if (Utils.isInstallByread(Utils.BDpackage)) {
			// ----39.905787,116.558903
			// 116.566022,39.911183

			StringBuilder sb = new StringBuilder();
			sb.append("intent://map/direction?origin=latlng:")
					.append(curr_lat + ",")
					.append(curr_lon + "|name:当前位置")
					.append("&destination=" + club_lat + "," + club_lon)
					.append("&mode=driving")
					.append("&src=shape100|shape100")
					.append("&referer=Autohome|GasStation")
					.append("#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

			try {
				Intent intent = Intent.getIntent(sb.toString());
				startActivity(intent); // 启动调用
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else if (Utils.isInstallByread(Utils.GDpackage)) {
			// androidamap://route?sourceApplication=softname&slat=36.2&slon=116.1&sname=abc
			// &dlat=36.3&dlon=116.2&dname=def&dev=0&m=0&t=1&showType=1
			// pkg=com.autonavi.minimap
			// androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=36.547901&lon=104.258354&dev=1&style=2
			StringBuilder sb = new StringBuilder();
			sb.append("androidamap://navi?sourceApplication=shape100")
					.append("&lat=" + club_lat + "&lon=" + club_lon)
					.append("&dev=0") // 起终点是否偏移(0:lat
										// 和
										// lon
										// 是已经加密后的,不需要国测加密;
										// 1:需要国测加密)
					.append("&style=2"); // 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4
											// 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7
											// 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
			try {
				Intent intent = Intent.getIntent(sb.toString());
				startActivity(intent); // 启动调用
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Uri uri;
		Intent intent;
		switch (v.getId()) {
		case R.id.layout_address:
			switchMap();
			break;
		case R.id.btn_clubinfo_join_exit:
			joinClub();
			break;
		case R.id.iv_clubinfo_share:
			intent = new Intent(ClubInfoActivity.this, SharePopupWindow.class);
			intent.putExtra("clubbean", clubBean);
			startActivity(intent);
			break;
		case R.id.tv_clubinfo_detail:
			intent = new Intent(this, ClubInfoDetailActivity.class);
			intent.putExtra("desc", clubBean.getDescription());
			if (picArr != null && picArr.length != 0) {
				intent.putExtra("url", picArr[0]);
			}
			startActivity(intent);
			break;
		case R.id.tv_clubinfo_phone:
			callClub();
			break;
		case R.id.tv_clubinfo_homepageurl:
			String homePageUrl = mClubHomePageUrlView.getText() + "";
			uri = Uri.parse(homePageUrl);
			intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case R.id.layout_time:
			showList();
			break;
		}

	}

	/**
	 * 打电话
	 * 
	 * @author yupu
	 * @date 2015年3月27日
	 */
	public void callClub() {
		final String number = mClubPhoneView.getText().toString();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ClubInfoActivity.this);
		if (!TextUtils.isEmpty(number)) {
			builder.setMessage(number);
			builder.setPositiveButton(
					getResources().getString(R.string.club_call),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + number));
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
		builder.show();
	}

	private void setList(String[] content) {
		if (content != null) {
			tvClubBusinessHours.setText(content[0]);
			adapter = new TimeAdapter(ClubInfoActivity.this, content, false);
			listview.setAdapter(adapter);
		} else {
			tvClubBusinessHours.setText(getResources().getString(
					R.string.club_notime_value));
		}
	}

	/**
	 * 切换营业时间动画
	 * 
	 * @author yupu
	 * @date 2015年1月13日
	 */
	private void showList() {
		rightView.clearAnimation();
		if (listview.getVisibility() == View.VISIBLE) {
			listview.setVisibility(View.GONE);
			rightAnimation = new RotateAnimation(90f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rightAnimation.setFillAfter(true);
			rightAnimation.setDuration(500);
			rightView.startAnimation(rightAnimation);
		} else {
			adapter.notifyDataSetChanged();
			listview.setVisibility(View.VISIBLE);
			rightAnimation = new RotateAnimation(0f, 90f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rightAnimation.setFillAfter(true);
			rightAnimation.setDuration(500);
			rightView.startAnimation(rightAnimation);
		}
	}

	public void joinClub() {
		if (AppConfig.getInstance().getUserId() != 0) {
			log.e("clubId:" + clubBean.getId());
			new ClubJoin(new EventProtocolHandler(), clubBean).start();
		} else {
			Toast.makeText(this,
					getResources().getString(R.string.toast_no_lookcourse),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		clubBean = (ClubBean) bundle.getSerializable("club");

		// set scroll imgs
		try {
			log.e("clubPic:" + clubBean.getPicUrls());
			picArr = clubBean.getPicUrls().split(",");
		} catch (Exception e) {
			log.e("CoursePageActivity:course picUrls is null!");
		}
	}

	public void initView() {
		club_logo = (ImageView) findViewById(R.id.iv_club_logo);
		listview = (ListviewForScrollview) findViewById(R.id.list_expand);
		rightView = (ImageView) findViewById(R.id.iv_right);

		viewPager = (AutoScrollViewPager) findViewById(R.id.vp_clubinfo);
		indicator = (CirclePageIndicator) findViewById(R.id.cpi_clubinfo);

		tvClubName = (TextView) findViewById(R.id.tv_clubinfo_name);
		tvClubAddress = (TextView) findViewById(R.id.tv_clubinfo_address);
		mClubPhoneView = (TextView) findViewById(R.id.tv_clubinfo_phone);
		mClubHomePageUrlView = (TextView) findViewById(R.id.tv_clubinfo_homepageurl);
		tvClubBusinessHours = (TextView) findViewById(R.id.tv_clubinfo_businesshours);
		tvClubDescription = (TextView) findViewById(R.id.tv_clubinfo_desc);
		findViewById(R.id.tv_clubinfo_detail).setOnClickListener(this);
		findViewById(R.id.layout_time).setOnClickListener(this);
		findViewById(R.id.layout_address).setOnClickListener(this);

		mClubShareView = (ImageView) findViewById(R.id.iv_clubinfo_share);
		mJoinBtn = (Button) findViewById(R.id.btn_clubinfo_join_exit);
		mJoinBtn.setText(R.string.join_club);
		mJoinBtn.setOnClickListener(this);
		mClubShareView.setOnClickListener(this);
		mClubHomePageUrlView.setOnClickListener(this);
		tvClubName.setText(clubBean.getName());
		tvClubAddress.setText(clubBean.getAddress());
		mClubPhoneView.setText(clubBean.getPhone());
		if (!TextUtils.isEmpty(clubBean.getHomepageUrl())) {
			mClubHomePageUrlView.setText(clubBean.getHomepageUrl());
		} else {
			findViewById(R.id.web_clubInfo).setVisibility(View.GONE);
		}

		if (clubBean.getBusinessHours() != null) {
			setList(clubBean.getBusinessHours().split("\\|"));
		}

		if (!TextUtils.isEmpty(clubBean.getLogoUrl())) {
			ImageLoader.getInstance().displayImage(clubBean.getLogoUrl(),
					club_logo);
		}

		/*
		 * String businessHours = clubBean.getBusinessHours().replaceAll("\\|",
		 * "\n");
		 */
		// tvClubBusinessHours.setText(businessHours);
		String description = clubBean.getDescription();
		if (description.length() > 100) {
			description = description.substring(0, 100) + "……";
		}
		tvClubDescription.setText(description);

		// set Adapter
		log.d("__PicArr.length:" + picArr.length);
		viewPager.setAdapter(new ImagePagerAdapter(this, picArr));
		viewPager.setInterval(3000);
		viewPager.startAutoScroll();
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% picArr.length);
		indicator.setViewPager(viewPager);

	}

	// 废弃
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

				// 保存logo图像
				LogoUtils.saveLogo(clubBean.getLogoUrl(), MainName.LOGO_JPG);

				// post EventBus to FragmentCurr, refresh club and my schedule.
				EventBus.getDefault().post(
						new Event.RefreshScheduleEvent("joinClub"));
				// post EventBus to ShakeActivity, change frag
				EventBus.getDefault().post(
						new Event.ChangeFragmentEvent("ClubInfoFragment"));
				finish();
				// hint
				Toast.makeText(MainApplication.sContext,
						"成功加入" + clubBean.getName() + "!", Toast.LENGTH_SHORT)
						.show();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				Toast.makeText(MainApplication.sContext,
						"加入" + clubBean.getName() + "失败!", Toast.LENGTH_SHORT)
						.show();
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				Toast.makeText(MainApplication.sContext,
						getResources().getString(R.string.toast_no_net),
						Toast.LENGTH_SHORT).show();
				break;

			}
		}
	}

}
