package com.shape100.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;

public class BottomPop extends PopupWindow implements OnClickListener {
	public static final int REMOVE_FLAG = 1;
	public static final int COPY_FLAG = 2;
	public static final int CANCEL_FLAG = 3;
	private LayoutInflater inflater;
	private BottomPopListener listener;
	private long id;
	private View view;

	public BottomPop(Context context, long id, View up_view) {
		listener = (BottomPopListener) context;
		this.id = id;
		view = up_view;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}

	@Override
	public void dismiss() {
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
		}
		super.dismiss();
	}

	private void init() {
		View view = inflater.inflate(R.layout.pop_comment_bottom, null);
		TextView removeView = (TextView) view.findViewById(R.id.tv_remove);
		removeView.setOnClickListener(this);
		view.findViewById(R.id.tv_copy).setOnClickListener(this);
		view.findViewById(R.id.tv_cancel).setOnClickListener(this);
		if (id == AppConfig.getInstance().getUserId()) {
			removeView.setVisibility(View.VISIBLE);
		} else {
			removeView.setVisibility(View.GONE);
		}
		setContentView(view);
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setOutsideTouchable(true);
		setAnimationStyle(R.style.AnimBottom);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_remove:
			listener.clickMenu(REMOVE_FLAG);
			break;
		case R.id.tv_copy:
			listener.clickMenu(COPY_FLAG);
			break;
		case R.id.tv_cancel:
			listener.clickMenu(CANCEL_FLAG);
			break;
		default:
			break;
		}
	}

	public interface BottomPopListener {
		void clickMenu(int flag);
	}

}
