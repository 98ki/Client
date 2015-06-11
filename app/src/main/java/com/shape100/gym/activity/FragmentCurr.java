package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com._98ki.util.TimeUtils;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.provider.CourseUtil;
import com.shape100.widget.CourseWheel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;
import de.greenrobot.event.model.Event.RefreshScheduleEvent;

public class FragmentCurr extends Fragment implements OnPageChangeListener,
		View.OnClickListener {

	private static final Logger log = Logger.getLogger("FragmentCurr");
	private FragmentPagerAdapter mAdapter;
	private List<MyFragment> mFragments = new ArrayList<MyFragment>();
	private List<ImageView> mDotViews = new ArrayList<ImageView>();
	private List<String> mTabTitles = new ArrayList<String>();
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mIndex = 0; // 标记俱乐部界面0 ，我的界面1
	private int officialBug = 0;
	private ViewPager mViewPage;
	private TextView mTabTitleView;
	private CourseWheel courseWheel;
	private ImageView mFilterView;
	private ImageView mDateFilterView;

	private boolean isHide;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.d("onCreateView");
		return inflater.inflate(R.layout.fragment_curr, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.d("onCreate");
		// EventBus register
		EventBus.getDefault().register(this);
		log.e("Event Bus register!");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// EventBus unregister
		EventBus.getDefault().unregister(this);
		log.e("Event Bus unregister!");
		log.d("onDestroy");
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onActivityCreated");
		}

		View v = getView();
		mViewPage = (ViewPager) v.findViewById(R.id.curr_viewpager);

		mFilterView = (ImageView) v.findViewById(R.id.currtab_filter);

		mTabTitleView = (TextView) v.findViewById(R.id.currtab_title);
		ImageView mCurrtabDot1 = (ImageView) v.findViewById(R.id.currtab_dot1);
		ImageView mCurrtabDot2 = (ImageView) v.findViewById(R.id.currtab_dot2);

		CurrTabSelf currTabSelf = new CurrTabSelf();
		CurrTabClub currTabClub = new CurrTabClub();

		mFragments.add(currTabClub);
		mFragments.add(currTabSelf);

		mAdapter = new CurrFragmentPagerAdapter(getChildFragmentManager());
		mViewPage.setAdapter(mAdapter);
		mViewPage.setOnPageChangeListener(this);
		mViewPage.setCurrentItem(0);
		mDateFilterView = (ImageView) v.findViewById(R.id.currtab_date);

		mDotViews.add(mCurrtabDot1);
		mDotViews.add(mCurrtabDot2);

		mFilterView.setOnClickListener(this);
		mDateFilterView.setOnClickListener(this);

		//
		mTabTitles.add(getString(R.string.currtab_club));
		mTabTitles.add(getString(R.string.currtab_self));

		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 
	 * @author yupu
	 * @date 2015年5月1日
	 */
	public void hideFilter() {
		mFilterView.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @author yupu
	 * @date 2015年5月1日
	 */
	public void showFilter() {
		mFilterView.setVisibility(View.VISIBLE);
	}

	public void setHide(boolean ishide) {
		isHide = ishide;
	}

	/**
	 * EventBus -> event from clubInfoActivity.btnJoin.click
	 * 
	 * @throw
	 * @return void
	 */
	public void onEventMainThread(RefreshScheduleEvent event) {
		log.e("Event Bus get String:" + event.getString());
		if (event != null) {
			mFragments.get(0).refresh();
			mFragments.get(1).refresh();
			if (event.getString().equals("exitClub")) {
				hideFilter();
			}

			if (event.getString().equals("joinClub")) {
				if (mIndex == 1) {
					hideFilter();
				} else {
					showOrHide(); // 查询数据库判断
					if (isHide) {
						hideFilter();
					} else {
						showFilter();
					}
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int index) {
		mIndex = index;
		if (index == 0) {

			if (isHide) {
				hideFilter();
			} else {
				showFilter();
			}

			mDotViews.get(0).setBackgroundResource(R.drawable.dot_black);
			mDotViews.get(1).setBackgroundResource(R.drawable.dot_white);
		} else {
			hideFilter();

			mDotViews.get(1).setBackgroundResource(R.drawable.dot_black);
			mDotViews.get(0).setBackgroundResource(R.drawable.dot_white);
		}
		// set title
		String title = mTabTitles.get(index);
		mTabTitleView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.currtab_filter:
			changeCourse();
			break;
		case R.id.currtab_date:
			changeDate();
			break;
		}
	}

	private void changeDate() {
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(this.getActivity(),
				mDateSetListener, mYear, mMonth, mDay);
		officialBug = 0;
		dialog.show();

	}

	private void changeCourse() {
		courseWheel = new CourseWheel(getActivity());
		courseWheel.showAtLocation(
				getActivity().findViewById(R.id.fragment_curr), Gravity.BOTTOM,
				0, 0);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			if (officialBug == 0) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateView();
				officialBug++;
			}
		}
	};

	private void updateView() {
		//
		Calendar cd = Calendar.getInstance();
		cd.set(mYear, mMonth, mDay);
		String[] startEndDate = TimeUtils.getWeekDate(cd);

		if (mIndex == 1) {
			CurrTabSelf frag1 = (CurrTabSelf) mFragments.get(1);
			frag1.updateHeader(mYear, mMonth, mDay);
			log.e("Bus bus bus , my hero bus, ");
			EventBus.getDefault().post(
					new Event.FavoriteFilterEvent("date", "", startEndDate));
		} else if (mIndex == 0) {
			CurrTabClub frag2 = (CurrTabClub) mFragments.get(0);
			frag2.updateHeader(mYear, mMonth, mDay);
			EventBus.getDefault().post(
					new Event.CourseFilterEvent("date", "", startEndDate));
		}
	}

	private class CurrFragmentPagerAdapter extends FragmentPagerAdapter {

		public CurrFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return (Fragment) mFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

	};

	@Override
	public void onStart() {
		log.d("onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		if (Logger.DBG) {
			log.d("onResume");
		}
		super.onResume();
	}

	private void showOrHide() {
		if (CourseUtil.getCourseFilterDataModel().size() == 0) {
			isHide = true;
		} else {
			isHide = false;
		}
		log.e("---------------showOrHide--" + isHide);
	}

	@Override
	public void onPause() {
		log.d("onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		log.d("onStop");
		super.onStop();
	}

	public interface MyFragment {
		public void refresh();
	}

}
