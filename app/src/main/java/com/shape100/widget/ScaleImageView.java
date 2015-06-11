package com.shape100.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 按width拉伸height
 * 
 * @author yupu
 * @date 2015年1月12日
 */
public class ScaleImageView extends ImageView {

	public ScaleImageView(Context context) {
		super(context);
	}

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getMeasuredWidth();
		Drawable drawable = getDrawable();
		if (width != 0 && drawable != null) {
			float scale = drawable.getMinimumHeight() * 1.0f
					/ drawable.getMinimumWidth();
			int height = (int) (width * scale);
			setMeasuredDimension(width, height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
