package com.shape100.gym.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.R;
import com.shape100.gym.model.FileFolder;

public class MorePicListAdapter extends BaseAdapter {
	private Context context;
	private List<FileFolder> imgPath;

	public MorePicListAdapter(Context context) {
		this.context = context;
		imgPath = new ArrayList<FileFolder>();
	}

	public void upDate(List<FileFolder> data) {
		imgPath = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return imgPath.size();
	}

	@Override
	public Object getItem(int position) {
		return imgPath.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_list_pic,
					parent, false);
			holder.img = (ImageView) view.findViewById(R.id.iv_item_pic);
			holder.text = (TextView) view.findViewById(R.id.tv_item_text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ImageLoader.getInstance().displayImage(
				"file://" + imgPath.get(position).getContent().get(0),
				holder.img);
		holder.text.setText(imgPath.get(position).getName() + "  ("
				+ imgPath.get(position).getContent().size() + ")");
		return view;
	}

	class ViewHolder {
		ImageView img;
		TextView text;
	}
}
