package com.shape100.gym.activity;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shape100.gym.R;
import com.shape100.gym.adapter.PhotoPageAdapter;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

/**
 * 相片滑动浏览器
 * 
 * @author yupu
 * @date 2015年2月5日
 */
public class SwitcherPhotoActivity extends BaseActivity implements
		OnClickListener {
	private static final String DATA = "pics";
	private static final String CURR = "curpage";
	private static final String LOCAL = "local";
	private static final String THUMB = "thumb";
	private ViewPager pager;
	private ArrayList<String> pics;
	private ArrayList<View> imgs;
	private int currPage;
	private PhotoPageAdapter adapter;
	private PhotoViewAttacher attacher;
	private boolean isLocal;
	private TextView pageText;
	private ImageView deleteView;
	private Bitmap curBitmap;

	public static void ActionStart(Context activity, ArrayList<String> data,
			int curr, boolean isLocal) {
		Intent intent = new Intent(activity, SwitcherPhotoActivity.class);
		intent.putStringArrayListExtra(DATA, data);
		intent.putExtra(CURR, curr);
		intent.putExtra(LOCAL, isLocal);
		activity.startActivity(intent);

	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_photo_pager);
		pics = getIntent().getStringArrayListExtra(DATA);
		currPage = getIntent().getIntExtra(CURR, 0);
		isLocal = getIntent().getBooleanExtra(LOCAL, true);
		imgs = new ArrayList<View>();
		initView();
	}

	private void selectPager(final int position) {
		currPage = position;
		pageText.setText(position + 1 + " / " + pics.size());
		final ImageView image = (ImageView) imgs.get(position).findViewById(
				R.id.img_photo_big);
		final ProgressBar bar = (ProgressBar) imgs.get(position).findViewById(
				R.id.bar_progressbar);
		attacher = new PhotoViewAttacher(image);
		String uri;
		
		if (isLocal) {
			deleteView.setImageResource(R.drawable.btn_delete);
			deleteView.setVisibility(View.GONE);
			uri = "file://" + pics.get(position);
		} else {
			uri = pics.get(position);
			deleteView.setVisibility(View.VISIBLE);
			deleteView.setImageResource(R.drawable.btn_save);
		}

//		if (pics.get(position).endsWith(".gif")) {
//			System.out.println("------------------------url2--" + url);
//			gifView.setGifImageType(GifImageType.COVER);
//			ImageLoader.getInstance().displayImage(largeUrl,
//					(ImageAware) gifView);
//			return;
//		}
		
		ImageLoader.getInstance().displayImage(uri, image,
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
						bar.setVisibility(View.INVISIBLE);
						attacher.update();
						if (isLocal == false) {
							ImageLoader.getInstance().displayImage(
									pics.get(position).replace("thumbnail",
											"large"), image,
									new ImageLoadingListener() {

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
											attacher.update();
											curBitmap = loadedImage;
											bar.setVisibility(View.INVISIBLE);
										}

										@Override
										public void onLoadingCancelled(
												String imageUri, View view) {
											bar.setVisibility(View.INVISIBLE);
										}
									});
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						bar.setVisibility(View.INVISIBLE);
					}
				});
	}

	private void initView() {
		pager = (ViewPager) findViewById(R.id.pager_photo);
		deleteView = (ImageView) findViewById(R.id.iv_delete_photo);
		deleteView.setOnClickListener(this);
		adapter = new PhotoPageAdapter(this);
		pager.setAdapter(adapter);
		pageText = (TextView) findViewById(R.id.tv_photo_title);
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < pics.size(); i++) {
			View view = inflater.inflate(R.layout.act_big_photo, null);
			imgs.add(view);
		}
		adapter.update(imgs);
		pager.setCurrentItem(currPage);
		selectPager(currPage);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				selectPager(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_delete_photo) {
			if (isLocal) {
				// imgs.remove(currPage);
				// if (imgs.size() > 0) {
				// adapter.update(imgs);
				// currPage = currPage > 0 ? currPage - 1 : 0;
				// pageText.setText(currPage + 1 + " / " + imgs.size());
				// } else {
				// finish();
				// }
			} else {
				if (curBitmap != null) {
					String name = MD5Tools.toMD5(pics.get(currPage).replace(
							"thumbnail", "large"))
							+ ".jpg";
					boolean issave = ImageUtils.insertPic(
							SwitcherPhotoActivity.this, curBitmap, name,
							FileUtils.getSaveImgPath());
					if (issave) {
						Toast.makeText(SwitcherPhotoActivity.this,
								"已保存至" + FileUtils.getSaveImgPath() + name,
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}
}
