package com.shape100.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shape100.gym.R;

public class MenuPop extends PopupWindow {
	private ArrayList<String> items;
	private MenuItemListener listener;
	private LayoutInflater inflater;

	public MenuPop(Context context, ArrayList<String> items) {
		this.listener = (MenuItemListener) context;
		this.items = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (items != null && items.size() != 0) {
			init();
		}
	}

	private void init() {
		int count = items.size();
		View view = inflater.inflate(R.layout.menu_pop, null, false);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismiss();
				return true;
			}
		});
		LinearLayout linearLayout = (LinearLayout) view
				.findViewById(R.id.menu_container);
		setContentView(view);
		setHeight(LayoutParams.MATCH_PARENT); //
		setWidth(LayoutParams.MATCH_PARENT); // 可以自定义改变
		setFocusable(true); // 使窗口可点击
		setOutsideTouchable(true); // 设置点击外面可点
		setBackgroundDrawable(new BitmapDrawable()); // 设置背景不为空，才可以外点击
		setAnimationStyle(R.style.pop_animotion);

		for (int i = 0; i < count; i++) {
			View v = inflater.inflate(R.layout.include_text, null);
			linearLayout.addView(v);
			TextView text = (TextView) v.findViewById(R.id.tv_pop_include);
			text.setText(items.get(i));
			text.setBackgroundResource(R.drawable.btn_bg_white);
			text.setTag(i);
			
			if (i == count - 1) {
				v.findViewById(R.id.view_line).setVisibility(View.INVISIBLE);
			}
			
			text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onItemClick((int) v.getTag());
				}
			});
		}
	}

	public interface MenuItemListener {
		void onItemClick(int pos);
	}
}
