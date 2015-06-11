package com.shape100.gym.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 滑动浏览相框
 * 
 * @author yupu
 * @date 2015年2月5日
 */
public class PhotoPageAdapter extends PagerAdapter {
	private ArrayList<View> views;
	private Context context;

	public PhotoPageAdapter(Context context) {
		this.context = context;
		views = new ArrayList<View>();
	}

	public void update(ArrayList<View> views) {
		this.views = views;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
