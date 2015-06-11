package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.protocol.ClubNearby;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

public class ClubListFragment extends BaseFragment implements
		OnItemClickListener {
	/* View */
	private PullToRefreshListView mPullRefreshListView;
	/* Data */
	private static int page = 1;
	private static final Logger log = Logger.getLogger("ClubListFragment");
	private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();
	private List<ClubBean> mClubItems = new ArrayList<ClubBean>();
	private ContentAdapter adapter;
	private DisplayImageOptions options;
	private String mQuery = "";
	private boolean isLastPage = false;
	double geoLat = 0;
	double geoLng = 0;

	public Handler handler = new Handler() {// 处理UI绘制
		@Override
		public void handleMessage(Message msg) {
			log.d("get handler! msg:" + msg);

			FragmentClub parent = (FragmentClub) getParentFragment();
			geoLat = parent.getGeoLat();
			geoLng = parent.getGetLng();
			log.d("get HandleHandler.getLat:" + geoLat);

			// Init pull_to_refresh_list widget
			initPullToRefresh();

			// task to get json
			ThreadPool.getInstance().execute(
					new ClubNearby(new EventProtocolHandler(), geoLat, geoLng,
							page, mQuery));

			// new ClubNearby(new EventProtocolHandler(), geoLat, geoLng, page,
			// mQuery, mClubItems).start();
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.d("onCreateView");
		return inflater.inflate(R.layout.clublist_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.d("onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		log.d("onActivityCreated");

		super.onActivityCreated(savedInstanceState);
		log.d("onActivityCreated Completed");

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		log.d("onViewCreated");
		// init cache image options
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_club_default_logo)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// get location ,run in UI Tread
		log.d("onViewCreated.getLocation()");
		getLocation();
		log.d("onViewCreated.getLocation() Completed");

		log.d("onViewCreated Complete");
	}

	/**
	 * Get Location in UI thread ,then run nearbyClub task
	 *
	 * @return void
	 * @throw
	 */
	public void getLocation() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				log.d("getLocationThread start");
				int count = 0;
				int value = 0;
				FragmentClub parent = (FragmentClub) getParentFragment();

				do {
					geoLat = parent.getGeoLat();
					geoLng = parent.getGetLng();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					value = (int) geoLat;
					log.d("runOnGetLocationThread :" + count);
				} while (count < 100 && value == 0);

				log.d("getLocationThread end");

				// handler
				Message msg = handler.obtainMessage();
				msg.what = ConstVar.HANDLER_MSG_SUCCESS;
				handler.sendMessage(msg);// 发送
			}
		}).start();
	}

	/**
	 * init Pull to refresh widget set pull up/down event.
	 *
	 * @return void
	 * @throw
	 */
	public void initPullToRefresh() {
		mPullRefreshListView = (PullToRefreshListView) getView().findViewById(
				R.id.ptrl_clublist);
		// set listview adapter
		adapter = new ContentAdapter(this.getActivity(), mItemNodes);
		mPullRefreshListView.setAdapter(adapter);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					/**
					 * Pull Down to refresh, get new location,clear old items
					 * and old listview's nodes
					 *
					 * @throw
					 * @return void
					 */
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(getActivity()
								.getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// clear data
						page = 1;
						mClubItems.clear();
						mItemNodes.clear();
						ThreadPool.getInstance().execute(
								new ClubNearby(new EventProtocolHandler(),
										geoLat, geoLng, page, mQuery));
					}

					/**
					 * Pull up to refresh, get new location,clear old mClubItems
					 * who load json, retain list's nodes just add new nodes to
					 * listview
					 *
					 * @throw
					 * @return void
					 */

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(
										getResources().getString(
												R.string.load_more));
						refreshView.getLoadingLayoutProxy().setPullLabel(
								getResources().getString(R.string.up_pull_lab));
						refreshView.getLoadingLayoutProxy().setReleaseLabel(
								getResources().getString(
										R.string.up_release_lab));
						// Do work to refresh the list here.
						page += 1;
						if (!isLastPage) {
							ThreadPool.getInstance().execute(
									new ClubNearby(new EventProtocolHandler(),
											geoLat, geoLng, page, mQuery));
						}
					}
				});

		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						if (isLastPage) {
							mPullRefreshListView.onRefreshComplete();
							Toast.makeText(getActivity(), "最后一页啦",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		// mPullRefreshListView.onRefreshComplete();
	}

	/**
	 * Set adapter
	 *
	 * @return void
	 * @throw
	 */
	private void updateListView() {
		for (ClubBean item : mClubItems) {
			ItemNode node = new ItemNode();
			node.mName = item.getName();
			node.mAddress = item.getAddress();
			node.mPhone = item.getPhone();
			node.logo = item.getLogoUrl();
			mItemNodes.add(node);
		}
		log.d("mItemNodes.size():" + mItemNodes.size());
		// adapter = new ContentAdapter(this.getActivity(), mItemNodes);
		adapter.notifyDataSetChanged();
		mPullRefreshListView.setOnItemClickListener(this);
	}

	/**
	 * find club by key word, clear mClubItems and mItemNodes
	 *
	 * @param query
	 *            key word, from FragmentClub
	 * @return void
	 * @throw
	 */
	public void findClubByKeyWord(String query) {
		page = 1;
		mQuery = query;
		mClubItems.clear();
		mItemNodes.clear();
		ThreadPool.getInstance().execute(
				new ClubNearby(new EventProtocolHandler(), geoLat, geoLng,
						page, mQuery));
	}

	public class ItemNode {
		public String mName;
		public String mAddress;
		public String mPhone;
		public String logo;
	}

	private class ItemHolder {
		public ImageView mAvatarView;
		public TextView mNameView;
		public TextView mAddressView;
		public TextView mPhoneView;
	}

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
				convertView = mInflater.inflate(R.layout.listitem_club, null);
				holder = new ItemHolder();
				holder.mAvatarView = (ImageView) convertView
						.findViewById(R.id.clubitem_avatar);
				holder.mNameView = (TextView) convertView
						.findViewById(R.id.clubitem_name);
				holder.mAddressView = (TextView) convertView
						.findViewById(R.id.clubitem_address);
				holder.mPhoneView = (TextView) convertView
						.findViewById(R.id.clubitem_phone);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}

			holder.mNameView.setText(node.mName);
			holder.mAddressView.setText(node.mAddress);
			holder.mPhoneView.setText(node.mPhone);

			ImageLoader.getInstance().displayImage(node.logo,
					holder.mAvatarView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
						}
					});
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this.getActivity(), ClubInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("club", mClubItems.get(position - 1));
		intent.putExtras(bundle);
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

	public class EventProtocolHandler extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			mPullRefreshListView.onRefreshComplete();
			if (msg.what == ConstVar.HANDLER_MSG_SUCCESS) {
				log.d("mClubItems.size():" + mClubItems.size());
				ArrayList<ClubBean> clubBeans = (ArrayList<ClubBean>) msg.obj;
				if (clubBeans == null || clubBeans.size() == 0) {
					isLastPage = true;
				} else {
					mClubItems.addAll(clubBeans);
					updateListView();
				}
			}
		}
	}
}
