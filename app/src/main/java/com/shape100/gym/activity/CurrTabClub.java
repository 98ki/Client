package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.FormatUtils;
import com._98ki.util.TimeUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.activity.FragmentCurr.MyFragment;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ScheduleClub;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.CourseUtil;
import com.shape100.gym.provider.DBConst.CourseColumns;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event.CourseFilterEvent;

public class CurrTabClub extends BaseFragment implements MyFragment,
		OnItemClickListener {
	// Data
	private static final Logger log = Logger.getLogger("CurrTabClub");

	private static int COLUMN_HIEGHT = 0;
	private final int WEEK_COUNT = 7;
	private static int[] DATE_IDS = { R.id.curr_date01, R.id.curr_date02,
			R.id.curr_date03, R.id.curr_date04, R.id.curr_date05,
			R.id.curr_date06, R.id.curr_date07 };
	private static int[] WEEK_IDS = { R.id.curr_week1, R.id.curr_week2,
			R.id.curr_week3, R.id.curr_week4, R.id.curr_week5, R.id.curr_week6,
			R.id.curr_week7 };
	private static int[] LAYOUT_IDS = { R.id.bg_week1, R.id.bg_week2,
			R.id.bg_week3, R.id.bg_week4, R.id.bg_week5, R.id.bg_week6,
			R.id.bg_week7 };

	private ArrayList<CourseBean> mCourseList;
	List<ArrayList<CourseBean>> mScheduleList;

	private String mSql = "";
	private String mKeywords = "";
	private int mWeekOfYear = TimeUtils.getWeekOfYear();
	private int mYear = TimeUtils.getYear();
	private String[] mStartEndDate;

	private GridView mGridView;
	/**
	 * time column , first column of schedule. *
	 */
	private LinearLayout mTimeColumnLayout;
	private TextView[] mDateView;
	private View mView;
	private ProgressBar mProgressBar;
	private TextView leftRoom;
	private String className;

	private ArrayList<Integer> hourStart; // 课程表行数的数组

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			afterData();
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onCreateView");
		}
		return inflater.inflate(R.layout.currtab_club, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.d("onCreate");
		super.onCreate(savedInstanceState);
		// EventBus register
		EventBus.getDefault().register(this);
		log.e("Event Bus register");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onActivityCreated");
		}

		hourStart = new ArrayList<Integer>();
		COLUMN_HIEGHT = FormatUtils.dip2px(getActivity(), 100);
		// find View
		mView = this.getView();
		mGridView = (GridView) mView.findViewById(R.id.currclub_grid);
		mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_curr_club);
		mGridView.setOnItemClickListener(this);
		mTimeColumnLayout = (LinearLayout) mView
				.findViewById(R.id.currclub_layout_schedule);
		leftRoom = (TextView) mView.findViewById(R.id.tv_room);

		// 设置今天的背景
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;

		for (int i = 0; i < DATE_IDS.length; i++) {
			if (day == i) {
				((TextView) mView.findViewById(DATE_IDS[i]))
						.setTextColor(Color.WHITE);
				((TextView) mView.findViewById(WEEK_IDS[i]))
						.setTextColor(Color.WHITE);
				((LinearLayout) mView.findViewById(LAYOUT_IDS[i]))
						.setBackgroundColor(Color.BLACK);
			}
		}

		if (AppConfig.getInstance().getUserId() != 0) {
			// set schedule timtable's first row -> Monday to Sunday.
			mDateView = new TextView[WEEK_COUNT];

			for (int i = 0; i < WEEK_COUNT; i++) {
				mDateView[i] = (TextView) mView.findViewById(DATE_IDS[i]);
			}
			// Set first week row -> Calendar
			Calendar c = Calendar.getInstance();
			mStartEndDate = TimeUtils.getWeekDate(c);

			updateHeader(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));

			// 首先加载本地
			setDefaultClassRoom();
			drawScheduleUI(mKeywords);

			// start task to get schedule
			ThreadPool.getInstance().execute(
					new ScheduleClub(new EventProtocolHandler(),
							AccountDetailUtil.getUserClub(), mStartEndDate[0],
							mStartEndDate[1]));

		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.toast_no_lookcourse),
					Toast.LENGTH_SHORT).show();
		}
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * EventBus -> event from FragmentCurr.wheel_submit.click
	 *
	 * @return void
	 * @throw
	 */
	public void onEventMainThread(CourseFilterEvent event) {
		if (event != null) {
			log.d("Event Bus get sql :" + event.getSql());
			if (event.getmFilter().equals("date")) {
				// mKeywords = "";
				mStartEndDate[0] = event.getStartEndDate()[0];
				mStartEndDate[1] = event.getStartEndDate()[1];
				mWeekOfYear = TimeUtils.getWeekOfYearByString(mStartEndDate[0]);
				mYear = TimeUtils.getYearByString(mStartEndDate[0]);
				// mProgressBar.setVisibility(View.VISIBLE);
				ThreadPool.getInstance().execute(
						new ScheduleClub(new EventProtocolHandler(),
								AccountDetailUtil.getUserClub(),
								mStartEndDate[0], mStartEndDate[1]));
			} else {
				mKeywords = event.getmSql();
				className = event.getmClassName();
				drawScheduleUI(mKeywords);
			}
		}
	}

	/**
	 * 设置默认教室
	 *
	 * @author yupu
	 * @date 2015年1月14日
	 */
	private void setDefaultClassRoom() {
		List<CourseBean> list = CourseUtil.getCourseFilterDataModel();
		if (list != null && list.size() != 0) {
			className = list.get(0).getClassRoomName();
			mKeywords = " " + CourseColumns.CLASSROOM_NAME + " = '" + className
					+ "'";
			leftRoom.setText(className); // 设置教室名字
		}
	}

	@Override
	public void onStart() {
		log.d("onCreate");
		super.onStart();
	}

	@Override
	public void onDestroy() {
		log.d("onDestroy");
		// EventBus unregister
		EventBus.getDefault().unregister(this);
		log.e("Event Bus unregister");

		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mScheduleList.get(position).size() != 0) {
			CourseBean bean = mScheduleList.get(position).get(0);
			bean.setFavorite(false);
			CoursePageActivity.ActionStart(getActivity(), bean);
		}
	}

	@Override
	public void refresh() {
		mProgressBar.setVisibility(View.VISIBLE);
		ThreadPool.getInstance().execute(
				new ScheduleClub(new EventProtocolHandler(), AccountDetailUtil
						.getUserClub(), mStartEndDate[0], mStartEndDate[1]));
	}

	/**
	 * updateHeader set first row, and return start_date and end_date for
	 * ScheduleClub protocol
	 *
	 * @param
	 */
	public void updateHeader(int year, int month, int day) {
		Calendar cd = Calendar.getInstance();
		cd.set(year, month, day);
		String[] times = TimeUtils.getWeekDate(cd);
		// set MM.dd to TextView
		for (int i = WEEK_COUNT - 1; i >= 0; i--) {
			String text = String.format("%d.%d", cd.get(Calendar.MONTH) + 1,
					cd.get(Calendar.DAY_OF_MONTH));
			mDateView[i].setText(text);
			cd.add(Calendar.DATE, -1);
		}

	}

	/**
	 * Draw Schedule UI
	 *
	 * @return void
	 * @throw
	 */
	public void drawScheduleUI(String sql) {
		// TODO by sql,such as keywords filter,, now have no time filter.
		log.d("get Course By Sql()  start! sql = " + sql + "----" + mWeekOfYear);
		mSql = CourseColumns.CLUB_ID + "=" + AccountDetailUtil.getUserClub()
				+ " and " + CourseColumns.YEAR + " = " + mYear + " and "
				+ CourseColumns.WEEKOFYEAR + " = " + mWeekOfYear;
		if (!sql.equals("")) {
			mSql = mSql + " and " + sql;
		}
		leftRoom.setText(className); // 设置教室名字
		ThreadPool.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				mCourseList = CourseUtil.getCourseBySql(mSql);
				log.e("drawScheduleUI()    ->  getCourseBySql.size():"
						+ mCourseList.size());
				if (mCourseList != null) {
					handler.sendEmptyMessage(100);
				}
			}
		});
	}

	public void afterData() {
		if (mCourseList == null || mCourseList.size() == 0) {
			((FragmentCurr) getParentFragment()).hideFilter();
			((FragmentCurr) getParentFragment()).setHide(true);
		} else {
			((FragmentCurr) getParentFragment()).showFilter();
			((FragmentCurr) getParentFragment()).setHide(false);
		}

		// get club course's start/end time
		getCourseTimeRange();
		log.d("getCourseTimeRange() Completed");
		// set schedule timetable's body, data from course table.
		setScheduleTimeBody();
		log.d("setScheduleTimeBody() Completed");

		// set schedule timetable's first Column -> startTime to endTime

		setScheduleTimeColumn();
		log.d("setScheduleTimeColumn() Completed");
		// set body Height
		ViewGroup.LayoutParams params = mGridView.getLayoutParams();
		params.height = COLUMN_HIEGHT * hourStart.size();
		mGridView.setLayoutParams(params);
		log.d("drawScheduleUI() Completed");
	}

	public void getCourseTimeRange() {
		// TODO 先拿行数，再去计算每一个的值
		hourStart.clear();
		int[] visited = new int[25]; // 判断这一个小时的时间段中是否有课
		for (int i = 0; i < mCourseList.size(); i++) {
			int start = Integer.parseInt(mCourseList.get(i).getStartTime()
					.split(":")[0]);
			if (visited[start] == 0) {
				visited[start] = 1;
				// TODO
				hourStart.add(start);
			}
		}
		Collections.sort(hourStart);
	}

	public void setScheduleTimeBody() {
		mScheduleList = new ArrayList<ArrayList<CourseBean>>();
		List<ItemNode> mItemNodes = new ArrayList<ItemNode>();
		// init Schedule Date Structure and init mItemNodes
		for (int i = 0; i < 7 * hourStart.size(); i++) {
			mScheduleList.add(new ArrayList<CourseBean>());
			mItemNodes.add(new ItemNode());
		}

		// fill Schedule Date structure, data from Course table.
		// index = 7 * （class_start - club_start） + day_of_week -1;
		for (int i = 0; i < mCourseList.size(); i++) {
			CourseBean bean = mCourseList.get(i);
			int time = Integer.parseInt(bean.getStartTime().split(":")[0]);

			// TODO
			// 在此排序算一下总共的
			int index = 7 * (hourStart.indexOf(time))
					+ TimeUtils.getDayOfWeekByString(bean.getScheduleDate())
					- 1;
			mScheduleList.get(index).add(bean);
		}

		// init grid view border, course bean to ItemNode.
		ItemNode node;
		for (int i = 0; i < 7 * hourStart.size(); i++) {
			node = new ItemNode();
			ArrayList<CourseBean> conflictList = mScheduleList.get(i);
			int conflictCount = conflictList.size();
			if (conflictCount == 0) {
				// fill blank space, with 1.border
			} else if (conflictCount == 1) {
				// fill ItemNode, with 1.border & 2.course color
				CourseBean bean = conflictList.get(0);
				// fomart node.mText for beautity
				if (bean.getClassName().length() == 4) {
					bean.setClassName(bean.getClassName().substring(0, 2)
							+ "\n" + bean.getClassName().substring(2, 4));
				}
				node.mText = bean.getClassName() + "\n"
						+ bean.getStartTime().substring(0, 5) + "\n~\n"
						+ bean.getEndTime().substring(0, 5) + "\n"
						+ bean.getCoachName();
				// set background color
				node.mBgColor = bean.getBackgroundColor();
				mItemNodes.set(i, node);

			} else {
				// [BUG] Actually should fill Items with 1.border & 2.course
				// color & 3.complex tags
				// now the first version, we just show the first.
				CourseBean bean = conflictList.get(0);
				// fomart node.mText for beautity
				if (bean.getClassName().length() == 4) {
					bean.setClassName(bean.getClassName().substring(0, 2)
							+ "\n" + bean.getClassName().substring(2, 4));
				}
				node.mText = bean.getClassName() + "\n"
						+ bean.getStartTime().substring(0, 5) + "\n~\n"
						+ bean.getEndTime().substring(0, 5) + "\n"
						+ bean.getCoachName();
				node.mBgColor = bean.getBackgroundColor();
				mItemNodes.set(i, node);
			}
		}

		// set GridView adapter
		ContentAdapter adapter = new ContentAdapter(this.getActivity(),
				mItemNodes);
		mGridView.setAdapter(adapter);

	}

	/**
	 * Set Schedule's first column. get courseStart and courseEnd from club
	 * table. through courseStart and courseEnd dynamically create text view.
	 *
	 * @return void
	 * @throw
	 */
	public void setScheduleTimeColumn() {
		// get courseStart and courseEnd from club table.
		// remove old UI for redraw.
		mTimeColumnLayout.removeAllViews();
		// draw time column.

		String am_pm = "";
		for (int i = 0; i < hourStart.size(); i++) {
			TextView tv_time = new TextView(getActivity());
			tv_time.setTextSize(FormatUtils.dip2px(getActivity(), 4));

			if (hourStart.get(i) <= 12) {
				am_pm = "AM";
			} else {
				am_pm = "PM";
				hourStart.set(i, hourStart.get(i) - 12);
			}

			tv_time.setText(hourStart.get(i) + "\n" + am_pm);
			tv_time.setWidth(50);
			tv_time.setHeight(COLUMN_HIEGHT - 1);
			tv_time.setGravity(Gravity.CENTER);

			mTimeColumnLayout.addView(tv_time);

			View view = new View(getActivity());
			view.setLayoutParams(new LayoutParams(50, 1));
			view.setBackgroundColor(getActivity().getResources().getColor(
					R.color.color_light_gray));
			mTimeColumnLayout.addView(view);
		}

	}

	private class ContentAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private List<ItemNode> mItemNodes;

		public ContentAdapter(Context context, List<ItemNode> itemNodes) {
			mItemNodes = itemNodes;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mItemNodes.size();
		}

		@Override
		public Object getItem(int position) {
			return mItemNodes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return (long) position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ItemHolder holder;
			// Set Itemlayout
			ItemNode node = mItemNodes.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listitem_currself,
						null);
				holder = new ItemHolder();
				holder.mContentView = (TextView) convertView
						.findViewById(R.id.currselfitem_content);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}
			holder.mContentView.setText(node.mText);

			if (node.mBgColor != null) {
				holder.mContentView.setBackgroundColor(android.graphics.Color
						.parseColor(node.mBgColor));
			}
			// get layout's weight
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int width = dm.widthPixels;// 宽度
			// Set Item's width and height determined by position
			// First vertical row is curriculum's time, as 1/15. other is 2/15

			LayoutParams param;
			final int a = position;
			width = width - 50;
			param = new LayoutParams(width / 7, COLUMN_HIEGHT);

			convertView.setLayoutParams(param);

			return convertView;
		}

	}

	public class ItemNode {
		public String mText;
		public String mBgColor;
	}

	;

	private class ItemHolder {
		public TextView mContentView;
	}

	private class EventProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				mProgressBar.setVisibility(View.INVISIBLE);
				// Redraw UI
				setDefaultClassRoom();
				drawScheduleUI(mKeywords);
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				mProgressBar.setVisibility(View.INVISIBLE);
				setDefaultClassRoom();
				drawScheduleUI("");
				Toast.makeText(getActivity(), "尚未加入任何俱乐部！", Toast.LENGTH_SHORT)
						.show();
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				mProgressBar.setVisibility(View.INVISIBLE);
				setDefaultClassRoom();
				drawScheduleUI("");
				Toast.makeText(getActivity(), "请检查网络是否正确！", Toast.LENGTH_SHORT)
						.show();
				break;
			case Event.ERROR:
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}
		}
	}
}
