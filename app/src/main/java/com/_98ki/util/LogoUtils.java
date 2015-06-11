package com._98ki.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.shape100.gym.MainApplication;
import com.shape100.gym.R;

public class LogoUtils {
	public static Bitmap bitmapLogo; // 俱乐部的logo

	// 在这里预先保存一下logo图片,分享时候用
	public static void saveLogo(final String url, final String name) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bit = null;
				if (!TextUtils.isEmpty(url)) {
					bit = ImageUtils.getBitmapFromUrl(url, 1000 * 20);
				} else {
					bit = BitmapFactory.decodeResource(
							MainApplication.sContext.getResources(),
							R.drawable.ic_club_default_logo);
				}

				if (bit != null) {
					// bit = Bitmap.createScaledBitmap(bit, 80, 80, true);
					ImageUtils.bitmapToPngFile(bit, name);
				}
			}
		}).start();
	}
}
