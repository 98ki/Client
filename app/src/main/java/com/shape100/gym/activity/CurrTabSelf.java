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
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.activity.FragmentCurr.MyFragment;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.CourseBean;
import com.shape100.gym.protocol.CourseFavorites;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.CourseFavoriteUtil;
import com.shape100.gym.provider.CourseUtil;
import com.shape100.gym.provider.DBConst.CourseColumns;
import com.shape100.widget.MoreFavoriteView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event.FavoriteFilterEvent;

public class CurrTabSelf extends BaseFragment implements OnItemClickListener,
		MyFragment {
	// Data
	private static final Logger log = Logger.getLogger("CurrTabSelf");

	private static int COLUMN_HIEGHT = 0;
	private final int WEEK_COUNT = 7;
	private static int[] DATE_IDS = { R.id.curr_self_date01,
			R.id.curr_self_date02, R.id.curr_self_date03,
			R.id.curr_self_date04, R.id.curr_self_date05,
			R.id.curr_self_date06, R.id.curr_self_date07 };
	private static int[] WEEK_IDS = { R.id.curr_self_week1,
			R.id.curr_self_week2, R.id.curr_self_week3, R.id.curr_self_week4,
			R.id.curr_self_week5, R.id.curr_self_week6, R.id.curr_self_week7, };
	private static int[] LAYOUT_IDS = { R.id.bg_self_week1, R.id.bg_self_week2,
			R.id.bg_self_week3, R.id.bg_self_week4, R.id.bg_self_week5,
			R.id.bg_self_week6, R.id.bg_self_week7 };

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
	LayoutParams param; // 每个item的宽高

	private List<Integer> hourStart;

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
		return inflater.inflate(R.layout.currtab_self, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.d("onCreate");
		EventBus.getDefault().register(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		log.d("onDestroy");
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onActivityCreated");
		}
		COLUMN_HIEGHT = FormatUtils.dip2px(getActivity(), 100);
		// find View
		mView = this.getView();
		mGridView = (GridView) mView.findViewById(R.id.currself_grid);
		mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_curr_self);

		hourStart = new ArrayList<Integer>();
		mGridView.setOnItemClickListener(this);
		mTimeColumnLayout = (LinearLayout) mView
				.findViewById(R.id.currself_layout_schedule);

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

			// start task to get schedule
			ThreadPool.getInstance().execute(
					new CourseFavorites(new EventProtocolHandler(),
							mStartEndDate[0], mStartEndDate[1]));

		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.toast_no_lookcourse),
					Toast.LENGTH_SHORT).show();
		}

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (Logger.DBG) {
			log.d("onCreate");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (Logger.DBG) {
			log.d("onResume");
		}
		drawScheduleUI("");
		super.onResume();
	}

	@Override
	public void onPause() {
		if (Logger.DBG) {
			log.d("onPause");
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		if (Logger.DBG) {
			log.d("onStop");
		}
		super.onStop();
	}

	public void onEventMainThread(FavoriteFilterEvent event) {
		log.d("Event Bus On Main Thread start!");

		if (event != null) {
			log.d("Event Bus get sql :" + event.getSql());
			if (event.getmFilter().equals("date")) {
				mKeywords = "";
				mStartEndDate[0] = event.getStartEndDate()[0];
				mStartEndDate[1] = event.getStartEndDate()[1];
				mWeekOfYear = TimeUtils.getWeekOfYearByString(mStartEndDate[0]);
				mYear = TimeUtils.getYearByString(mStartEndDate[0]);
				mProgressBar.setVisibility(View.VISIBLE);
				ThreadPool.getInstance().execute(
						new CourseFavorites(new EventProtocolHandler(),
								mStartEndDate[0], mStartEndDate[1]));
			} else if (event.getmFilter().equals("keyword")) {
				mKeywords = event.getSql();
				drawScheduleUI(event.getSql());
			} else {
				drawScheduleUI("");
			}
		}
	}

	@Override
	public void refresh() {
		 drawScheduleUI("");
	//	ThreadPool.getInstance().execute(
	//			new CourseFavorites(new EventProtocolHandler(),
	//					mStartEndDate[0], mStartEndDate[1]));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mScheduleList.get(position).size() != 0) {
			initClickItems(position);
		}
	}

	private void initClickItems(int position) {
		ArrayList<CourseBean> courseBeans = mScheduleList.get(position);
		ArrayList<ItemNode> itemNodes = new ArrayList<ItemNode>();
		for (int i = 0; i < courseBeans.size(); i++) {
			CourseBean bean = courseBeans.get(i);
			ItemNode itemNode = new ItemNode();
			itemNode.mText = bean.getClassName() + "\n"
					+ bean.getStartTime().substring(0, 5) + "\n~\n"
					+ bean.getEndTime().substring(0, 5) + "\n"
					+ bean.getCoachName();
			itemNode.mBgColor = bean.getBackgroundColor();
			itemNodes.add(itemNode);
		}

		new MoreFavoriteView(getActivity(), R.layout.currtab_self,
				R.layout.more_favorite_course, itemNodes, param, courseBeans)
				.showPop();
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
	 * @param sql
	 * @return void
	 * @throw
	 */
	public void drawScheduleUI(String sql) {
		// TODO by sql,such as keywords filter,, now have no time filter.
		log.d("get Course By Sql()  start! sql = " + sql);
		mSql = CourseColumns.YEAR + " = " + mYear + " and "
				+ CourseColumns.WEEKOFYEAR + " = " + mWeekOfYear;
		if (sql.equals("")) {

		} else {
			mSql = mSql + " and " + sql;
		}
		ThreadPool.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				mCourseList = CourseFavoriteUtil.getFavoriteBySql(mSql);
				log.e("drawScheduleUI()     ->  getCourseBySql.size():"
						+ mCourseList.size());

				if (mCourseList != null) {
					handler.sendEmptyMessage(100);
				}
			}
		});
	}

	private void afterData() {
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
		int[] visited = new int[25];
		hourStart.clear();
		;
		for (int i = 0; i < mCourseList.size(); i++) {
			int start = Integer.parseInt(mCourseList.get(i).getStartTime()
					.split(":")[0]);
			if (visited[start] == 0) {
				visited[start] = 1;
				hourStart.add(start);
			}
		}
		Collections.sort(hourStart);
	}

	public void setScheduleTimeBody() {
		mScheduleList = new ArrayList<ArrayList<CourseBean>>();
		List<ItemNode> mItemNodes = new ArrayList<ItemNode>();
		// init Schedule Date Structure and init mItemNodes
		ItemNode item = new ItemNode();
		for (int i = 0; i < 7 * hourStart.size(); i++) {
			mScheduleList.add(new ArrayList<CourseBean>());
			mItemNodes.add(item);
		}

		// fill Schedule Date structure, data from Course table.
		// index = 7 * （class_start - club_start） + day_of_week -1;
		for (int i = 0; i < mCourseList.size(); i++) {
			CourseBean bean = mCourseList.get(i);
			int time = Integer.parseInt(bean.getStartTime().split(":")[0]);
			int index = 7 * hourStart.indexOf(time)
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
			// tv_time.setTextColor(0xFFFFFF);
			tv_time.setGravity(Gravity.CENTER);

			mTimeColumnLayout.addView(tv_time);
			View view = new View(getActivity());
			view.setLayoutParams(new LayoutParams(50, 1));
			view.setBackgroundColor(getActivity().getResources().getColor(
					R.color.color_light_gray));
			mTimeColumnLayout.addView(view);
		}

	}

	public class ContentAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();

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

			final int a = position;
			width = width - 50;
			param = new LayoutParams(width / 7, COLUMN_HIEGHT);
			// convertView.setOnClickListener(new OnClickListener(){
			//
			// @Override
			// public void onClick(View v) {
			// Log.e("_______", a+"");
			//
			// }});

			convertView.setLayoutParams(param);

			return convertView;
		}

		class ItemHolder {
			public TextView mContentView;
		}
	}

	public class ItemNode {
		public String mText;
		public String mBgColor;
	}

	private class EventProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				// Redraw UI
				drawScheduleUI(mKeywords);
				mKeywords = "";
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				mProgressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity(), "获取个人课表失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				mProgressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity(),
						getResources().getString(R.string.toast_no_net),
						Toast.LENGTH_SHORT).show();
				break;

			}
		}
	}

}
