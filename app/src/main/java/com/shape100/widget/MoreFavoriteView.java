package com.shape100.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.shape100.gym.R;
import com.shape100.gym.activity.CoursePageActivity;
import com.shape100.gym.activity.CurrTabSelf.ItemNode;
import com.shape100.gym.adapter.FavoriteAdapter;
import com.shape100.gym.model.CourseBean;

public class MoreFavoriteView extends PopupWindow implements
		OnItemClickListener {
	private Context mcontext;
	private View parent, popView;
	private LayoutInflater inflater;
	private ArrayList<ItemNode> itemNodes;
	private GridView mGridView;
	private FavoriteAdapter adapter;
	private LayoutParams params;
	private ArrayList<CourseBean> courseBeans;
	String mDate;

	public MoreFavoriteView(Context context, int parentID, int popViewID,
			ArrayList<ItemNode> itemNodes, LayoutParams params,
			ArrayList<CourseBean> courseBeans) {
		mcontext = context;
		this.itemNodes = itemNodes;
		this.params = params;
		this.courseBeans = courseBeans;
		inflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		parent = inflater.inflate(parentID, null);
		popView = inflater.inflate(popViewID, null);
		mGridView = (GridView) popView.findViewById(R.id.gride_more);
		initPop();
		initData();
	}

	private void initData() {
		adapter = new FavoriteAdapter(mcontext, itemNodes, params);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}

	private void initPop() {
		this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.setContentView(popView);
		this.setHeight(LayoutParams.WRAP_CONTENT); //
		this.setWidth(LayoutParams.MATCH_PARENT); // 可以自定义改变
		this.setFocusable(true); // 使窗口可点击
		this.setOutsideTouchable(true); // 设置点击外面可点
		this.setBackgroundDrawable(new BitmapDrawable()); // 设置背景不为空，才可以外点击
		this.setAnimationStyle(R.style.AnimBottom);
	}

	public void showPop() {
		this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CourseBean bean = courseBeans.get(position);
		bean.setFavorite(true); // 表示此课程是喜爱
		CoursePageActivity.ActionStart(mcontext, bean);
		dismiss();
	}
}
