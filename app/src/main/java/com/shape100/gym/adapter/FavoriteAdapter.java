package com.shape100.gym.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shape100.gym.R;
import com.shape100.gym.activity.CurrTabSelf.ItemNode;

public class FavoriteAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ItemNode> mItemNodes;
	LayoutParams params;

	public FavoriteAdapter(Context context, List<ItemNode> itemNodes,
			LayoutParams params) {
		mItemNodes = itemNodes;
		this.params = params;
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
			convertView = mInflater.inflate(R.layout.listitem_currself, null);
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
		convertView.setLayoutParams(params);
		return convertView;
	}

	class ItemHolder {
		public TextView mContentView;
	}
}
