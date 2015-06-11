package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.Utils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.UpdateBean;
import com.shape100.gym.protocol.CheckUpdate;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.AccountDetailUtil;
import com.umeng.fb.FeedbackAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event.ChangeFragmentEvent;

/**
 * 主界面activity
 * 
 * @author yupu
 * @date 2015年3月26日
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private long curTime;
	private static final Logger log = Logger.getLogger("ShakeActivity");
	private static final String INDEX = "page";
	private static final int TAB_COUNT = 4;

	private static final int INDEX_TAB_PANEL = 0;
	private static final int INDEX_TAB_BUTTON = 1;
	private static final int INDEX_TAB_TEXT = 2;
	private static final int INDEX_TAB_BG_NORMAL = 3;
	private static final int INDEX_TAB_BG_FOCUS = 4;

	private final int sTabRes[][] = {
			{ R.id.tab_home, R.id.tab_button_home, R.id.tab_text_home,
					R.drawable.ic_main_home_normal,
					R.drawable.ic_main_home_focus },
			{ R.id.tab_club, R.id.tab_button_club, R.id.tab_text_club,
					R.drawable.ic_main_club_normal,
					R.drawable.ic_main_club_focus },
			{ R.id.tab_curr, R.id.tab_button_curr, R.id.tab_text_curr,
					R.drawable.ic_main_curr_normal,
					R.drawable.ic_main_curr_focus },
			{ R.id.tab_owner, R.id.tab_button_owner, R.id.tab_text_owner,
					R.drawable.ic_main_owner_normal,
					R.drawable.ic_main_owner_focus } };

	private FragmentManager mFM;
	private List<TabBean> mTabBeans = new ArrayList<TabBean>(TAB_COUNT);
	private ImageView first_bg;

	private class TabBean {
		public Fragment mFragment;
		public View mTabPanel;
		public ImageView mTabButton;
		public TextView mTabTextView;
		public String mTag;
	}

	public static void ActionStart(Activity activity, int index) {
		Intent intent = new Intent(activity, MainActivity.class);
		intent.putExtra(INDEX, index);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MainApplication.activityActivity.push(MainActivity.this);
		new FeedbackAgent(this).sync();
		AppConfig.getInstance().setMobile(Utils.getinfo());// 设置手机号码
		setContentView(R.layout.activity_main);
		first_bg = (ImageView) findViewById(R.id.first_bg);
		first_bg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				first_bg.setVisibility(View.INVISIBLE);
				AppConfig.getInstance().setFirstClub(1);
				return true;
			}
		});
		// String te =
		// getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
		// String te2 = Environment.getExternalStorageDirectory() +
		// File.separator
		// + Environment.DIRECTORY_DCIM;

		// checkupdate

		ThreadPool.getInstance().execute(
				new CheckUpdate(new EventProtocolHandler()));
		// EventBus register
		EventBus.getDefault().register(this);
		initTabBeans();
		setTabPanelListener();

		mFM = getSupportFragmentManager();
		selectTab(getIntent().getIntExtra(INDEX, 0));

		if (Logger.DBG) {
			log.e("onCreate");
		}
	}

	private void initTabBeans() {
		for (int i = 0; i < TAB_COUNT; i++) {
			TabBean bean = new TabBean();
			bean.mFragment = null;
			bean.mTabPanel = findViewById(sTabRes[i][INDEX_TAB_PANEL]);
			bean.mTabButton = (ImageView) findViewById(sTabRes[i][INDEX_TAB_BUTTON]);
			bean.mTabTextView = (TextView) findViewById(sTabRes[i][INDEX_TAB_TEXT]);
			mTabBeans.add(bean);
		}
	}

	private void setTabPanelListener() {
		for (int i = 0; i < TAB_COUNT; i++) {
			TabBean bean = mTabBeans.get(i);
			bean.mTabPanel.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_home:
			selectTab(0);
			break;
		case R.id.tab_club:
			selectTab(1);
			break;
		case R.id.tab_curr:
			if (AppConfig.getInstance().isFirstClub() == 0) {
				first_bg.setVisibility(View.VISIBLE);
			}
			selectTab(2);
			break;
		case R.id.tab_owner:
			selectTab(3);
			break;
		}
	}

	private void selectTab(int index) {
		if (index == 3 && AppConfig.getInstance().getUserId() == 0) {
			LoginActivity.ActionStart(MainActivity.this);
			finish();
		} else {
			switchFragment(index);
			switchTabBar(index);
		}
	}

	private void switchFragment(int index) {
		FragmentTransaction ft = mFM.beginTransaction();

		// Hide all fragments
		for (int i = 0; i < TAB_COUNT; i++) {
			TabBean bean = mTabBeans.get(i);
			if (bean.mFragment != null) {
				ft.hide(bean.mFragment);
			}
		}
		// Show selected flagment
		if (index < 0 || index >= TAB_COUNT) {
			return;
		}
		TabBean bean = mTabBeans.get(index);
		if (bean.mFragment == null) {
			bean.mFragment = createTabFragment(index);
			ft.add(R.id.main_content, bean.mFragment, index + "");
		} else {
			ft.show(bean.mFragment);
		}
		ft.commit();
	}

	private Fragment createTabFragment(int index) {
		Fragment fragment = null;
		switch (index) {
		case 0:
			fragment = new FragmentHome();
			break;
		case 1:
			if (AccountDetailUtil.getUserClub() != 0) {
				fragment = new ClubInfoFragment();
			} else {
				fragment = new FragmentClub();
			}
			break;
		case 2:
			fragment = new FragmentCurr();
			break;
		case 3:
			fragment = new FragmentOwner();
			break;
		}
		return fragment;
	}

	private void switchTabBar(int index) {
		if (index < 0 || index >= TAB_COUNT) {
			return;
		}

		int normalColor = getResources().getColor(
				R.color.maintab_textcolor_normal);
		int focusColor = getResources().getColor(
				R.color.maintab_textcolor_focus);

		for (int i = 0; i < TAB_COUNT; i++) {
			TabBean bean = mTabBeans.get(i);
			bean.mTabButton
					.setBackgroundResource(sTabRes[i][INDEX_TAB_BG_NORMAL]);
			bean.mTabTextView.setTextColor(normalColor);
		}

		TabBean bean = mTabBeans.get(index);
		bean.mTabButton
				.setBackgroundResource(sTabRes[index][INDEX_TAB_BG_FOCUS]);
		bean.mTabTextView.setTextColor(focusColor);
	}

	@Override
	protected void onStop() {
		if (Logger.DBG) {
			log.e("onStop");
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// EventBus unregister
		EventBus.getDefault().unregister(this);
		if (Logger.DBG) {
			log.e("onDestroy");
		}
		super.onDestroy();
	}

	/**
	 * 
	 * onEventMainThread
	 * 
	 * @param ChangeFragmentEvent
	 *            change fragment
	 */
	public void onEventMainThread(ChangeFragmentEvent event) {
		log.e("[Event Bus] get String:" + event.getFragmentName());
		if (event != null) {
			// change ClubInfoFragment.
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragment = null;
			if (event.getFragmentName().equals("ClubInfoFragment")) {
				fragment = new ClubInfoFragment();
			} else if (event.getFragmentName().equals("FragmentClub")) {
				fragment = new FragmentClub();
			}
			ft.remove(fm.findFragmentByTag("1"));
			ft.add(R.id.main_content, fragment, "1");
			mTabBeans.get(1).mFragment = fragment;
			ft.commitAllowingStateLoss();
		}
	}

	@Override
	protected void onPause() {
		if (Logger.DBG) {
			log.e("onPause");
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (Logger.DBG) {
			log.e("onResume");
		}
		super.onResume();
	}

	@Override
	protected void onResumeFragments() {
		if (Logger.DBG) {
			log.e("onResumeFragments");
		}

		super.onResumeFragments();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (Logger.DBG) {
			log.e("onSaveInstanceState");
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		if (Logger.DBG) {
			log.e("onStart");
		}
		super.onStart();
	}

	@Override
	protected void onRestart() {
		if (Logger.DBG) {
			log.e("onRestart");
		}
		super.onRestart();
	}

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - curTime > 2000) {
			Toast.makeText(MainActivity.this, "再按一次返回键将退出应用", Toast.LENGTH_LONG)
					.show();
			curTime = System.currentTimeMillis();
		} else {
			if (MainApplication.activityActivity.size() != 0) {
				MainApplication.activityActivity.pop().finish();
			}
			super.onBackPressed();
		}
	}

	private Dialog buildDialog(final UpdateBean bean) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		// builder.setTitle("button02");
		builder.setMessage(bean.getUpdateDescription());
		builder.setPositiveButton("现在更新",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Uri uri = Uri.parse(bean.getDownloadUrl());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					}
				});
		builder.setNegativeButton("以后再说", null);
		return builder.create();

	}

	private class EventProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				UpdateBean bean = (UpdateBean) msg.obj;
				if (bean.getHasNewVersion() == 1) {
					buildDialog(bean).show();
				}
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				break;

			}
		}
	}

}
