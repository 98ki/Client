/**    
 * file name：OwnerCoachFavoriteActivity.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-1 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.model.UserBean;
import com.shape100.gym.protocol.CoachFavorites;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

/**
 * 
 * project：shape100 class：OwnerCoachFavoriteActivity desc： author：zpy
 * zpy@98ki.com create date：2014-12-1 上午10:58:52 modify author: zpy update
 * date：2014-12-1 上午10:58:52 update remark：
 * 
 * @version
 * 
 */
public class FavoriteCoachActivity extends SlideActivity implements
		OnClickListener, OnItemClickListener {
	/* Data */
	private static final Logger log = Logger.getLogger("OwnerCoachFavorite");
	private static final String USERID = "userid";
	private ContentAdapter adapter;
	private DisplayImageOptions options;
	private ArrayList<UserBean> mCoachList = new ArrayList<UserBean>();
	private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();

	/* View */
	private ListView mCoachView;

	public static void ActionStart(Activity activity, long userID) {
		Intent intent = new Intent(activity, FavoriteCoachActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong(USERID, userID);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_owner_favorite_coach);
		findViewById(R.id.tv_back).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText(getResources()
				.getString(R.string.favorite_coach));
		mCoachView = (ListView) findViewById(R.id.lv_owner_favorite_coach);
		// init cache image options
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_unknown)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		// set listview adapter
		adapter = new ContentAdapter(this, mItemNodes);
		mCoachView.setAdapter(adapter);
		mCoachView.setOnItemClickListener(this);

		// get Bundle
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		long userId = bundle.getLong("userid");
		log.d("userid in list favorite coach");

		ThreadPool.getInstance().execute(
				new CoachFavorites(new EventProtocolHandler(), mCoachList,
						userId));
		// new CoachFavorites(new EventProtocolHandler(), mCoachList, userId)
		// .start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CoachPageActivity.ActionStart(FavoriteCoachActivity.this, mCoachList
				.get(position).getUserId());
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
			final ItemHolder holder;

			ItemNode node = mItemNodes.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listitem_user, null);
				holder = new ItemHolder();
				holder.mAvatarView = (ImageView) convertView
						.findViewById(R.id.useritem_avatar);
				holder.mNameView = (TextView) convertView
						.findViewById(R.id.useritem_name);
				holder.mAddressView = (TextView) convertView
						.findViewById(R.id.useritem_address);
				holder.mPhoneView = (TextView) convertView
						.findViewById(R.id.useritem_phone);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}

			holder.mNameView.setText(node.mName);
			holder.mAddressView.setText(node.mAddress);
			holder.mPhoneView.setText(node.mPhone);

			ImageLoader.getInstance().displayImage(node.logo,
					holder.mAvatarView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.mAvatarView
									.setImageResource(R.drawable.ic_unknown);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
						}
					});
			return convertView;
		}

	}

	public class ItemNode {
		public String mName;
		public String mAddress;
		public String mPhone;
		public String logo;
	};

	private class ItemHolder {
		public ImageView mAvatarView;
		public TextView mNameView;
		public TextView mAddressView;
		public TextView mPhoneView;
	};

	/**
	 * 
	 * 
	 * project：shape100 class：EventProtocolHandler desc： author：zpy zpy@98ki.com
	 * create date：2014-12-1 上午11:06:04 modify author: zpy update date：2014-12-1
	 * 上午11:06:04 update remark：
	 * 
	 * @version
	 *
	 */
	private class EventProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				for (int i = 0; i < mCoachList.size(); i++) {
					ItemNode node = new ItemNode();
					node.mName = mCoachList.get(i).getName();
					node.logo = mCoachList.get(i).getProfileImageUrl();
					mItemNodes.add(node);
				}
				adapter.notifyDataSetChanged();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:

				break;
			case ConstVar.HANDLER_MSG_ERROR:
				Toast.makeText(MainApplication.sContext, "请检查网络是否正确！",
						Toast.LENGTH_SHORT).show();
				break;

			}
		}
	}

}
