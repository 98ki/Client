package com.shape100.widget;




import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
public class CurrItemLayout  extends RelativeLayout {
    public CurrItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public CurrItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public CurrItemLayout(Context context) {
        super(context);
    }
 
    /**
     * Set every item's height by measure width
     */
    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
 
        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        // Height is double width
        int childHeightSize = getMeasuredHeight();
        
         widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
         heightMeasureSpec = widthMeasureSpec *2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    */
}


