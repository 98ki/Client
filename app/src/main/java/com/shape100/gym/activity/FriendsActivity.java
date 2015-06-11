package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shape100.gym.R;

public class FriendsActivity extends SlideActivity {

	private ListView mListView;
	private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friends);

		mListView = (ListView) findViewById(R.id.friendslist);

		ItemNode node = new ItemNode();
		node.mName = "张强";
		node.mNote = "没有整洁的外表，根本没人会去在意你美好的内心";
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "常立伟";
		node.mNote = "柴米油盐酱醋茶两个人来调和";
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "大熊";
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "刘海涛";
		node.mNote = "...";
		mItemNodes.add(node);

		ContentAdapter adapter = new ContentAdapter(this, mItemNodes);
		mListView.setAdapter(adapter);
	}

	public class ItemNode {
		public String mName;
		public String mNote;
	};

	private class ItemHolder {
		public ImageView mAvatarView;
		public TextView mNameView;
		public TextView mNoteView;
	};

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

			ItemNode node = mItemNodes.get(position);
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.listitem_friends, null);
				holder = new ItemHolder();
				holder.mAvatarView = (ImageView) convertView
						.findViewById(R.id.friendsitem_avatar);
				holder.mNameView = (TextView) convertView
						.findViewById(R.id.friendsitem_name);
				holder.mNoteView = (TextView) convertView
						.findViewById(R.id.friendsitem_note);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}

			holder.mAvatarView.setBackgroundResource(R.drawable.ic_head);
			holder.mNameView.setText(node.mName);
			holder.mNoteView.setText(node.mNote);
			return convertView;
		}

	}
}
