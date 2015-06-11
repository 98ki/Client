package com.shape100.gym.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shape100.gym.R;

public class TimeAdapter extends BaseAdapter {
	private Context context;
	private String[] data;
	private boolean flag;

	public TimeAdapter(Context context, String[] data,boolean flag) {
		this.context = context;
		this.data = data;
		this.flag=flag;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		view = LayoutInflater.from(context).inflate(R.layout.list_time, parent,false);
		TextView time = (TextView) view.findViewById(R.id.tv_time);
		ImageView imageView=(ImageView) view.findViewById(R.id.img_pic);
		if (flag) {
			imageView.setVisibility(View.VISIBLE);
		}else {
			imageView.setVisibility(View.GONE);
		}
		time.setText(data[position]);
		return view;
	}
}
