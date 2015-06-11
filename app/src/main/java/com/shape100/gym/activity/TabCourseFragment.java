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
import android.widget.ListView;
import android.widget.TextView;

import com.shape100.gym.Logger;
import com.shape100.gym.R;

public class TabCourseFragment extends BaseFragment implements
		OnItemClickListener {

	private static final Logger log = Logger.getLogger("TabCourseFragment");

	private ListView mListView;
	private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onCreateView");
		}
		return inflater.inflate(R.layout.tabcourse_fragment, container, false);
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

		mListView = (ListView) getView().findViewById(R.id.courselist);

		ItemNode node = new ItemNode();
		node.mName = "普拉提";
		node.mCount = 120;
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "瑜伽";
		node.mCount = 20;
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "快乐单车";
		node.mCount = 230;
		mItemNodes.add(node);

		node = new ItemNode();
		node.mName = "街舞";
		node.mCount = 40;
		mItemNodes.add(node);

		ContentAdapter adapter = new ContentAdapter(this.getActivity(),
				mItemNodes);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

		super.onActivityCreated(savedInstanceState);
	}

	public class ItemNode {
		public String mName;
		public int mCount;
	};

	private class ItemHolder {
		public TextView mNameView;
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
				convertView = mInflater.inflate(R.layout.listitem_course, null);
				holder = new ItemHolder();
				holder.mNameView = (TextView) convertView
						.findViewById(R.id.courseitem_name);
				holder.mCountView = (TextView) convertView
						.findViewById(R.id.courseitem_count);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}

			holder.mNameView.setText(node.mName);
			holder.mCountView.setText("" + node.mCount);
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this.getActivity(),
				CourseDetailActivity.class);
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
