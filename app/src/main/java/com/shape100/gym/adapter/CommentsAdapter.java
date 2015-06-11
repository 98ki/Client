package com.shape100.gym.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.R;
import com.shape100.gym.activity.CoachPageActivity;
import com.shape100.gym.model.CommentsData;
import com.shape100.gym.protocol.DeleteDynamic;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.AccountUtil;

public class CommentsAdapter extends BaseAdapter implements OnClickListener {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"MM.dd HH:mm", Locale.CHINA);
	private Context context;
	private ArrayList<CommentsData> data;

	public CommentsAdapter(Context context) {
		this.context = context;
		data = new ArrayList<CommentsData>();
	}

	public void update(ArrayList<CommentsData> commentsDatas) {
		this.data = commentsDatas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size() > 0 ? data.size() - 1 : 0;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_comment,
					parent, false);
			holder.content = (TextView) view
					.findViewById(R.id.tv_comment_content);
			holder.name = (TextView) view.findViewById(R.id.tv_commenter_name);
			holder.time = (TextView) view.findViewById(R.id.tv_commenter_time);
			holder.pic = (ImageView) view
					.findViewById(R.id.iv_commenter_avatar);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(data.get(position).getUser().getName());
		Date date = new Date(data.get(position).getCreated_at());
		holder.pic.setOnClickListener(this);
		holder.pic.setTag(position);
		holder.time.setText(sdf.format(date));
		String rep = "";
		if (data.get(position).getIn_reply_to_status_id() != 0) {
			rep = "回复 @" + data.get(position).getIn_reply_to_fullname() + ": ";
			SpannableString ss = new SpannableString(rep
					+ data.get(position).getText());
			ss.setSpan(new ForegroundColorSpan(Color.GREEN), "回复".length(),
					rep.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.content.setText(ss);
		} else {
			holder.content.setText(data.get(position).getText());
		}

		ImageLoader.getInstance().displayImage(
				data.get(position).getUser().getProfileImageUrl(), holder.pic);
		return view;
	}

	class ViewHolder {
		ImageView pic;
		TextView content;
		TextView name;
		TextView time;
	}

	@Override
	public void onClick(View v) {
		int pos;
		switch (v.getId()) {
		case R.id.iv_commenter_avatar:
			pos = (int) v.getTag();
			CoachPageActivity.ActionStart(context, data.get(pos).getUser()
					.getUserId());
			break;
		default:
			break;
		}
	}
}
