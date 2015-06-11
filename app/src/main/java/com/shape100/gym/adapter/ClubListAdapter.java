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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.model.ClubBean;

public class ClubListAdapter extends BaseAdapter {
	private Context context;
	private List<ClubBean> clubBeans;
	private JoinListener listener;

	public ClubListAdapter(Context context, JoinListener listener) {
		this.context = context;
		clubBeans = new ArrayList<ClubBean>();
		this.listener = listener;
	}

	public void upDate(List<ClubBean> clubBeans) {
		this.clubBeans = clubBeans;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return clubBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return clubBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Viewholder holder;
		if (view == null) {
			holder = new Viewholder();
			view = LayoutInflater.from(context).inflate(R.layout.listitem_club,
					parent, false);
			holder.name = (TextView) view.findViewById(R.id.clubitem_name);
			holder.address = (TextView) view
					.findViewById(R.id.clubitem_address);
			holder.phone = (TextView) view.findViewById(R.id.clubitem_phone);
			holder.pic = (ImageView) view.findViewById(R.id.clubitem_avatar);
			holder.join = (TextView) view.findViewById(R.id.tv_join);
			view.setTag(holder);
		} else {
			holder = (Viewholder) view.getTag();
		}

		ImageLoader.getInstance().displayImage(
				clubBeans.get(position).getLogoUrl(),
				holder.pic,
				MainApplication.getInstance().getDisplayImageOptions(
						R.drawable.ic_club_default_logo));
		holder.name.setText(clubBeans.get(position).getName());
		holder.address.setText(clubBeans.get(position).getAddress());
		holder.phone.setText(clubBeans.get(position).getPhone());
		holder.join.setVisibility(View.VISIBLE);
		holder.join.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClickJoin((TextView) v, position);
			}
		});

		return view;
	}

	class Viewholder {
		ImageView pic;
		TextView name;
		TextView address;
		TextView phone;
		TextView join;
	}

	public interface JoinListener {
		void onClickJoin(TextView text, int position);
	}
}
