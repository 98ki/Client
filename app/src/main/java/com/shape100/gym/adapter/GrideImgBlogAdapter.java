package com.shape100.gym.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.R;

public class GrideImgBlogAdapter extends BaseAdapter {
	private List<String> pics;
	private Context mContext;
	private BlogClosePhotoChoiceListener listener;

	public GrideImgBlogAdapter(Context context) {
		mContext = context;
		pics = new ArrayList<String>();
		this.listener = (BlogClosePhotoChoiceListener) context;
	}

	public void upDate(List<String> pics) {
		this.pics = pics;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return pics.size() + 1;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		view = LayoutInflater.from(mContext).inflate(R.layout.item_img_gride,
				parent, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.img);
		ImageView closeView = (ImageView) view.findViewById(R.id.img_check);
		closeView.setImageResource(R.drawable.compose_photo_close);
		closeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.choice(position);
			}
		});
		if (position == pics.size()) {
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(R.drawable.compose_pic_add);
			closeView.setVisibility(View.GONE);
		} else {
			ImageLoader.getInstance().displayImage(
					"file://" + pics.get(position), imageView);
		}
		return view;
	}

	public void setOnCloseItemClickListener(
			BlogClosePhotoChoiceListener listener) {

	}

	public interface BlogClosePhotoChoiceListener {
		void choice(int position);
	}
}
