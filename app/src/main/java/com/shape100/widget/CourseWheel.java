/**    
 * file name：CourseWheel.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-16 
 * @version:    
 * Copyright zpy@98ki.com Corporation 2014         
 *    
 */
package com.shape100.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.provider.CourseUtil;
import com.shape100.gym.provider.DBConst.CourseColumns;
import com.shape100.widget.wheel.data.ArrayWheelAdapter;
import com.shape100.widget.wheel.data.OnWheelChangedListener;
import com.shape100.widget.wheel.data.WheelView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;
import de.greenrobot.event.model.Event.CourseFilterEvent;

/**
 * 
 * project：shape100 class：CourseWheel desc： author：zpy zpy@98ki.com create
 * date：2014-11-16 上午10:46:02 modify author: zpy update date：2014-11-16
 * 上午10:46:02 update remark：
 * 
 * @version
 * 
 */
public class CourseWheel extends PopupWindow implements OnClickListener,
		OnWheelChangedListener {
	/* UI */
	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit, btn_cancel_filter;
	private WheelView mClassRoomNameView, mClassNameView, mCoachNameView;

	/* Data */
	private static final Logger log = Logger.getLogger("CourseWheel");
	/**
	 * key - classRoom values - classNames
	 */
	// private Map<String, ArrayList<String>> mClassNameDatasMap = new
	// HashMap<String, ArrayList<String>>();
	/**
	 * key - className values - coachNames
	 */
	// private Map<String, ArrayList<String>> mCoachNameDatasMap = new
	// HashMap<String, ArrayList<String>>();
	/**
	 * All ClassRoomName
	 */
	private ArrayList<String> mClassRoomNameDatas = new ArrayList<String>();
	/**
	 * The Current classroom name
	 */
	private String mCurrentClassRoomName;
	/**
	 * The Current class name
	 */
	private String mCurrentClassName;
	/**
	 * The Current coach name
	 */
	private String mCurrentCoachName = "";

	/**
	 * 所有的课程名字
	 */
	private ArrayList<String> mClassNameDatas = new ArrayList<String>();
	/**
	 * 所有的教练名字
	 */
	private ArrayList<String> mCoachNameDatas = new ArrayList<String>();

	public CourseWheel(Activity context) {
		super(context);
		mContext = context;

		initView();
		// get filter model list
		List<CourseBean> list = CourseUtil.getCourseFilterDataModel();
		// list to mClassNameDatasMap and mCoachNameDatasMap
		// courseListToMap(list);
		initData(list);
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mContext,
				mClassRoomNameDatas.toArray(new String[mClassRoomNameDatas
						.size()]));
		adapter.setTextSize(20);
		mClassRoomNameView.setViewAdapter(adapter);

		mClassRoomNameView.setVisibleItems(5);
		mClassNameView.setVisibleItems(5);
		mCoachNameView.setVisibleItems(5);
		updateClassNameView();
		updateCoachNameView();

	}

	public void initView() {
		// create
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_course, null);
		viewfipper = new ViewFlipper(mContext);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		mClassRoomNameView = (WheelView) mMenuView
				.findViewById(R.id.wheel_classroom);
		mClassNameView = (WheelView) mMenuView.findViewById(R.id.wheel_course);
		mCoachNameView = (WheelView) mMenuView.findViewById(R.id.wheel_coach);
		mClassRoomNameView.addChangingListener(this);
		mClassNameView.addChangingListener(this);
		mCoachNameView.addChangingListener(this);
		btn_submit = (Button) mMenuView.findViewById(R.id.popup_course_submit);
		btn_cancel_filter = (Button) mMenuView
				.findViewById(R.id.popup_course_cancel_filter);
		btn_submit.setOnClickListener(this);
		btn_cancel_filter.setOnClickListener(this);

		// show
		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xfffffff);// 0x00000000
		this.setBackgroundDrawable(dw);
		this.update();
	}

	/**
	 * 填充各个list数据
	 * 
	 * @author yupu
	 * @date 2015年1月7日
	 */
	private void initData(List<CourseBean> list) {
		mClassNameDatas.add("全部课程");
		mCoachNameDatas.add("全部教练");
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		Set<String> set3 = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			set1.add(list.get(i).getClassRoomName());
			set2.add(list.get(i).getClassName());
			set3.add(list.get(i).getCoachName());
		}

		quChong(set1, mClassRoomNameDatas);
		quChong(set2, mClassNameDatas);
		quChong(set3, mCoachNameDatas);
	}

	/**
	 * 去除每个list 的重复值
	 * 
	 * @author yupu
	 * @date 2015年1月7日
	 */
	private void quChong(Set<String> set, List<String> list) {
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			list.add(iterator.next().toString());
		}
	}

	/**
	 * 
	 * list to mClassNameDatasMap and mCoachNameDatasMap
	 * 
	 * @param list
	 */
	/*
	 * public void courseListToMap(List<CourseBean> list) { // put filter model
	 * to mClassNameDatasMap and mCoachNameDatasMap for (int i = 0; i <
	 * list.size(); i++) { CourseBean bean = list.get(i); if
	 * (mClassNameDatasMap.containsKey(bean.getClassRoomName())) {
	 * ArrayList<String> classNameList =
	 * mClassNameDatasMap.get(bean.getClassRoomName());
	 * classNameList.add(bean.getClassName()); } else {
	 * mClassRoomNameDatas.add(bean.getClassRoomName());
	 * 
	 * ArrayList<String> classNameList = new ArrayList<String>();
	 * classNameList.add(bean.getClassName());
	 * mClassNameDatasMap.put(bean.getClassRoomName(), classNameList); }
	 * 
	 * if (mCoachNameDatasMap.containsKey(bean.getClassName())) {
	 * ArrayList<String> coachNameList =
	 * mCoachNameDatasMap.get(bean.getClassName());
	 * coachNameList.add(bean.getCoachName()); } else { ArrayList<String>
	 * coachNameList = new ArrayList<String>();
	 * coachNameList.add(bean.getCoachName());
	 * mCoachNameDatasMap.put(bean.getClassName(), coachNameList); } } //
	 * classRoom - class - coach， this third map will make // mClassNameDatasMap
	 * values confilt(same classroom/class differnet // coach) // so we should
	 * unique mClassNameDatasMap.value for (String key :
	 * mClassNameDatasMap.keySet()) { ArrayList<String> classNameList =
	 * mClassNameDatasMap.get(key); HashSet h = new HashSet(classNameList);
	 * classNameList.clear(); classNameList.addAll(h); } // Log for (String key
	 * : mClassNameDatasMap.keySet()) { ArrayList<String> classNameList =
	 * mClassNameDatasMap.get(key); // for(int i=0;i<classNameList.size();i++){
	 * // log.d("(mClassNameDatasMap) -> "+ key+":"+classNameList.get(i)); // }
	 * } for (String key : mCoachNameDatasMap.keySet()) { ArrayList<String>
	 * coachNameList = mCoachNameDatasMap.get(key); // for(int
	 * i=0;i<coachNameList.size();i++){ // log.d("(mCoachNameDatasMap) -> "+
	 * key+":"+coachNameList.get(i)); // } } }
	 */
	/**
	 * according to current class name update class name view
	 */
	private void updateCoachNameView() {
		/*
		 * int cCurrent = mClassNameView.getCurrentItem(); if (cCurrent != 0) {
		 * mCurrentClassName = mClassNameDatasMap.get(mCurrentClassRoomName)
		 * .get(cCurrent); mCurrentCoachName =
		 * mCoachNameDatasMap.get(mCurrentClassName) .get(0);
		 * 
		 * ArrayList<String> coachNameList = mCoachNameDatasMap
		 * .get(mCurrentClassName);
		 * 
		 * if (coachNameList == null) { coachNameList = new ArrayList<String>();
		 * }
		 */

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mContext, mCoachNameDatas.toArray(new String[mCoachNameDatas
						.size()]));
		adapter.setTextSize(20);
		mCoachNameView.setViewAdapter(adapter);
		mCoachNameView.setCurrentItem(0);
		// }
	}

	/**
	 * according to current classroom name update class name view
	 */
	private void updateClassNameView() {
		// int rCurrent = mClassRoomNameView.getCurrentItem();
		/*
		 * try { mCurrentClassRoomName = mClassRoomNameDatas.get(rCurrent); }
		 * catch (IndexOutOfBoundsException e) {
		 * 
		 * } ArrayList<String> classNameList = mClassNameDatasMap
		 * .get(mCurrentClassRoomName); if (classNameList == null) {
		 * classNameList = new ArrayList<String>(); }
		 */
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mContext, mClassNameDatas.toArray(new String[mClassNameDatas
						.size()]));
		adapter.setTextSize(20);
		mClassNameView.setViewAdapter(adapter);
		mClassNameView.setCurrentItem(0);

		// updateCoachNameView();
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}

	/**
	 * change event
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		/*
		 * if (wheel == mClassRoomNameView) { updateClassNameView(); } else if
		 * (wheel == mClassNameView) { updateCoachNameView(); } else if (wheel
		 * == mCoachNameView) {
		 * log.e(mCoachNameDatasMap.get(mCurrentClassName).size() + "");
		 * mCurrentCoachName = mCoachNameDatasMap.get(mCurrentClassName).get(
		 * newValue); }
		 */
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_course_submit:
			// post EventBus to FragmentCurr, refresh club and my schedule.
			mCurrentClassRoomName = mClassRoomNameDatas.get(mClassRoomNameView
					.getCurrentItem());
			mCurrentClassName = mClassNameDatas.get(mClassNameView
					.getCurrentItem());
			mCurrentCoachName = mCoachNameDatas.get(mCoachNameView
					.getCurrentItem());
			String filter1 = null;
			if (mClassNameView.getCurrentItem() != 0
					&& mCoachNameView.getCurrentItem() != 0) {
				filter1 = " " + CourseColumns.CLASSROOM_NAME + " = '"
						+ mCurrentClassRoomName + "' and "
						+ CourseColumns.CLASS_NAME + " = '" + mCurrentClassName
						+ "' and " + CourseColumns.COACH_NAME + " = '"
						+ mCurrentCoachName + "'";
			} else if (mClassNameView.getCurrentItem() == 0
					&& mCoachNameView.getCurrentItem() == 0) {
				filter1 = " " + CourseColumns.CLASSROOM_NAME + " = '"
						+ mCurrentClassRoomName + "'";
			} else if (mClassNameView.getCurrentItem() == 0) {
				filter1 = " " + CourseColumns.CLASSROOM_NAME + " = '"
						+ mCurrentClassRoomName + "' and "
						+ CourseColumns.COACH_NAME + " = '" + mCurrentCoachName
						+ "'";
			} else {
				filter1 = " " + CourseColumns.CLASSROOM_NAME + " = '"
						+ mCurrentClassRoomName + "' and "
						+ CourseColumns.CLASS_NAME + " = '" + mCurrentClassName
						+ "'";
			}
			log.e("Event Bus Start!");
			CourseFilterEvent ev=new Event.CourseFilterEvent("keyword", filter1);
			ev.setmClassName(mCurrentClassRoomName);
			EventBus.getDefault().post(ev);
			this.dismiss();
			break;
		case R.id.popup_course_cancel_filter:
			this.dismiss();
			break;

		}

	}

}
