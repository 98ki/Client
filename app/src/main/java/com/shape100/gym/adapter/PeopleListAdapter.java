package com.shape100.gym.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.Event;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.Friendships;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.AccountDetailUtil;

/**
 * 关注列表数据适配器
 * 
 * @author yupu
 * @date 2015年3月20日
 */
public class PeopleListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<UserInfo> data;
	private int pos;
	private ViewHolder hold;

	public PeopleListAdapter(Context context) {
		this.context = context;
	}

	public void upDate(ArrayList<UserInfo> userInfos) {
		data = userInfos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_peoplelist_concern, parent, false);
			holder = new ViewHolder();
			holder.headview = (ImageView) view
					.findViewById(R.id.iv_concern_head);
			holder.name = (TextView) view.findViewById(R.id.tv_concern_name);
			holder.text = (TextView) view.findViewById(R.id.tv_concern_status);
			holder.concern = (TextView) view.findViewById(R.id.tv_concern);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ImageLoader.getInstance().displayImage(
				data.get(position).getUser().getProfileImageUrl(),
				holder.headview,
				MainApplication.getInstance().getDisplayImageOptions(
						R.drawable.ic_unknown));
		holder.name.setText(data.get(position).getUser().getName());
		if (data.get(position).getStatus() != null) {
			holder.text.setText("最近动态: "
					+ data.get(position).getStatus().getText()
							.replace("\n", ""));
		} else {
			holder.text.setText("最近动态：这个人很懒，最近什么也没有留下");
		}

		if (data.get(position).getUser().getUserId() == AppConfig.getInstance()
				.getUserId()) {
			holder.concern.setVisibility(View.GONE);
		} else {
			holder.concern.setVisibility(View.VISIBLE);
		}

		if (data.get(position).getUser().isFollowing()) {
			holder.isConcern = true;
		} else {
			holder.isConcern = false;
		}

		setConcern(holder, position);
		holder.concern.setTag(holder);

		holder.concern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hold = (ViewHolder) v.getTag();
				hold.concern.setClickable(false);
				pos = position;
				if (hold.isConcern) {
					ThreadPool.getInstance().execute(
							new Friendships(data.get(position).getUser()
									.getUserId(), new ResultHandler(),
									Friendships.DESTORY));
				} else {
					ThreadPool.getInstance().execute(
							new Friendships(data.get(position).getUser()
									.getUserId(), new ResultHandler(),
									Friendships.CREAT));
				}
			}
		});

		return view;
	}

	private void setConcern(ViewHolder holder, int position) {
		if (holder.isConcern) {
			holder.concern.setBackgroundResource(R.drawable.gray_corner);
			holder.concern.setText(context.getResources().getString(
					R.string.concern_cancle_str));
		} else {
			holder.concern.setBackgroundResource(R.drawable.green_corner);
			holder.concern.setText(context.getResources().getString(
					R.string.concern_str));
		}
	}

	class ViewHolder {
		ImageView headview;
		TextView name;
		TextView text;
		TextView concern;
		boolean isConcern;
	}

	class ResultHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Event.CREAT_CONCERN:
				if (hold != null) {
					hold.concern.setClickable(true);
				}
				hold.isConcern = !hold.isConcern;
				data.get(pos).getUser().setFollowing(hold.isConcern);
				notifyDataSetChanged();
				break;
			case Event.CANCEL_CONCERN:
				if (hold != null) {
					hold.concern.setClickable(true);
				}
				hold.isConcern = !hold.isConcern;
				data.get(pos).getUser().setFollowing(hold.isConcern);
				notifyDataSetChanged();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
}
