package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shape100.gym.Logger;
import com.shape100.gym.R;

public class TabTrainerFragment extends BaseFragment implements
		OnItemClickListener {

	private static final Logger log = Logger.getLogger("TabTrainerFragment");

	private ListView mListView;
	private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onCreateView");
		}
		return inflater.inflate(R.layout.tabtrainer_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onCreate");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onActivityCreated");
		}

		mListView = (ListView) getView().findViewById(R.id.trainerlist);

		ItemNode node = new ItemNode();
		node.mName = "张强";
		node.mNote = "没有整洁的外表，根本没人会去在意你美好的内心";
		node.mCount = 1000;
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "常立伟";
		node.mNote = "柴米油盐酱醋茶两个人来调和";
		node.mCount = 800;
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "大熊";
		node.mNote = "生活中原本只有红.蓝.黄三原色，红得发紫，紫得发青了，所以有人黑了，给生活一点点淡淡的红黄蓝，才有七彩的斑斓！";
		node.mCount = 556;
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "刘海涛";
		node.mNote = "...";
		node.mCount = 30;
		mItemNodes.add(node);

		ContentAdapter adapter = new ContentAdapter(this.getActivity(),
				mItemNodes);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

		super.onActivityCreated(savedInstanceState);
	}

	public class ItemNode {
		public String mName;
		public String mNote;
		public int mCount;
	};

	private class ItemHolder {
		public ImageView mAvatarView;
		public TextView mNameView;
		public TextView mNoteView;
		public TextView mCountView;
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
						.inflate(R.layout.listitem_trainer, null);
				holder = new ItemHolder();
				holder.mAvatarView = (ImageView) convertView
						.findViewById(R.id.traineritem_avatar);
				holder.mNameView = (TextView) convertView
						.findViewById(R.id.traineritem_name);
				holder.mNoteView = (TextView) convertView
						.findViewById(R.id.traineritem_note);
				holder.mCountView = (TextView) convertView
						.findViewById(R.id.traineritem_count);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}

			holder.mAvatarView.setBackgroundResource(R.drawable.ic_head);
			holder.mNameView.setText(node.mName);
			holder.mNoteView.setText(node.mNote);
			holder.mCountView.setText("" + node.mCount);
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this.getActivity(),
				TrainerDetailActivity.class);
		this.startActivity(intent);
	}

	@Override
	public void onStart() {
		if (Logger.DBG) {
			log.d("onStart");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (Logger.DBG) {
			log.d("onResume");
		}
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

	@Override
	public void onDestroy() {
		if (Logger.DBG) {
			log.d("onDestroy");
		}
		super.onDestroy();
	}

}
