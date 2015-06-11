//package com.shape100.widget;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import android.content.Context;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.laoyoubang.util.FormatUtils;
//import com.shape100.gym.R;
//import com.shape100.gym.model.CourseBean;
//import com.shape100.gym.provider.CourseUtil;
//
///**    
// * A schedule for Shape100  
// * 
// * project：shape100    
// * class：ShapeSchedule    
// * desc：    Schedule for Shape100
// * author：zpy zpy@98ki.com    
// * create date：2014-11-13 下午8:37:19    
// * modify author: zpy    
// * update date：2014-11-13 下午8:37:19    
// * update remark：    
// * @version   1.0        
// */
//public class ShapeSchedule extends LinearLayout implements android.widget.AdapterView.OnItemClickListener{
//	/**
//	 * 
//	 */
//	private Context mContext;
//	private static int COLUMN_HIEGHT = 0;
//	private final int WEEK_COUNT = 7;
//	private static int[] DATE_IDS = { R.id.curr_date01, R.id.curr_date02, R.id.curr_date03, R.id.curr_date04,
//		R.id.curr_date05, R.id.curr_date06, R.id.curr_date07 };
//	
//	private ArrayList<CourseBean> mCourseList;
//	private int mCourseStart = 24;
//	private int mCourseEnd = -1;
//	
//	
//	private GridView mGridView;
//	/** time column , first column of schedule.**/
//	private LinearLayout mTimeColumnLayout;
//	private TextView[] mDateView;
//	
//
//
//	public ShapeSchedule(Context context, ArrayList<CourseBean> courseList){
//		super(context);
//		mContext = context;
//		mCourseList = courseList;
//		COLUMN_HIEGHT = FormatUtils.dip2px(context, 100);
//		
//		// get courselist
//		
//		mCourseList = CourseUtil.getCourseByClubId();	
//		this.setOrientation(HORIZONTAL);
////		// set schedule timtable's first row -> Monday to Sunday.		
////		LinearLayout firstRowLayout = new LinearLayout(context);
////		firstRowLayout.setOrientation(VERTICAL);
////		
////		LinearLayout layout1 = new  LinearLayout(context);
////		LayoutParams param = new LayoutParams(49,LayoutParams.WRAP_CONTENT);
////		layout1.setLayoutParams(param);
////		//layout1.setGravity();
////		firstRowLayout.addView(layout1);
////		
////		LinearLayout layout2 = new  LinearLayout(context);
////		LayoutParams param = new LayoutParams(49,LayoutParams.WRAP_CONTENT);
////		layout1.setLayoutParams(param);
////		//layout1.setGravity();
////		firstRowLayout.addView(layout1);
////		 <LinearLayout
////         android:layout_width="1px"
////         android:layout_height="match_parent"
////         android:background="#808080" >
////     </LinearLayout>
////		mDateView = new TextView[WEEK_COUNT];
////		for (int i = 0; i < WEEK_COUNT; i++) {
////			LinearLayout layout2 = new LinearLayout(context); 
////			layout2.setP
////			TextView weekView = new TextView(context);
////			weekView.setHeight(LayoutParams.WRAP_CONTENT);
////			weekView.setWidth(LayoutParams.WRAP_CONTENT);
////			mDateView[i] = weekView;
////		}
//		// set GridView
//		mGridView = new GridView(context);
//		mGridView.setNumColumns(7);
//		
//		mGridView.setOnItemClickListener(this);
//		//mTimeColumnLayout = (LinearLayout)findViewById(R.id.currclub_layout_schedule);
//
//		mGridView.setBackgroundColor(555555);
//		this.setBackgroundColor(252525);
//	addView(mGridView);
//		// set schedule dynamic UI -> first column and body
////		drawScheduleUI();
////
////		// Set first week row -> Calendar
////		Calendar c = Calendar.getInstance();
////		int year = c.get(Calendar.YEAR);
////		int month = c.get(Calendar.MONTH);
////		int day = c.get(Calendar.DAY_OF_MONTH);
////		updateHeader(year, month, day);
//	}
//
//	
//	
//	public void refresh() {
//		drawScheduleUI();
//	}
//	
//	public void updateHeader(int year, int month, int day) {
//		Calendar cd = Calendar.getInstance();
//		cd.set(year, month, day);
//	
//		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
//		if (dayOfWeek == 0) {
//			dayOfWeek = 7;
//		}
//	
//		int offset = 1 - dayOfWeek;
//	
//		cd.add(Calendar.DATE, offset - 1);
//	
//		for (int i = 0; i < WEEK_COUNT; i++) {
//			cd.add(Calendar.DATE, 1);
//			String text = String.format("%d.%d", cd.get(Calendar.MONTH) + 1, cd.get(Calendar.DAY_OF_MONTH));
//			mDateView[i].setText(text);
//		}
//	
//	}
//
//
//
//	/**
//	 * Draw Schedule UI
//	 * 
//	 * @throw
//	 * @return void
//	 */
//	public void drawScheduleUI() {
//		// get club course's start/end time
//		getCourseTimeRange();
//		// set schedule timetable's body, data from course table.
//		setScheduleTimeBody();
//		// set schedule timetable's first Column -> startTime to endTime
//		setScheduleTimeColumn();
//		// set body Height
//		ViewGroup.LayoutParams params = mGridView.getLayoutParams();
//		params.height = COLUMN_HIEGHT * (mCourseEnd - mCourseStart + 1);
//		mGridView.setLayoutParams(params);
//
//	}
//
//	public void getCourseTimeRange() {
//		for (int i = 0; i < mCourseList.size(); i++) {
//			int start = Integer.parseInt(mCourseList.get(i).getStartTime().split(":")[0]);
//			int end = Integer.parseInt(mCourseList.get(i).getEndTime().split(":")[0]);
//			if (mCourseStart > start) {
//				mCourseStart = start;
//			}
//			if (mCourseEnd < end) {
//				mCourseEnd = end;
//			}
//		}
//		Log.e("__ShapeSchedule____","courseStart:" + mCourseStart + "courseEnd:" + mCourseEnd);
//
//	}
//
//	public void setScheduleTimeBody() {
//		List<ArrayList<CourseBean>> mScheduleList = new ArrayList<ArrayList<CourseBean>>();
//		List<ItemNode> mItemNodes = new ArrayList<ItemNode>();
//
//		// init Schedule Date Structure and init mItemNodes
//		ItemNode item = new ItemNode();
//		for (int i = 0; i < 7 * (mCourseEnd - mCourseStart + 1); i++) {
//			mScheduleList.add(new ArrayList<CourseBean>());
//			mItemNodes.add(item);
//		}
//		// fill Schedule Date structure, data from Course table.
//		// index = 7 * （class_start - club_start） + day_of_week -1;
//		for (int i = 0; i < mCourseList.size(); i++) {
//
//			CourseBean bean = mCourseList.get(i);
//			int time = Integer.parseInt(bean.getStartTime().split(":")[0]);
//			int index = 7 * (time - mCourseStart) + bean.getDayOfWeek() - 1;
//			mScheduleList.get(index).add(bean);
//		}
//		// init grid view border, course bean to ItemNode.
//		ItemNode node;
//		for (int i = 0; i < 7 * (mCourseEnd - mCourseStart + 1); i++) {
//			node = new ItemNode();
//			ArrayList<CourseBean> conflictList = mScheduleList.get(i);
//			int conflictCount = conflictList.size();
//			if (conflictCount == 0) {
//				// fill blank space, with 1.border
//
//			} else if (conflictCount == 1) {
//				// fill ItemNode, with 1.border & 2.course color
//				CourseBean bean = conflictList.get(0);
//				node.mText = bean.getClassName() + "\n" + bean.getStartTime() + "~" + bean.getEndTime() + "\n"
//						+ bean.getCoachName();
//				node.mBgColor = bean.getBackgroundColor();
//				mItemNodes.set(i, node);
//
//			} else {
//				// fill Item屡有， with 1.border & 2.course color & 3.complex tags
//			}
//
//		}
//
//		// set GridView adapter
//		ContentAdapter adapter = new ContentAdapter(mContext, mItemNodes);
//		mGridView.setAdapter(adapter);
//
//	}
//
//	/**
//	 * Set Schedule's first column. get courseStart and courseEnd from club
//	 * table. through courseStart and courseEnd dynamically create text view.
//	 * 
//	 * @throw
//	 * @return void
//	 */
//	public void setScheduleTimeColumn() {
//		// get courseStart and courseEnd from club table.
//		// remove old UI for redraw.
//		mTimeColumnLayout.removeAllViews();
//		// draw time column.
//
//		String am_pm = "";
//		for (int i = mCourseStart; i <= mCourseEnd; i++) {
//			TextView tv_time = new TextView(mContext);
//			if (i < 12) {
//				am_pm = "AM";
//			} else {
//				am_pm = "PM";
//			}
//			tv_time.setText(i + "\n" + am_pm);
//			tv_time.setWidth(50);
//			tv_time.setHeight(COLUMN_HIEGHT);
//			// tv_time.setTextColor(0xFFFFFF);
//			tv_time.setGravity(Gravity.CENTER);
//
//			mTimeColumnLayout.addView(tv_time);
//
//		}
//
//	}
//	public void setCourseList(ArrayList<CourseBean> courseList){
//		mCourseList = courseList;
//	}
//	public ArrayList<CourseBean> getCourseList(){
//		return mCourseList;
//	}
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	private class ContentAdapter extends BaseAdapter {
//
//		private LayoutInflater mInflater;
//		private List<ItemNode> mItemNodes;
//
//		public ContentAdapter(Context context, List<ItemNode> itemNodes) {
//			mItemNodes = itemNodes;
//			mInflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public int getCount() {
//			return mItemNodes.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return mItemNodes.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return (long) position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ItemHolder holder;
//			// Set Itemlayout
//			ItemNode node = mItemNodes.get(position);
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.listitem_currself, null);
//				holder = new ItemHolder();
//				holder.mContentView = (TextView) convertView.findViewById(R.id.currselfitem_content);
//				convertView.setTag(holder);
//			} else {
//				holder = (ItemHolder) convertView.getTag();
//			}
//			holder.mContentView.setText(node.mText);
//
//			if (node.mBgColor != null) {
//				holder.mContentView.setBackgroundColor(android.graphics.Color.parseColor(node.mBgColor));
//			}
//			
//			// get layout's weight
//			DisplayMetrics dm = getResources().getDisplayMetrics();
//			int width = dm.widthPixels;// 宽度
//			// Set Item's width and height determined by position
//			// First vertical row is curriculum's time, as 1/15. other is 2/15
//
//			LayoutParams param;
//			final int a = position;
//			width = width - 50;
//			param = new LayoutParams(width / 7, COLUMN_HIEGHT);
//			// convertView.setOnClickListener(new OnClickListener(){
//			//
//			// @Override
//			// public void onClick(View v) {
//			// Log.e("_______", a+"");
//			//
//			// }});
//
//			convertView.setLayoutParams(param);
//
//			return convertView;
//		}
//
//	}
//	
//	
//	
//	
//	
//	public class ItemNode {
//		public String mText;
//		public String mBgColor;
//	};
//
//	private class ItemHolder {
//		public TextView mContentView;
//	}
//
//
//
//
//
//
//}
