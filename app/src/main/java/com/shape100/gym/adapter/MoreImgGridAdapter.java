package com.shape100.gym.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shape100.gym.R;

public class MoreImgGridAdapter extends BaseAdapter {
	private List<String> picPath;
	private Context context;
	private CheckItemListener listener;
	private int[] visited;
	private int COUNT = 0;
	private int flag = 0; // 标记是否全部刷新，防止闪屏

	public MoreImgGridAdapter(Context context) {
		this.context = context;
		picPath = new ArrayList<String>();
	}

	public void upDate(List<String> picth, int count) {
		this.COUNT = count;
		this.picPath = picth;
		visited = new int[picPath.size()];
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return picPath.size();
	}

	@Override
	public Object getItem(int position) {
		return picPath.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_img_gride, parent, false);
			holder.img = (ImageView) view.findViewById(R.id.img);
			holder.check = (ImageView) view.findViewById(R.id.img_check);
			// holder.check.setTag(false);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (position == 0) {
			holder.img.setImageResource(R.drawable.icon_camera);
			// ImageLoader.getInstance().displayImage(, imageAware, listener);
			holder.check.setVisibility(View.GONE);
			holder.img.setClickable(true);
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onClickPhoto();
				}
			});
		} else {
			holder.check.setVisibility(View.VISIBLE);
			holder.img.setClickable(false);
			ImageLoader.getInstance().displayImage(
					"file://" + picPath.get(position), holder.img,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							if (flag == 0) {
								holder.img
										.setImageResource(R.drawable.icon_default_bg);
							}
							super.onLoadingStarted(imageUri, view);
						}
					});

			if (visited != null && visited[position] == 1) {
				holder.check
						.setImageResource(R.drawable.compose_photo_preview_right);
			} else {
				holder.check
						.setImageResource(R.drawable.compose_photo_preview_default);
			}

			holder.check.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] url = picPath.get(position).split("/");
					if (url[url.length - 1].endsWith(".jpg")
							|| url[url.length - 1].endsWith(".png")
							|| url[url.length - 1].endsWith(".gif")) {
						if (visited[position] == 1) {
							listener.onClickItem(position, false);
							COUNT--;
							visited[position] = 0;
						} else {
							if (COUNT < 9) {
								listener.onClickItem(position, true);
								visited[position] = 1;
								COUNT++;
							} else {
								Toast.makeText(context, "最多只能添加9张哦",
										Toast.LENGTH_LONG).show();
							}
						}
						flag = 1;
						notifyDataSetChanged();
					} else {
						String name = url[url.length - 1];
						// System.out.println("-----------------"
						// + text.length);
						Toast.makeText(context, "暂不支持" + name + "格式的图片",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
		return view;
	}

	class ViewHolder {
		ImageView img;
		ImageView check;
	}

	public void setOnCheckListener(CheckItemListener listener) {
		this.listener = listener;
	}

	public interface CheckItemListener {
		void onClickItem(int position, boolean flag);

		void onClickPhoto();
	}
}
