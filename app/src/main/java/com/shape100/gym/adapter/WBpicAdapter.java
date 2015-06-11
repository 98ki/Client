package com.shape100.gym.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.R;

public class WBpicAdapter extends BaseAdapter {
	private List<String> pics;
	private LayoutInflater inflater;

	public WBpicAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
		pics = new ArrayList<String>();
	}

	public void upDate(List<String> pics) {
		this.pics = pics;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return pics.size();
	}

	@Override
	public Object getItem(int position) {
		return pics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		view = inflater.inflate(R.layout.include_wb_pic_style, parent, false);
		ImageView img = (ImageView) view.findViewById(R.id.wb_show_img);
		ImageLoader.getInstance().displayImage(pics.get(position), img);
		return view;
	}
}
