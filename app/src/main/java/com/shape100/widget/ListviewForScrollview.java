package com.shape100.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListviewForScrollview extends ListView {

	public ListviewForScrollview(Context context) {
		super(context);
	}

	public ListviewForScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListviewForScrollview(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
