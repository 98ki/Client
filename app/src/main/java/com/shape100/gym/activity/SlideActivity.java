package com.shape100.gym.activity;

import android.view.MotionEvent;
import android.view.VelocityTracker;

public class SlideActivity extends BaseActivity {
	private VelocityTracker tracker; // 得到手指滑动速度
	private static int VELOCITY = 500;
	private float downx, downy;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			downx = ev.getX();
			downy = ev.getY();
			if (tracker == null) {
				tracker = VelocityTracker.obtain();
				tracker.addMovement(ev);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (tracker != null) {
				tracker.addMovement(ev);
			}
			break;
		case MotionEvent.ACTION_UP:
			int veloctyX = 0;
			int veloctyY = 0;
			float x = ev.getX() - downx;
			float y = Math.abs(ev.getY() - downy);
			if (tracker != null) {
				tracker.addMovement(ev);
				tracker.computeCurrentVelocity(1000); // 每秒滑动的像素数
				veloctyX = (int) tracker.getXVelocity();
				veloctyY = (int) tracker.getYVelocity();
			}

			// System.out.println("------------滑动速度-X--" + veloctyX + "-----Y-"
			// + veloctyY);
			// System.out.println("------------距离--x=" + x + "--y=" + y);

			if (veloctyX > VELOCITY && veloctyY < VELOCITY && x > 200
					&& y < 150) {
				this.finish();
			}

			if (tracker != null) {
				tracker.recycle();
				tracker = null;
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
