package com.shape100.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 注意grideview会抢夺事件
 * 
 * @author yupu
 * @date 2015年3月6日
 */
public class GrideforListview extends GridView {

	public GrideforListview(Context context) {
		super(context);
	}

	public GrideforListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GrideforListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// return true; // true 拦截事件自己处理，禁止向下传递
	// }
	//
	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// return false; //false 自己不处理此次事件以及后续的事件，那么向上传递给外层view
	// }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
