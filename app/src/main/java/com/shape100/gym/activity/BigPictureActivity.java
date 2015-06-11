package com.shape100.gym.activity;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

/**
 * 获取大图片
 * 
 * @author yupu
 * @date 2015年1月12日
 */
public class BigPictureActivity extends BaseActivity implements OnClickListener {
	private Logger log = Logger.getLogger("Big_picture_Activity");
	public static final String URL = "url";
	private ImageView mHeadView;
	private ProgressBar bar;
	private PhotoViewAttacher attacher;
	private ImageView savePic;
	private Bitmap bitmap;
	private String largeUrl;

	/**
	 * 
	 * @author yupu
	 * @date 2015年1月11日
	 * @param style
	 *            教练，自己
	 */
	public static void ActionStart(Activity activity, String url) {
		Intent intent = new Intent(activity, BigPictureActivity.class);
		intent.putExtra(URL, url);
		activity.startActivity(intent);
		activity.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_big_photo);
		mHeadView = (ImageView) findViewById(R.id.img_photo_big);
		savePic = (ImageView) findViewById(R.id.iv_save_img);
		savePic.setOnClickListener(this);
		savePic.setVisibility(View.VISIBLE);
		attacher = new PhotoViewAttacher(mHeadView);

		bar = (ProgressBar) findViewById(R.id.bar_progressbar);
		loadImage(getIntent().getStringExtra(URL));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	private void loadImage(final String url) {
		if (url.contains("square")) {
			largeUrl = url.replace("square", "large");
		} else if (url.contains("thumbnail")) {
			largeUrl = url.replace("thumbnail", "large");
		}
		
//		if (url.endsWith(".gif")) {
//			gifView.setGifImageType(GifImageType.COVER);
//			ImageLoader.getInstance().displayImage(largeUrl,
//					(ImageAware) gifView);
//			return;
//		}
		
		ImageLoader.getInstance().displayImage(url, mHeadView,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						bar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						bar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						attacher.update();
						bar.setVisibility(View.INVISIBLE);
						ImageLoader.getInstance().displayImage(largeUrl,
								mHeadView, new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(
											String imageUri, View view) {
										bar.setVisibility(View.VISIBLE);
									}

									@Override
									public void onLoadingFailed(
											String imageUri, View view,
											FailReason failReason) {
										bar.setVisibility(View.INVISIBLE);
									}

									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										bar.setVisibility(View.INVISIBLE);
										bitmap = loadedImage;
										attacher.update();
									}

									@Override
									public void onLoadingCancelled(
											String imageUri, View view) {
										bar.setVisibility(View.INVISIBLE);
									}
								});
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						bar.setVisibility(View.INVISIBLE);
					}
				});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_save_img) {
			if (bitmap != null) {
				String name = MD5Tools.toMD5(largeUrl) + ".jpg";
				boolean issave = ImageUtils.insertPic(BigPictureActivity.this,
						bitmap, name, FileUtils.getSaveImgPath());
				if (issave) {
					Toast.makeText(BigPictureActivity.this,
							"已保存至" + FileUtils.getSaveImgPath() + name,
							Toast.LENGTH_LONG).show();
				}
			}

		}

	}
}
